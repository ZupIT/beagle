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

import 'package:beagle/bridge_impl/beagle_service_js.dart';
import 'package:beagle/interface/action_handler.dart';
import 'package:beagle/interface/beagle_service.dart';
import 'package:beagle/interface/component_builder.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/interface/storage.dart';
import 'package:beagle/model/network_strategy.dart';

// ignore: avoid_classes_with_only_static_members
class BeagleInitializer {
  static BeagleService service;

  static Future<void> start(
      {String baseUrl,
      HttpClient httpClient,
      ComponentBuilder componentBuilder,
      Storage storage,
      bool useBeagleHeaders,
      ActionHandler actionHandler,
      NetworkStrategy strategy}) async {
    service = BeagleServiceJS(
      baseUrl: baseUrl,
      httpClient: httpClient,
      componentBuilder: componentBuilder,
      storage: storage,
      useBeagleHeaders: useBeagleHeaders ?? true,
      actionHandler: actionHandler,
      strategy: strategy,
    );

    await service.start();
  }
}
