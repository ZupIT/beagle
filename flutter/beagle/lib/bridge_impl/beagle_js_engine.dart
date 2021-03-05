/*
 *
 *  Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import 'dart:convert';

import 'package:beagle/bridge_impl/beagle_js_request_message.dart';
import 'package:beagle/bridge_impl/beagle_navigator_js.dart';
import 'package:beagle/bridge_impl/beagle_view_js.dart';
import 'package:beagle/bridge_impl/js_runtime_wrapper.dart';
import 'package:beagle/interface/beagle_navigator.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/interface/storage.dart';
import 'package:beagle/interface/types.dart';
import 'package:beagle/model/beagle_action.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle/model/response.dart';
import 'package:beagle/networking/beagle_network_options.dart';
import 'package:beagle/networking/beagle_request.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_js/flutter_js.dart';

typedef ActionListener = void Function({
  BeagleAction action,
  BeagleView view,
  BeagleUIElement element,
});

typedef HttpListener = void Function(String requestId, BeagleRequest request);

/// Provides an interface to run javascript code and listen to Beagle's core
/// events.
class BeagleJSEngine {
  BeagleJSEngine(
    JavascriptRuntimeWrapper jsRuntime,
    Storage storage,
  )   : assert(jsRuntime != null),
        _jsRuntime = jsRuntime,
        _storage = storage;

  final JavascriptRuntimeWrapper _jsRuntime;
  BeagleJSEngineState _engineState = BeagleJSEngineState.CREATED;

  final _httpRequestChannelName = 'httpClient.request';
  final _actionChannelName = 'action';
  final _viewUpdateChannelName = 'beagleView.update';
  final _navigatorChannelName = 'beagleNavigator';

  HttpListener _httpListener;
  final Map<String, List<ActionListener>> _viewActionListenerMap = {};
  final Map<String, List<ViewUpdateListener>> _viewUpdateListenerMap = {};
  final Map<String, List<ViewErrorListener>> _viewErrorListenerMap = {};
  final Map<String, List<NavigationListener>> _navigationListenerMap = {};
  final Storage _storage;

  /// Runs javascript [code].
  /// It throws [BeagleJSEngineException] if [BeagleJSEngine] isn't started.
  JsEvalResult evaluateJavascriptCode(String code) {
    _checkEngineIsStarted();
    return _jsRuntime.evaluate(code);
  }

  void _checkEngineIsStarted() {
    if (!_isEngineStarted()) {
      throw BeagleJSEngineException(
          'BeagleJSEngine has not been started. Did you miss to call BeagleJSEngine.start()?');
    }
  }

  bool _isEngineStarted() {
    return _engineState == BeagleJSEngineState.STARTED;
  }

  BeagleJSEngineState get state => _engineState;

  dynamic _deserializeJsFunctions(dynamic value, [String viewId]) {
    if (value.runtimeType.toString() == 'String' &&
        value.toString().startsWith('__beagleFn:')) {
      return ([Map<String, dynamic> argumentsMap]) {
        final args = argumentsMap == null
            ? "'$value'"
            : "'$value', ${json.encode(argumentsMap)}";
        final jsMethod =
            viewId == null ? 'call(' : "callViewFunction('$viewId', ";
        final result = _jsRuntime.evaluate('global.beagle.$jsMethod$args)');
        debugPrint('dynamic function result: $result');
      };
    }

    if (value.runtimeType.toString() == 'List<dynamic>') {
      // ignore: avoid_as
      return (value as List<dynamic>)
          .map((item) => _deserializeJsFunctions(item, viewId))
          .toList();
    }

    if (value.runtimeType.toString() ==
        '_InternalLinkedHashMap<String, dynamic>') {
      // ignore: avoid_as
      final map = value as Map<String, dynamic>;
      final result = <String, dynamic>{};
      final keys = map.keys;

      // ignore: cascade_invocations, avoid_function_literals_in_foreach_calls
      keys.forEach((key) {
        result[key] = _deserializeJsFunctions(map[key], viewId);
      });
      return result;
    }

    return value;
  }

  void _setupMessages() {
    _setupHttpMessages();
    _setupActionMessages();
    _setupBeagleViewMessages();
    _setupBeagleNavigatorMessages();
    _setupStorageMessages();
  }

  void _setupHttpMessages() {
    _jsRuntime.onMessage(
      _httpRequestChannelName,
      _notifyHttpListener,
    );
  }

  void _notifyHttpListener(dynamic requestMessage) {
    if (_httpListener == null) {
      return;
    }

    final jsRequestMessage = BeagleJSRequestMessage.fromJson(requestMessage);
    final requestId = jsRequestMessage.requestId;
    final req = jsRequestMessage.toRequest();

    _httpListener(requestId, req);
  }

  void _setupActionMessages() {
    _jsRuntime.onMessage(
      _actionChannelName,
      _notifyActionListener,
    );
  }

  void _notifyActionListener(dynamic actionMessage) {
    final viewId = actionMessage['viewId'];

    if (!_hasActionListenerForView(viewId)) {
      return;
    }

    final action =
        BeagleAction(_deserializeJsFunctions(actionMessage['action']));

    final view = BeagleViewJS.views[viewId];
    final element = BeagleUIElement(actionMessage['element']);

    for (final listener in _viewActionListenerMap[viewId]) {
      listener(action: action, view: view, element: element);
    }
  }

  bool _hasActionListenerForView(String viewId) {
    return _viewActionListenerMap.containsKey(viewId) &&
        _viewActionListenerMap[viewId].isNotEmpty;
  }

  void _setupBeagleViewMessages() {
    _jsRuntime.onMessage(
      _viewUpdateChannelName,
      _notifyViewUpdateListeners,
    );
  }

  void _notifyViewUpdateListeners(dynamic updateMessage) {
    final viewId = updateMessage['id'];

    if (!_hasUpdateListenerForView(viewId)) {
      return;
    }

    final deserialized = _deserializeJsFunctions(updateMessage['tree'], viewId);
    final uiElement = BeagleUIElement(deserialized);

    for (final listener in _viewUpdateListenerMap[viewId]) {
      listener(uiElement);
    }
  }

  bool _hasUpdateListenerForView(String viewId) {
    return _viewUpdateListenerMap.containsKey(viewId) &&
        _viewUpdateListenerMap[viewId].isNotEmpty;
  }

  void _setupBeagleNavigatorMessages() {
    _jsRuntime.onMessage(
      _navigatorChannelName,
      _notifyNavigationListeners,
    );
  }

  void _notifyNavigationListeners(dynamic navigationMessage) {
    final viewId = navigationMessage['viewId'];

    if (!_hasNavigationListenerForView(viewId)) {
      return;
    }

    final route = BeagleNavigatorJS.mapToRoute(navigationMessage['route']);

    for (final listener in _navigationListenerMap[viewId]) {
      listener(route);
    }
  }

  bool _hasNavigationListenerForView(String viewId) {
    return _navigationListenerMap.containsKey(viewId) &&
        _navigationListenerMap[viewId].isNotEmpty;
  }

  void _setupStorageMessages() {
    _jsRuntime
      ..onMessage('storage.set', (dynamic args) async {
        final key = args['key'];
        final value = args['value'];
        final promiseId = args['promiseId'];
        await _storage.setItem(key, value);
        _jsRuntime.evaluate("global.beagle.promise.resolve('$promiseId')");
      })
      ..onMessage('storage.get', (dynamic args) async {
        final key = args['key'];
        final promiseId = args['promiseId'];
        final result = await _storage.getItem(key);
        _jsRuntime.evaluate(
            "global.beagle.promise.resolve('$promiseId', ${jsonEncode(result)})");
      })
      ..onMessage('storage.remove', (dynamic args) async {
        final key = args['key'];
        final promiseId = args['promiseId'];
        await _storage.removeItem(key);
        _jsRuntime.evaluate("global.beagle.promise.resolve('$promiseId')");
      })
      ..onMessage('storage.clear', (dynamic args) async {
        final promiseId = args['promiseId'];
        await _storage.clear();
        _jsRuntime.evaluate("global.beagle.promise.resolve('$promiseId')");
      });
  }

  /// Handles a javascript promise.
  /// It throws [BeagleJSEngineException] if [BeagleJSEngine] isn't started.
  Future<JsEvalResult> promiseToFuture(JsEvalResult result) {
    _checkEngineIsStarted();
    return _jsRuntime.handlePromise(result);
  }

  /// Lazily starts the [BeagleJSEngine].
  /// This method must be called before any attempt to interact with Beagle's
  /// javascript core.
  Future<void> start() async {
    if (!_isEngineStarted()) {
      _engineState = BeagleJSEngineState.STARTED;
      _jsRuntime.enableHandlePromises();
      _setupMessages();
      final beagleJS =
          await rootBundle.loadString('packages/beagle/assets/js/beagle.js');
      _jsRuntime.evaluate('var window = global = globalThis;');
      final bundleResult = await _jsRuntime.evaluateAsync(beagleJS);
      debugPrint('Initialization result: $bundleResult');
    }
  }

  /// Creates a new BeagleView and returns the created view id.
  String createBeagleView({
    BeagleNetworkOptions networkOptions,
    String initialControllerId,
  }) {
    final params = [BeagleNetworkOptions.toJsonEncode(networkOptions)];
    if (initialControllerId != null) {
      params.add(initialControllerId);
    }
    final script = 'global.beagle.createBeagleView(${params.join(', ')})';
    final id = _jsRuntime.evaluate(script).stringResult;

    return id;
  }

  // ignore: use_setters_to_change_properties
  RemoveListener onAction(String viewId, ActionListener listener) {
    _viewActionListenerMap[viewId] = _viewActionListenerMap[viewId] ?? [];
    _viewActionListenerMap[viewId].add(listener);
    return () {
      _viewActionListenerMap[viewId].remove(listener);
    };
  }

  // ignore: use_setters_to_change_properties
  void onHttpRequest(HttpListener listener) {
    _httpListener = listener;
  }

  RemoveListener onViewUpdate(String viewId, ViewUpdateListener listener) {
    _viewUpdateListenerMap[viewId] = _viewUpdateListenerMap[viewId] ?? [];
    _viewUpdateListenerMap[viewId].add(listener);
    return () {
      _viewUpdateListenerMap[viewId].remove(listener);
    };
  }

  RemoveListener onViewUpdateError(String viewId, ViewErrorListener listener) {
    _viewErrorListenerMap[viewId] = _viewErrorListenerMap[viewId] ?? [];
    _viewErrorListenerMap[viewId].add(listener);
    return () {
      _viewErrorListenerMap[viewId].remove(listener);
    };
  }

  RemoveListener onNavigate(String viewId, NavigationListener listener) {
    _navigationListenerMap[viewId] = _navigationListenerMap[viewId] ?? [];
    _navigationListenerMap[viewId].add(listener);
    return () {
      _navigationListenerMap[viewId].remove(listener);
    };
  }

  void removeViewListeners(String viewId) {
    _viewUpdateListenerMap.remove(viewId);
    _viewErrorListenerMap.remove(viewId);
    _viewActionListenerMap.remove(viewId);
  }

  void callJsFunction(String functionId, [Map<String, dynamic> argumentsMap]) {
    final args = argumentsMap == null
        ? "'$functionId'"
        : "'$functionId', ${json.encode(argumentsMap)}";
    final result = _jsRuntime.evaluate('global.beagle.call($args)');
    debugPrint('dynamic function result: $result');
  }

  void respondHttpRequest(String id, Response response) {
    final result = _jsRuntime.evaluate(
        'global.beagle.httpClient.respond($id, ${response.toJson()})');
    debugPrint('httpClient.respond result: $result');
  }
}

class BeagleJSEngineException implements Exception {
  BeagleJSEngineException(this._message);

  final String _message;

  @override
  String toString() => _message;
}

enum BeagleJSEngineState { CREATED, STARTED }
