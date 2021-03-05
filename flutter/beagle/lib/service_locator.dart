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
import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/bridge_impl/beagle_service_js.dart';
import 'package:beagle/bridge_impl/beagle_view_js.dart';
import 'package:beagle/bridge_impl/global_context_js.dart';
import 'package:beagle/bridge_impl/js_runtime_wrapper.dart';
import 'package:beagle/default/url_builder.dart';
import 'package:beagle/interface/beagle_image_downloader.dart';
import 'package:beagle/interface/beagle_service.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/interface/navigation_controller.dart';
import 'package:beagle/interface/storage.dart';
import 'package:beagle/logger/beagle_logger.dart';
import 'package:beagle/model/beagle_config.dart';
import 'package:beagle/networking/beagle_network_options.dart';
import 'package:beagle/networking/beagle_network_strategy.dart';
import 'package:beagle/setup/beagle_design_system.dart';
import 'package:flutter_js/flutter_js.dart';
import 'package:get_it/get_it.dart';

final GetIt beagleServiceLocator = GetIt.instance;

void setupServiceLocator({
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
}) {
  beagleServiceLocator
    ..registerSingleton<JavascriptRuntimeWrapper>(
      createJavascriptRuntimeWrapperInstance(),
    )
    ..registerSingleton<BeagleJSEngine>(
      createBeagleJSEngineInstance(storage),
    )
    ..registerSingleton<BeagleConfig>(beagleConfig)
    ..registerSingleton<BeagleDesignSystem>(designSystem)
    ..registerSingleton<BeagleImageDownloader>(imageDownloader)
    ..registerSingleton<BeagleLogger>(logger)
    ..registerSingleton<BeagleService>(
      BeagleServiceJS(
        beagleServiceLocator<BeagleJSEngine>(),
        baseUrl: beagleConfig.baseUrl,
        httpClient: httpClient,
        components: components,
        useBeagleHeaders: useBeagleHeaders,
        actions: actions,
        strategy: strategy,
        navigationControllers: navigationControllers,
      ),
    )
    ..registerFactoryParam<BeagleViewJS, BeagleNetworkOptions, String>(
      (networkOptions, initialControllerId) => BeagleViewJS(
        beagleServiceLocator<BeagleJSEngine>(),
        networkOptions: networkOptions,
        initialControllerId: initialControllerId,
      ),
    )
    ..registerFactory<UrlBuilder>(() => UrlBuilder(beagleConfig.baseUrl))
    ..registerFactory(
      () => GlobalContextJS(beagleServiceLocator<BeagleJSEngine>()),
    );
}

JavascriptRuntimeWrapper createJavascriptRuntimeWrapperInstance() =>
    JavascriptRuntimeWrapper(
        getJavascriptRuntime(forceJavascriptCoreOnAndroid: true, xhr: false));

BeagleJSEngine createBeagleJSEngineInstance(Storage storage) =>
    BeagleJSEngine(beagleServiceLocator<JavascriptRuntimeWrapper>(), storage);
