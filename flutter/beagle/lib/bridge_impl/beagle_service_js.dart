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
import 'package:beagle/default/default_action_handler.dart';
import 'package:beagle/default/default_http_client.dart';
import 'package:beagle/interface/action_handler.dart';
import 'package:beagle/interface/beagle_service.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/interface/component_builder.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/interface/storage.dart';
import 'package:beagle/model/network_options.dart';
import 'package:beagle/model/network_strategy.dart';
import 'package:beagle/model/request.dart';

class BeagleServiceJS implements BeagleService {
  BeagleServiceJS({
    this.baseUrl,
    this.httpClient,
    this.componentBuilder,
    this.storage,
    this.useBeagleHeaders,
    this.actionHandler,
    this.strategy,
  });

  @override
  String baseUrl;
  @override
  HttpClient httpClient;
  @override
  ComponentBuilder componentBuilder;
  @override
  Storage storage;
  @override
  bool useBeagleHeaders;
  @override
  ActionHandler actionHandler;
  @override
  NetworkStrategy strategy;

  @override
  Future<void> start() async {
    httpClient ??= DefaultHttpClient();
    actionHandler ??= DefaultActionHandler();

    await BeagleJSEngine.start();

    BeagleJSEngine.createBeagleService(baseUrl, actionHandler.getActionKeys());

    BeagleJSEngine.onHttpRequest((String id, Request request) async {
      final response = await httpClient.sendRequest(request);
      BeagleJSEngine.respondHttpRequest(id, response);
    });

    BeagleJSEngine.onAction(actionHandler.handleAction);
  }

  @override
  BeagleView createView(
      {String route,
      NetworkOptions networkOptions,
      String initialControllerId}) {
    return BeagleViewJS(route: route);
  }
}
