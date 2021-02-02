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

import 'dart:async' show Future, Stream;
import 'dart:io'
    show
        HttpClient,
        HttpClientRequest,
        HttpClientResponse,
        HttpHeaders,
        HttpOverrides,
        HttpStatus;
import 'dart:io';

import 'package:mockito/mockito.dart'
    show Mock, any, anyNamed, captureAny, throwOnMissingStub, typed, when;

/// Runs [body] in separate [Zone] with [MockHttpClient].
R mockNetworkImagesFor<R>(R Function() body, Map<Uri, List<int>> data) {
  return HttpOverrides.runZoned(
    body,
    createHttpClient: (_) => createMockImageHttpClient(data),
  );
}

MockHttpClient createMockImageHttpClient(Map<Uri, List<int>> data) {
  final client = MockHttpClient();
  final request = MockHttpClientRequest();
  final response = MockHttpClientResponse(data);
  final headers = MockHttpHeaders();

  throwOnMissingStub(client);
  throwOnMissingStub(request);
  throwOnMissingStub(response);
  throwOnMissingStub(headers);

  when(client.autoUncompress = captureAny).thenAnswer((realInvocation) => null);

  when(client.getUrl(captureAny)).thenAnswer((invocation) {
    response.requestedUrl = invocation.positionalArguments[0];
    return Future<HttpClientRequest>.value(request);
  });

  when(request.headers).thenAnswer((_) => headers);

  when(request.close())
      .thenAnswer((_) => Future<HttpClientResponse>.value(response));

  when(response.compressionState)
      .thenReturn(HttpClientResponseCompressionState.notCompressed);

  when(response.contentLength)
      .thenAnswer((_) => data[response.requestedUrl].length);

  when(response.statusCode).thenReturn(HttpStatus.ok);

  when(
    response.listen(
      any,
      cancelOnError: anyNamed('cancelOnError'),
      onDone: anyNamed('onDone'),
      onError: anyNamed('onError'),
    ),
  ).thenAnswer((invocation) {
    final onData =
        invocation.positionalArguments[0] as void Function(List<int>);

    final onDone = invocation.namedArguments[#onDone] as void Function();

    final onError = invocation.namedArguments[#onError] as void Function(Object,
        [StackTrace]);

    final cancelOnError = invocation.namedArguments[#cancelOnError];

    return Stream<List<int>>.fromIterable([data[response.requestedUrl]]).listen(
        onData,
        onDone: onDone,
        onError: onError,
        cancelOnError: cancelOnError);
  });
  return client;
}

class MockHttpClient extends Mock implements HttpClient {}

class MockHttpClientRequest extends Mock implements HttpClientRequest {}

class MockHttpClientResponse extends Mock implements HttpClientResponse {
  MockHttpClientResponse(this.data);

  final Map<Uri, List<int>> data;
  Uri requestedUrl;
}

class MockHttpHeaders extends Mock implements HttpHeaders {}
