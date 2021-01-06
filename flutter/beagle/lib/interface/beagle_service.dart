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

import 'package:beagle/beagle.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/interface/storage.dart';
import 'package:beagle/model/beagle_action.dart';
import 'package:beagle/model/network_options.dart';
import 'package:beagle/model/network_strategy.dart';
import 'package:flutter/widgets.dart';

typedef ComponentBuilder = Widget Function(
    BeagleUIElement element, List<Widget> children);
typedef ActionHandler = void Function(
    {BeagleAction action, BeagleView view, BeagleUIElement element});

abstract class BeagleService {
  String baseUrl;
  HttpClient httpClient;
  Map<String, ComponentBuilder> components;
  Storage storage;
  bool useBeagleHeaders;
  Map<String, ActionHandler> actions;
  NetworkStrategy strategy;

  // todo:
  /*Analytics analytics;
  LifecycleHandler lifecycles,
  Map<String, NavigationController> navigationControllers,
  AnalyticsProvider analyticsProvider,
  Map<String, Operation> customOperations,*/

  // todo: add support for the GlobalContext
  // todo: add support for the ViewContentManager

  Future<void> start();

  BeagleView createView(
      {NetworkOptions networkOptions, String initialControllerId});
}
