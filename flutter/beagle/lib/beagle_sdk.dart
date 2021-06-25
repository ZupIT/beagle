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

import 'package:beagle/default/default_actions.dart';
import 'package:beagle/default/default_http_client.dart';
import 'package:beagle/default/default_image_downloader.dart';
import 'package:beagle/default/default_storage.dart';
import 'package:beagle/default/empty/default_empty_config.dart';
import 'package:beagle/default/empty/default_empty_design_system.dart';
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
import 'package:yoga_engine/yoga_engine.dart';

class BeagleSdk {
  /// Starts the BeagleService. Only a single instance of this service is allowed.
  /// The parameters are all the attributes of the class BeagleService. Please check its
  /// documentation for more details.
  static void init({
    /// Interface that provides initial beagle configuration attributes.
    BeagleConfig beagleConfig,

    /// Interface that provides client to beagle make the requests.
    HttpClient httpClient,
    Map<String, ComponentBuilder> components,
    Storage storage,
    bool useBeagleHeaders,
    Map<String, ActionHandler> actions,
    BeagleNetworkStrategy strategy,
    Map<String, NavigationController> navigationControllers,

    /// [BeagleDesignSystem] interface that provides design system to beagle components.
    BeagleDesignSystem designSystem,

    /// [BeagleImageDownloader] interface that provides image resource from network.
    BeagleImageDownloader imageDownloader,

    /// [BeagleLogger] interface that provides logger to beagle use in application.
    BeagleLogger logger,
    Map<String, Operation> customOperations,
  }) {
    Yoga.init();
    httpClient = httpClient ?? const DefaultHttpClient();
    actions = actions == null ? defaultActions : { ...defaultActions, ...actions };

    Map<String, ComponentBuilder> lowercaseComponents = components.map(
      (key, value) => MapEntry(key.toLowerCase(), value)
    );

    Map<String, ActionHandler> lowercaseActions = actions.map(
      (key, value) => MapEntry(key.toLowerCase(), value)
    );

    setupServiceLocator(
      beagleConfig: beagleConfig ?? DefaultEmptyConfig(),
      httpClient: httpClient,
      components: lowercaseComponents,
      storage: storage ?? DefaultStorage(),
      useBeagleHeaders: useBeagleHeaders ?? true,
      actions: lowercaseActions,
      navigationControllers: navigationControllers,
      designSystem: designSystem ?? DefaultEmptyDesignSystem(),
      imageDownloader: imageDownloader ?? DefaultBeagleImageDownloader(httpClient: httpClient),
      strategy: strategy ?? BeagleNetworkStrategy.beagleWithFallbackToCache,
      logger: logger ?? DefaultEmptyLogger(),
      customOperations: customOperations,
    );
  }
}
