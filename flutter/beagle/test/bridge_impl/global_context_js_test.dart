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

import 'package:beagle/src/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/src/bridge_impl/global_context_js.dart';
import 'package:flutter_js/flutter_js.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

class MockBeagleJSEngine extends Mock implements BeagleJSEngine {}

class User {
  User(this.name, this.age);

  String name;
  int age;
}

void main() {
  final beagleJSEngineMock = MockBeagleJSEngine();

  group('Given a GlobalContextJS object', () {
    group('When I call set method passing an encodable value', () {
      test('Then it should set value in global context', () {
        final value = {
          'abc': 1,
          'def': false,
          'ghi': 'test',
          'jkl': [1, '2', true]
        };
        GlobalContextJS(beagleJSEngineMock).set(value);
        expect(
            verify(beagleJSEngineMock.evaluateJavascriptCode(captureAny))
                .captured
                .single,
            'global.beagle.getService().globalContext.set(${jsonEncode(value)})');
      });
    });

    group('When I call set method passing a value to a specific path', () {
      test('Then it should set value in global context at specific path', () {
        GlobalContextJS(beagleJSEngineMock).set('test', 'order.cart.name');
        expect(
            verify(beagleJSEngineMock.evaluateJavascriptCode(captureAny))
                .captured
                .single,
            "global.beagle.getService().globalContext.set(\"test\", 'order.cart.name')");
      });
    });

    group('When I call set method passing a uncodable value', () {
      test('Then it should throw GlobalContextSerializationError exception',
          () {
        final user = User('Fulano', 30);
        expect(() => GlobalContextJS(beagleJSEngineMock).set(user, 'user'),
            throwsA(isInstanceOf<GlobalContextSerializationError>()));
        verifyNever(beagleJSEngineMock.evaluateJavascriptCode(captureAny));
      });
    });

    group('When I call get method', () {
      test('Then it should get global context value', () {
        final value = {
          'account': {
            'number': 1,
            'name': 'Fulano',
            'email': 'fulano@beagle.com'
          },
          'order': {
            'cart': {
              'name': 'Flutter test',
              'items': [
                {'name': 'keyboard', 'price': 39.9},
                {'name': 'mouse', 'price': 28.45}
              ]
            }
          }
        };
        when(beagleJSEngineMock.evaluateJavascriptCode(
                'global.beagle.getService().globalContext.get()'))
            .thenReturn(JsEvalResult(value.toString(), value));
        final result = GlobalContextJS(beagleJSEngineMock).get();
        expect(result, value);
      });
    });

    group('When I call get method for a specific path', () {
      test('Then it should get value in global context at specific path', () {
        const value = 'Flutter test';
        when(beagleJSEngineMock.evaluateJavascriptCode(
                "global.beagle.getService().globalContext.get('order.cart.name')"))
            .thenReturn(JsEvalResult(value, value));
        final result =
            GlobalContextJS(beagleJSEngineMock).get('order.cart.name');
        expect(result, value);
      });
    });

    group('When I call clear method', () {
      test('Then it should clear global context', () {
        clearInteractions(beagleJSEngineMock);
        GlobalContextJS(beagleJSEngineMock).clear();
        expect(
            verify(beagleJSEngineMock.evaluateJavascriptCode(captureAny))
                .captured
                .single,
            'global.beagle.getService().globalContext.clear()');
      });
    });

    group('When I call clear method for a specific path', () {
      test('Then it should clear global context at specific path', () {
        GlobalContextJS(beagleJSEngineMock).clear('order.cart.name');
        expect(
            verify(beagleJSEngineMock.evaluateJavascriptCode(captureAny))
                .captured
                .single,
            "global.beagle.getService().globalContext.clear('order.cart.name')");
      });
    });
  });
}
