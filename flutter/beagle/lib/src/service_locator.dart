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
import 'package:flutter_js/flutter_js.dart';
import 'package:get_it/get_it.dart';

import 'bridge_impl/beagle_js_engine.dart';
import 'bridge_impl/beagle_service_js.dart';
import 'bridge_impl/beagle_view_js.dart';
import 'bridge_impl/global_context_js.dart';
import 'bridge_impl/js_runtime_wrapper.dart';

final GetIt beagleServiceLocator = GetIt.instance;

void setupServiceLocator({
  String baseUrl,
  BeagleEnvironment environment,
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
  Map<String, Operation> operations,
}) {
  beagleServiceLocator
    ..registerSingleton<BeagleYogaFactory>(BeagleYogaFactory())
    ..registerSingleton<JavascriptRuntimeWrapper>(
      createJavascriptRuntimeWrapperInstance(),
    )
    ..registerSingleton<BeagleJSEngine>(
      createBeagleJSEngineInstance(storage),
    )
    ..registerSingleton<GlobalContext>(GlobalContextJS(beagleServiceLocator<BeagleJSEngine>()),)
    ..registerSingleton<BeagleDesignSystem>(designSystem)
    ..registerSingleton<BeagleImageDownloader>(imageDownloader)
    ..registerSingleton<BeagleLogger>(logger)
    ..registerSingleton<BeagleEnvironment>(environment)
    ..registerSingletonAsync<BeagleService>(() async {
      final configService = BeagleServiceJS(
        beagleServiceLocator<BeagleJSEngine>(),
        baseUrl: baseUrl,
        httpClient: httpClient,
        components: components,
        useBeagleHeaders: useBeagleHeaders,
        actions: actions,
        strategy: strategy,
        navigationControllers: navigationControllers,
        operations: operations,
      );

      await configService.start();
      return configService;
    })
    ..registerFactoryParam<BeagleViewJS, BeagleNetworkOptions, String>(
      (networkOptions, initialControllerId) => BeagleViewJS(
        beagleServiceLocator<BeagleJSEngine>(),
        networkOptions: networkOptions,
        initialControllerId: initialControllerId,
      ),
    )
    ..registerFactory<UrlBuilder>(() => UrlBuilder(baseUrl));
}

JavascriptRuntimeWrapper createJavascriptRuntimeWrapperInstance() =>
    JavascriptRuntimeWrapper(
        getJavascriptRuntime(forceJavascriptCoreOnAndroid: true, xhr: false));

BeagleJSEngine createBeagleJSEngineInstance(Storage storage) =>
    BeagleJSEngine(beagleServiceLocator<JavascriptRuntimeWrapper>(), storage);
