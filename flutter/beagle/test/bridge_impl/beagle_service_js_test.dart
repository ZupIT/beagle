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

import 'dart:convert';

import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/bridge_impl/beagle_service_js.dart';
import 'package:beagle/interface/navigation_controller.dart';
import 'package:beagle/networking/beagle_network_strategy.dart';
import 'package:beagle/utils/network_strategy.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

class MockBeagleJSEngine extends Mock implements BeagleJSEngine {}

void main() {
  final beagleJSEngineMock = MockBeagleJSEngine();
  const baseUrl = 'https://usebeagle.io';
  const useBeagleHeaders = true;
  final actions = {'beagle:alert': ({action, view, element, context}) {}};
  const strategy = BeagleNetworkStrategy.networkOnly;
  final navigationControllers = {
    'general': NavigationController(
        isDefault: true, loadingComponent: 'custom:loading'),
  };

  setUp(() {
    reset(beagleJSEngineMock);
  });

  group('Given a BeagleServiceJS', () {
    final beagleService = BeagleServiceJS(
      beagleJSEngineMock,
      baseUrl: baseUrl,
      useBeagleHeaders: useBeagleHeaders,
      actions: actions,
      strategy: strategy,
      navigationControllers: navigationControllers,
    );

    group('When start is called', () {
      test('Then should start BeagleJSEngine', () async {
        await beagleService.start();

        verify(beagleJSEngineMock.start()).called(1);
      });

      test('Then should start beagle javascript core', () async {
        await beagleService.start();

        final expectedParams = {
          'baseUrl': baseUrl,
          'actionKeys': actions.keys.toList(),
          'useBeagleHeaders': useBeagleHeaders,
          'strategy': NetworkStrategyUtils.getJsStrategyName(strategy),
          'navigationControllers': {
            'general': NavigationController(
                    isDefault: true, loadingComponent: 'custom:loading')
                .toMap()
          },
        };

        verify(beagleJSEngineMock.evaluateJavascriptCode(
                'global.beagle.start(${json.encode(expectedParams)})'))
            .called(1);
      });

      test('Then should register http request listener', () async {
        await beagleService.start();

        verify(beagleJSEngineMock.onHttpRequest(any)).called(1);
      });
    });
  });
}
