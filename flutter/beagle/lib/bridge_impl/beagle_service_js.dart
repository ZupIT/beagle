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

import 'package:beagle/beagle.dart';
import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/interface/beagle_service.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/interface/navigation_controller.dart';
import 'package:beagle/networking/beagle_network_strategy.dart';
import 'package:beagle/networking/beagle_request.dart';
import 'package:beagle/utils/network_strategy.dart';

class BeagleServiceJS implements BeagleService {
  BeagleServiceJS(
    this._beagleJSEngine, {
    this.baseUrl,
    this.httpClient,
    this.components,
    this.useBeagleHeaders,
    this.actions,
    this.strategy,
    this.navigationControllers,
  });

  @override
  String baseUrl;
  @override
  HttpClient httpClient;
  @override
  Map<String, ComponentBuilder> components;
  @override
  bool useBeagleHeaders;
  @override
  Map<String, ActionHandler> actions;
  @override
  BeagleNetworkStrategy strategy;
  @override
  Map<String, NavigationController> navigationControllers;

  final BeagleJSEngine _beagleJSEngine;

  Map<String, dynamic> _getNavigationControllersAsMap() {
    if (navigationControllers == null) {
      return null;
    }
    final result = <String, dynamic>{};
    for (final key in navigationControllers.keys) {
      final controller = navigationControllers[key];
      result[key] = controller.toMap();
    }
    return result;
  }

  @override
  Future<void> start() async {
    await _beagleJSEngine.start();

    _registerBeagleService();
    _registerHttpListener();
    _registerActionListener();
  }

  void _registerBeagleService() {
    final params = {
      'baseUrl': baseUrl,
      'actionKeys': actions.keys.toList(),
      'useBeagleHeaders': useBeagleHeaders,
      'strategy': NetworkStrategyUtils.getJsStrategyName(strategy),
    };
    final navigationControllers = _getNavigationControllersAsMap();
    if (navigationControllers != null) {
      params['navigationControllers'] = navigationControllers;
    }
    _beagleJSEngine
        .evaluateJavascriptCode('global.beagle.start(${json.encode(params)})');
  }

  void _registerHttpListener() {
    _beagleJSEngine.onHttpRequest((String id, BeagleRequest request) async {
      final response = await httpClient.sendRequest(request);
      _beagleJSEngine.respondHttpRequest(id, response);
    });
  }

  void _registerActionListener() {
    _beagleJSEngine.onAction(({action, view, element}) {
      final handler = actions[action.getType()];
      if (handler == null) {
        return;
      }
      handler(
        action: action,
        view: view,
        element: element,
        context: view.getContext(),
      );
    });
  }
}
