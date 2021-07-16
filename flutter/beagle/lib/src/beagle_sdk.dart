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

import 'package:beagle/beagle.dart';
import 'package:beagle/src/service_locator.dart';
import 'package:yoga_engine/yoga_engine.dart';

class BeagleSdk {
  /// Starts the BeagleService. Only a single instance of this service is allowed.
  /// The parameters are all the attributes of the class BeagleService. Please check its
  /// documentation for more details.
  static void init({
    /// Attribute responsible for informing Beagle about the current build status of the application.
    BeagleEnvironment environment,

    /// Informs the base URL used in Beagle in the application.
    String baseUrl,
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

    Map<String, Operation> operations,
  }) {
    Yoga.init();

    baseUrl = baseUrl ?? "";
    httpClient = httpClient ?? const DefaultHttpClient();
    environment = environment ?? BeagleEnvironment.debug;
    useBeagleHeaders = useBeagleHeaders ?? true;
    storage = storage ?? DefaultStorage();
    designSystem = designSystem ?? DefaultEmptyDesignSystem();
    imageDownloader = imageDownloader ?? DefaultBeagleImageDownloader(httpClient: httpClient);
    strategy = strategy ?? BeagleNetworkStrategy.beagleWithFallbackToCache;
    logger = logger ?? DefaultEmptyLogger();
    operations = operations ?? {};

    actions = actions == null ? defaultActions : { ...defaultActions, ...actions };

    Map<String, ComponentBuilder> lowercaseComponents = components.map(
      (key, value) => MapEntry(key.toLowerCase(), value)
    );

    Map<String, ActionHandler> lowercaseActions = actions.map(
      (key, value) => MapEntry(key.toLowerCase(), value)
    );

    setupServiceLocator(
      baseUrl: baseUrl,
      httpClient: httpClient,
      environment: environment,
      components: lowercaseComponents,
      storage: storage,
      useBeagleHeaders: useBeagleHeaders,
      actions: lowercaseActions,
      navigationControllers: navigationControllers,
      designSystem: designSystem,
      imageDownloader: imageDownloader ,
      strategy: strategy,
      logger: logger,
      operations: operations,
    );
  }
}
