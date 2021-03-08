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
import 'dart:typed_data';

import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/bridge_impl/js_runtime_wrapper.dart';
import 'package:beagle/interface/storage.dart';
import 'package:beagle/model/response.dart';
import 'package:beagle/networking/beagle_http_method.dart';
import 'package:flutter_js/flutter_js.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

class MockJavascriptRuntimeWrapper extends Mock
    implements JavascriptRuntimeWrapper {}

class MockStorage extends Mock implements Storage {}

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();
  final jsRuntimeMock = MockJavascriptRuntimeWrapper();
  final storageMock = MockStorage();

  setUp(() {
    reset(jsRuntimeMock);
  });

  group('Given a not started BeagleJSEngine', () {
    group('When evaluateJavascriptCode is called', () {
      test('Then should throw BeagleJSEngineException', () {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        expect(() => beagleJSEngine.evaluateJavascriptCode('code'),
            throwsA(isInstanceOf<BeagleJSEngineException>()));

        verifyNever(jsRuntimeMock.evaluate(captureAny));
      });
    });

    group('When promiseToFuture is called', () {
      test('Then should throw BeagleJSEngineException', () {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        expect(
            () => beagleJSEngine
                .promiseToFuture(JsEvalResult('stringResult', 'rawResult')),
            throwsA(isInstanceOf<BeagleJSEngineException>()));

        verifyNever(jsRuntimeMock.evaluate(captureAny));
      });
    });

    group('When start was NOT called yet', () {
      test('Then engine state should be BeagleJSEngineState.CREATED', () {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        expect(beagleJSEngine.state, BeagleJSEngineState.CREATED);
      });
    });

    group('When start is called', () {
      test('Then should change engine state to BeagleJSEngineState.STARTED',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        expect(beagleJSEngine.state, BeagleJSEngineState.STARTED);
      });

      test('Then should initialize the JavascriptRuntime', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        verifyInOrder([
          jsRuntimeMock.enableHandlePromises(),
          jsRuntimeMock.evaluate('var window = global = globalThis;'),
          jsRuntimeMock.evaluateAsync(any)
        ]);
      });

      test('Then should register for javascript httpClient.request messages',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        const expectedChannelName = 'httpClient.request';
        verify(jsRuntimeMock.onMessage(expectedChannelName, any));
      });

      test('Then should register for javascript action messages', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        const expectedChannelName = 'action';
        verify(jsRuntimeMock.onMessage(expectedChannelName, any));
      });

      test('Then should register for javascript operation messages', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        const expectedChannelName = 'operation';
        verify(jsRuntimeMock.onMessage(expectedChannelName, any));
      });

      test('Then should register for javascript beagleView.update messages',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        const expectedChannelName = 'beagleView.update';
        verify(jsRuntimeMock.onMessage(expectedChannelName, any));
      });

      test('Then should register for javascript beagleNavigator messages',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        const expectedChannelName = 'beagleNavigator';
        verify(jsRuntimeMock.onMessage(expectedChannelName, any));
      });

      test('Then should register for javascript storage.set messages',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        const expectedChannelName = 'storage.set';
        verify(jsRuntimeMock.onMessage(expectedChannelName, any));
      });

      test('Then should register for javascript storage.get messages',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        const expectedChannelName = 'storage.get';
        verify(jsRuntimeMock.onMessage(expectedChannelName, any));
      });

      test('Then should register for javascript storage.remove messages',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        const expectedChannelName = 'storage.remove';
        verify(jsRuntimeMock.onMessage(expectedChannelName, any));
      });

      test('Then should register for javascript storage.clear messages',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        const expectedChannelName = 'storage.clear';
        verify(jsRuntimeMock.onMessage(expectedChannelName, any));
      });
    });
  });

  group('Given a started BeagleJSEngine', () {
    group('When evaluateJavascriptCode is called', () {
      test('Then should NOT throw BeagleJSEngineException', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        beagleJSEngine.evaluateJavascriptCode('code');
      });

      test('Then should execute code in javascriptRuntime', () async {
        const jsCode = 'code';
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        beagleJSEngine.evaluateJavascriptCode(jsCode);

        verify(jsRuntimeMock.evaluate(jsCode)).called(1);
      });
    });

    group('When promiseToFuture is called', () {
      test('Then should call handlePromise in javascriptRuntime', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();
        final result = JsEvalResult('stringResult', 'rawResult');

        await beagleJSEngine.promiseToFuture(result);

        verify(jsRuntimeMock.handlePromise(result)).called(1);
      });
    });

    group('When start is called more than one time', () {
      test('Then should initialize the JavascriptRuntime only one time',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();
        reset(jsRuntimeMock);
        await beagleJSEngine.start();

        verifyZeroInteractions(jsRuntimeMock);
      });
    });

    group('When an httpClient.request message is received', () {
      test('Then should call registered http listener', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        final httpMessage = {
          'id': '1',
          'url': 'https://usebeagle.io/',
          'method': 'get',
          'headers': <String, String>{},
          'body': ''
        };

        var httpListenerCalled = false;

        beagleJSEngine.onHttpRequest((requestId, request) {
          httpListenerCalled = true;
          expect(requestId, '1');
          expect(request.method, BeagleHttpMethod.get);
        });

        verify(jsRuntimeMock.onMessage('httpClient.request', captureAny))
            .captured
            .single(httpMessage);

        expect(httpListenerCalled, true);
      });
    });

    group('When an action message is received', () {
      test('Then should call all registered action listeners', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        const viewId = '1';
        final actionMessage = {
          '_beagleAction_': 'beagle:setContext',
          'contextId': 'address',
          'path': 'complement',
          'value': '@{onChange.value}',
        };

        var firstActionListenerCalled = false;
        var secondActionListenerCalled = false;
        var nonRegisteredActionListenerCalled = false;

        beagleJSEngine
          ..onAction(viewId, ({action, element, view}) {
            firstActionListenerCalled = true;
            expect(action.getType(), 'beagle:setContext');
            expect(action.getAttributeValue('contextId'), 'address');
            expect(action.getAttributeValue('path'), 'complement');
            expect(action.getAttributeValue('value'), '@{onChange.value}');
          })
          ..onAction(viewId, ({action, element, view}) {
            secondActionListenerCalled = true;
          })
          ..onAction('2', ({action, element, view}) {
            nonRegisteredActionListenerCalled = true;
          });

        verify(jsRuntimeMock.onMessage('action', captureAny))
            .captured
            .single({'viewId': viewId, 'action': actionMessage});

        expect(firstActionListenerCalled, true);
        expect(secondActionListenerCalled, true);
        expect(nonRegisteredActionListenerCalled, false);
      });
    });

    group('When an operation message is received', () {
      test('Then should call registered operation listener', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        final operationMessage = {
          'operation': 'mockOperation',
          'params': ['paramA', 'paramB'],
        };

        var operationListener = false;

        beagleJSEngine.onOperation((operation, params) {
          operationListener = true;
          expect(operation, 'mockOperation');
          expect(params, ['paramA', 'paramB']);
        });

        verify(jsRuntimeMock.onMessage('operation', captureAny))
            .captured
            .single(operationMessage);

        expect(operationListener, true);
      });
    });

    group('When a beagleView.update message is received', () {
      test('Then should call all registered view update listeners', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        final viewUpdateMessage = {
          'id': '1',
          'tree': {
            '_beagleComponent_': 'beagle:container',
            'id': '_beagle_7',
          },
        };

        var firstViewUpdateListenerCalled = false;
        var secondViewUpdateListenerCalled = false;
        var nonRegisteredViewUpdateListenerCalled = false;

        beagleJSEngine
          ..onViewUpdate('1', (tree) {
            firstViewUpdateListenerCalled = true;
          })
          ..onViewUpdate('1', (tree) {
            secondViewUpdateListenerCalled = true;
          })
          ..onViewUpdate('2', (tree) {
            nonRegisteredViewUpdateListenerCalled = true;
          });

        verify(jsRuntimeMock.onMessage('beagleView.update', captureAny))
            .captured
            .single(viewUpdateMessage);

        expect(firstViewUpdateListenerCalled, true);
        expect(secondViewUpdateListenerCalled, true);
        expect(nonRegisteredViewUpdateListenerCalled, false);
      });
    });

    group('When a beagleNavigator message is received', () {
      test('Then should call all registered navigation listeners', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        final navigationMessage = {
          'viewId': '1',
          'route': {
            'screen': {
              '_beagleComponent_': 'beagle:screenComponent',
            }
          }
        };

        var firstNavigationListenerCalled = false;
        var secondNavigationListenerCalled = false;
        var nonRegisteredViewNavigationListenerCalled = false;

        beagleJSEngine
          ..onNavigate('1', (route) {
            firstNavigationListenerCalled = true;
          })
          ..onNavigate('1', (route) {
            secondNavigationListenerCalled = true;
          })
          ..onNavigate('2', (route) {
            nonRegisteredViewNavigationListenerCalled = true;
          });

        verify(jsRuntimeMock.onMessage('beagleNavigator', captureAny))
            .captured
            .single(navigationMessage);

        expect(firstNavigationListenerCalled, true);
        expect(secondNavigationListenerCalled, true);
        expect(nonRegisteredViewNavigationListenerCalled, false);
      });
    });

    group('When a storage.set message is received', () {
      test('Then should call setItem on storage', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        const key = 'key';
        const value = 'value';
        const promiseId = 'promiseId';

        final storageMessage = {
          'key': key,
          'value': value,
          'promiseId': promiseId,
        };

        await verify(jsRuntimeMock.onMessage('storage.set', captureAny))
            .captured
            .single(storageMessage);

        verify(storageMock.setItem(key, value));

        verify(jsRuntimeMock
            .evaluate("global.beagle.promise.resolve('$promiseId')"));
      });
    });

    group('When a storage.get message is received', () {
      test('Then should call getItem on storage', () async {
        const key = 'key';
        const promiseId = 'promiseId';
        const value = 'value';
        when(storageMock.getItem(key)).thenAnswer((_) async => value);
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        final storageMessage = {
          'key': key,
          'promiseId': promiseId,
        };

        await verify(jsRuntimeMock.onMessage('storage.get', captureAny))
            .captured
            .single(storageMessage);

        verify(storageMock.getItem(key));

        verify(jsRuntimeMock.evaluate(
            "global.beagle.promise.resolve('$promiseId', ${jsonEncode(value)})"));
      });
    });

    group('When a storage.remove message is received', () {
      test('Then should call removeItem on storage', () async {
        const key = 'key';
        const promiseId = 'promiseId';
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        final storageMessage = {
          'key': key,
          'promiseId': promiseId,
        };

        await verify(jsRuntimeMock.onMessage('storage.remove', captureAny))
            .captured
            .single(storageMessage);

        verify(storageMock.removeItem(key));

        verify(jsRuntimeMock
            .evaluate("global.beagle.promise.resolve('$promiseId')"));
      });
    });

    group('When a storage.clear message is received', () {
      test('Then should call clear on storage', () async {
        const promiseId = 'promiseId';
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();

        final storageMessage = {
          'promiseId': promiseId,
        };

        await verify(jsRuntimeMock.onMessage('storage.clear', captureAny))
            .captured
            .single(storageMessage);

        verify(storageMock.clear());

        verify(jsRuntimeMock
            .evaluate("global.beagle.promise.resolve('$promiseId')"));
      });
    });

    group('When createBeagleView is called', () {
      test('Then should return correct view id', () async {
        final result = JsEvalResult('10', 'rawResult');
        when(jsRuntimeMock.evaluate(any)).thenReturn(result);
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);

        await beagleJSEngine.start();

        expect(beagleJSEngine.createBeagleView(), result.stringResult);
      });
    });

    group('When removeViewListeners is called by passing a view id', () {
      test('Then should remove the listeners bound to view id', () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();
        const viewId = '1';

        final viewUpdateMessage = {
          'id': viewId,
          'tree': {
            '_beagleComponent_': 'beagle:container',
            'id': '_beagle_7',
          },
        };

        var firstViewUpdateListenerCalled = false;
        var secondViewUpdateListenerCalled = false;
        var viewErrorListenerCalled = false;
        var viewActionListenerCalled = false;

        beagleJSEngine
          ..onViewUpdate(viewId, (tree) {
            firstViewUpdateListenerCalled = true;
          })
          ..onViewUpdate(viewId, (tree) {
            secondViewUpdateListenerCalled = true;
          })
          ..onViewUpdateError(viewId, (errors) {
            viewErrorListenerCalled = true;
          })
          ..onAction(viewId, ({action, element, view}) {
            viewActionListenerCalled = true;
          })
          ..removeViewListeners(viewId);

        verify(jsRuntimeMock.onMessage('beagleView.update', captureAny))
            .captured
            .single(viewUpdateMessage);

        expect(firstViewUpdateListenerCalled, false);
        expect(secondViewUpdateListenerCalled, false);
        expect(viewErrorListenerCalled, false);
        expect(viewActionListenerCalled, false);
      });
    });

    group('When callJsFunction is called', () {
      test(
          'Then should call JavascriptRuntime evaluate passing correct argument',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();
        const functionId = '1';
        const argumentsMap = {'arg': 'argument'};

        beagleJSEngine.callJsFunction(functionId, argumentsMap);

        final expectedJavaScriptCode =
            "global.beagle.call('$functionId', ${json.encode(argumentsMap)})";

        verify(jsRuntimeMock.evaluate(expectedJavaScriptCode)).called(1);
      });
    });

    group('When respondHttpRequest is called', () {
      test(
          'Then should call JavascriptRuntime evaluate passing correct argument',
          () async {
        final beagleJSEngine = BeagleJSEngine(jsRuntimeMock, storageMock);
        await beagleJSEngine.start();
        const requestId = '1';
        final response = Response(200, '{}', {}, Uint8List(0));

        beagleJSEngine.respondHttpRequest(requestId, response);

        final expectedJavaScriptCode =
            'global.beagle.httpClient.respond($requestId, ${response.toJson()})';

        verify(jsRuntimeMock.evaluate(expectedJavaScriptCode)).called(1);
      });
    });
  });
}
