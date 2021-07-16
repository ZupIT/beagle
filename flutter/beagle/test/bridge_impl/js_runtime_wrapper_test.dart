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

import 'package:beagle/src/bridge_impl/js_runtime_wrapper.dart';
import 'package:flutter_js/flutter_js.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

class JavascriptRuntimeMock extends Mock implements JavascriptRuntime {}

void main() {
  group('Given a JavascriptRuntimeWrapper', () {
    final jsRuntimeMock = JavascriptRuntimeMock();
    final jsRuntimeWrapper = JavascriptRuntimeWrapper(jsRuntimeMock);

    setUp(() {
      reset(jsRuntimeMock);
    });
    group('When evaluate is called', () {
      test('Then should call JavascriptRuntime evaluate with correct parameter',
          () {
        const jsCode = 'some js code';
        jsRuntimeWrapper.evaluate(jsCode);

        verify(jsRuntimeMock.evaluate(jsCode)).called(1);
      });
    });

    group('When evaluateAsync is called', () {
      test(
          'Then should call JavascriptRuntime evaluateAsync with correct parameter',
          () async {
        const jsCode = 'some js code';
        await jsRuntimeWrapper.evaluateAsync(jsCode);

        verify(jsRuntimeMock.evaluateAsync(jsCode)).called(1);
      });
    });

    group('When onMessage is called', () {
      test(
          'Then should call JavascriptRuntime onMessage with correct parameters',
          () {
        const channelName = 'channel';
        void function(dynamic args) {}

        jsRuntimeWrapper.onMessage(channelName, function);

        verify(jsRuntimeMock.onMessage(channelName, function)).called(1);
      });
    });
  });
}
