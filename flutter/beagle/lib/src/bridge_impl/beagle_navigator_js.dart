/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import 'dart:convert';

import 'package:beagle/beagle.dart';

import 'beagle_js_engine.dart';

class BeagleNavigatorJS implements BeagleNavigator {
  BeagleNavigatorJS(this._beagleJSEngine, this._viewId);

  final String _viewId;
  final BeagleJSEngine _beagleJSEngine;

  static String routeToJson(Route route) {
    var map = <String, dynamic>{};

    if (route is LocalView) {
      map = {'screen': route.screen.properties};
    }

    if (route is RemoteView) {
      map = {
        'url': route.url,
        'fallback': route.fallback?.properties,
        'shouldPrefetch': route.shouldPrefetch
      };
    }

    return jsonEncode(map);
  }

  static Route mapToRoute(Map<String, dynamic> routeMap) {
    if (routeMap.containsKey('url')) {
      final fallback = routeMap.containsKey('fallback')
          ? BeagleUIElement(routeMap['fallback'])
          : null;
      final shouldPrefetch = routeMap.containsKey('shouldPrefetch')
          ? routeMap['shouldPrefetch']
          : false;
      return RemoteView(routeMap['url'],
          fallback: fallback, shouldPrefetch: shouldPrefetch);
    }

    if (routeMap.containsKey('screen')) {
      return LocalView(BeagleUIElement(routeMap['screen']));
    }

    return null;
  }

  @override
  T getCurrentRoute<T extends Route>() {
    final result = _beagleJSEngine
        .evaluateJavascriptCode(
            "global.beagle.getViewById('$_viewId').getNavigator().getCurrentRoute()")
        .rawResult;

    if (result == null) {
      return null;
    }

    return mapToRoute(result);
  }

  @override
  bool isEmpty() {
    return _beagleJSEngine
        .evaluateJavascriptCode(
            "global.beagle.getViewById('$_viewId').getNavigator().isEmpty()")
        .rawResult;
  }

  @override
  Future<void> popStack() {
    final result = _beagleJSEngine.evaluateJavascriptCode(
        "global.beagle.getViewById('$_viewId').getNavigator().popStack()");
    return _beagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> popToView(String routeIdentifier) {
    final result = _beagleJSEngine.evaluateJavascriptCode(
        "global.beagle.getViewById('$_viewId').getNavigator().popToView('$routeIdentifier')");
    return _beagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> popView() {
    final result = _beagleJSEngine.evaluateJavascriptCode(
        "global.beagle.getViewById('$_viewId').getNavigator().popView()");
    return _beagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> pushStack(Route route, [String controllerId]) {
    final routeJson = routeToJson(route);
    final args =
        controllerId == null ? routeJson : "$routeJson, '$controllerId'";
    final result = _beagleJSEngine.evaluateJavascriptCode(
        "global.beagle.getViewById('$_viewId').getNavigator().pushStack($args)");
    return _beagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> pushView(Route route) {
    final routeJson = routeToJson(route);
    final result = _beagleJSEngine.evaluateJavascriptCode(
        "global.beagle.getViewById('$_viewId').getNavigator().pushView($routeJson)");
    return _beagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> resetApplication(Route route, [String controllerId]) {
    final routeJson = routeToJson(route);
    final args =
        controllerId == null ? routeJson : "$routeJson, '$controllerId'";
    final result = _beagleJSEngine.evaluateJavascriptCode(
        "global.beagle.getViewById('$_viewId').getNavigator().resetApplication($args)");
    return _beagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> resetStack(Route route, [String controllerId]) {
    final routeJson = routeToJson(route);
    final args =
        controllerId == null ? routeJson : "$routeJson, '$controllerId'";
    final result = _beagleJSEngine.evaluateJavascriptCode(
        "global.beagle.getViewById('$_viewId').getNavigator().resetStack($args)");
    return _beagleJSEngine.promiseToFuture(result);
  }

  @override
  RemoveListener subscribe(NavigationListener listener) {
    return _beagleJSEngine.onNavigate(_viewId, listener);
  }
}
