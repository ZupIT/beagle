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
import 'package:beagle/bridge_impl/beagle_service_js.dart';
import 'package:beagle/default/default_actions.dart';
import 'package:beagle/default/default_http_client.dart';
import 'package:beagle/default/default_image_downloader.dart';
import 'package:beagle/default/default_storage.dart';
import 'package:beagle/interface/beagle_image_downloader.dart';
import 'package:beagle/interface/beagle_service.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/interface/navigation_controller.dart';
import 'package:beagle/interface/storage.dart';
import 'package:beagle/logger/beagle_logger.dart';
import 'package:beagle/model/network_strategy.dart';
import 'package:beagle/service_locator.dart';
import 'package:beagle/setup/beagle_design_system.dart';

// ignore: avoid_classes_with_only_static_members
class BeagleInitializer {
  static BeagleService _service;
  static DesignSystem _designSystem;
  static BeagleImageDownloader _imageDownloader;
  static BeagleLogger _logger;

  /// Starts the BeagleService. Only a single instance of this service is allowed.
  /// The parameters are all the attributes of the class BeagleService. Please check its
  /// documentation for more details.
  static Future<void> start({
    String baseUrl,
    HttpClient httpClient,
    Map<String, ComponentBuilder> components,
    Storage storage,
    bool useBeagleHeaders,
    Map<String, ActionHandler> actions,
    NetworkStrategy strategy,
    Map<String, NavigationController> navigationControllers,
    DesignSystem designSystem,
    BeagleImageDownloader imageDownloader,
    BeagleLogger logger,
  }) async {
    setupServiceLocator();
    _designSystem = designSystem;
    _imageDownloader = imageDownloader ??
        DefaultBeagleImageDownloader(
            httpClient: httpClient ?? const DefaultHttpClient());
    _logger = logger;

    _service = BeagleServiceJS(
      serviceLocator<BeagleJSEngine>(),
      baseUrl: baseUrl,
      httpClient: httpClient ?? const DefaultHttpClient(),
      components: components,
      storage: storage ?? DefaultStorage(),
      useBeagleHeaders: useBeagleHeaders ?? true,
      actions:
          actions == null ? defaultActions : {...defaultActions, ...actions},
      strategy: strategy ?? NetworkStrategy.beagleWithFallbackToCache,
      navigationControllers: navigationControllers,
    );

    await _service.start();
  }

  static BeagleService getService() {
    return _service;
  }

  static DesignSystem get designSystem => _designSystem;

  static BeagleImageDownloader get imageDownloader => _imageDownloader;

  static BeagleLogger get logger => _logger;
}
