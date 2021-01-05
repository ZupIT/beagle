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

import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/bridge_impl/beagle_view_js.dart';
import 'package:beagle/interface/beagle_service.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/interface/global_context.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/interface/navigation_controller.dart';
import 'package:beagle/interface/storage.dart';
import 'package:beagle/model/network_options.dart';
import 'package:beagle/model/network_strategy.dart';
import 'package:beagle/model/request.dart';
import 'package:flutter/widgets.dart';

import 'global_context_js.dart';

class BeagleServiceJS implements BeagleService {
  BeagleServiceJS({
    this.baseUrl,
    this.httpClient,
    this.components,
    this.storage,
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
  Storage storage;
  @override
  bool useBeagleHeaders;
  @override
  Map<String, ActionHandler> actions;
  @override
  NetworkStrategy strategy;
  @override
  Map<String, NavigationController> navigationControllers;
  @override
  GlobalContext globalContext;

  Map<String, dynamic> getNavigationControllersAsMap() {
    if (navigationControllers == null) {
      return null;
    }
    final result = <String, dynamic>{};
    for (final key in navigationControllers.keys) {
      final controller = navigationControllers[key];
      result[key] = {
        'errorComponent': controller.errorComponent,
        'isDefault': controller.isDefault,
        'loadingComponent': controller.loadingComponent,
        'shouldShowError': controller.shouldShowError,
        'shouldShowLoading': controller.shouldShowLoading,
      };
    }
    return result;
  }

  // transforms the enum NetworkStrategy into the string expected by beagle web (js)
  String getJsStrategyName() {
    /* When calling toString in an enum, it returns EnumName.EnumValue, we just need the part after
    the ".", which will give us the strategy name in camelCase. */
    final strategyNameInCamelCase = strategy.toString().split('.')[1];
    /* beagle web needs the strategy name in kebab-case, we use a regex to replace the uppercase
    letters with a hyphen and the lower case equivalent. */
    final strategyNameInKebabCase = strategyNameInCamelCase.replaceAllMapped(
        RegExp('[A-Z]'), (match) => '-${match[0].toLowerCase()}');
    return strategyNameInKebabCase;
  }

  @override
  Future<void> start() async {
    await BeagleJSEngine.start();

    BeagleJSEngine.createBeagleService(
        baseUrl: baseUrl,
        actionKeys: actions.keys.toList(),
        navigationControllers: getNavigationControllersAsMap(),
        useBeagleHeaders: useBeagleHeaders,
        strategy: getJsStrategyName());

    BeagleJSEngine.onHttpRequest((String id, Request request) async {
      final response = await httpClient.sendRequest(request);
      BeagleJSEngine.respondHttpRequest(id, response);
    });

    BeagleJSEngine.onAction(({action, view, element}) {
      final handler = actions[action.getType()];
      if (handler == null) {
        debugPrint("Can't find handler for action ${action.getType()}");
        return;
      }
      handler(action: action, view: view, element: element);
    });

    globalContext = GlobalContextJS();
  }

  @override
  BeagleView createView(
      {NetworkOptions networkOptions, String initialControllerId}) {
    return BeagleViewJS();
  }
}
