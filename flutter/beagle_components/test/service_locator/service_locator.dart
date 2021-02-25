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

import 'package:beagle/default/default_actions.dart';
import 'package:beagle/default/default_http_client.dart';
import 'package:beagle/default/default_storage.dart';
import 'package:beagle/default/empty/default_empty_config.dart';
import 'package:beagle/default/empty/default_empty_logger.dart';
import 'package:beagle/interface/beagle_image_downloader.dart';
import 'package:beagle/interface/beagle_service.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/interface/navigation_controller.dart';
import 'package:beagle/interface/storage.dart';
import 'package:beagle/logger/beagle_logger.dart';
import 'package:beagle/model/beagle_config.dart';
import 'package:beagle/networking/beagle_network_strategy.dart';
import 'package:beagle/service_locator.dart';
import 'package:beagle/setup/beagle_design_system.dart';

import '../objects_fake/fake_design_system.dart';

Future<void> testSetupServiceLocator({
  BeagleConfig beagleConfig,
  HttpClient httpClient,
  Map<String, ComponentBuilder> components,
  Storage storage,
  bool useBeagleHeaders,
  Map<String, ActionHandler> actions,
  BeagleNetworkStrategy strategy,
  Map<String, NavigationController> navigationControllers,
  BeagleDesignSystem designSystem,
  BeagleImageDownloader imageDownloader,
  BeagleLogger logger,
}) async {
  await beagleServiceLocator.reset();

  beagleServiceLocator
    ..registerSingleton<BeagleDesignSystem>(designSystem ?? FakeDesignSystem())
    ..registerSingleton<BeagleImageDownloader>(imageDownloader);
}
