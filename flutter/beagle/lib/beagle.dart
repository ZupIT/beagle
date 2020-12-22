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

library beagle;

import 'package:beagle_core/beagle_core.dart' as core;
import 'package:beagle_core/interface/action_handler.dart';
import 'package:beagle_core/interface/component_builder.dart';
import 'package:beagle_core/interface/http_client.dart';
import 'package:beagle_core/interface/storage.dart';
import 'package:beagle_core/model/network_strategy.dart';

export 'package:beagle_core/beagle_remote_view.dart';

class BeagleInitializer {
  static Future<void> start({
    String baseUrl,
    HttpClient httpClient,
    ComponentBuilder componentBuilder,
    Storage storage,
    bool useBeagleHeaders,
    ActionHandler actionHandler,
    NetworkStrategy strategy,
  }) async {
    return core.BeagleInitializer.start(
      baseUrl: baseUrl,
      httpClient: httpClient,
      componentBuilder: componentBuilder,
      storage: storage,
      useBeagleHeaders: useBeagleHeaders ?? true,
      actionHandler: actionHandler,
      strategy: strategy,
    );
  }
}
