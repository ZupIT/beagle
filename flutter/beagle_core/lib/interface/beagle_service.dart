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

import 'package:beagle_core/interface/action_handler.dart';
import 'package:beagle_core/interface/beagle_view.dart';
import 'package:beagle_core/interface/component_builder.dart';
import 'package:beagle_core/interface/http_client.dart';
import 'package:beagle_core/interface/storage.dart';
import 'package:beagle_core/model/network_options.dart';
import 'package:beagle_core/model/network_strategy.dart';

abstract class BeagleService {
  String baseUrl;
  HttpClient httpClient;
  ComponentBuilder componentBuilder;
  Storage storage;
  bool useBeagleHeaders;
  ActionHandler actionHandler;
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
      {String route,
      NetworkOptions networkOptions,
      String initialControllerId});
}
