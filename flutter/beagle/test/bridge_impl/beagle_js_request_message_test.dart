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

import 'package:beagle/bridge_impl/beagle_js_request_message.dart';
import 'package:beagle/networking/beagle_http_method.dart';
import 'package:beagle/utils/enum.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('Given a BeagleJSRequestMessage', () {
    group('When toRequest is called', () {
      test('Then should return a BeagleRequest object', () {
        const expectedId = 'requestId';
        const expectedUrl = 'https://usebeagle.io';
        const expectedMethod = BeagleHttpMethod.get;
        final expectedHeaders = {'scheme': 'https'};
        const expectedBody = '{"_beagleComponent_": "beagle:button"}';
        final json = {
          'id': expectedId,
          'url': expectedUrl,
          'method': EnumUtils.getEnumValueName(expectedMethod),
          'headers': expectedHeaders,
          'body': expectedBody
        };

        final jsRequestMessage = BeagleJSRequestMessage.fromJson(json);

        final result = jsRequestMessage.toRequest();

        expect(jsRequestMessage.requestId, expectedId);
        expect(result.url, expectedUrl);
        expect(result.method, expectedMethod);
        expect(result.headers, expectedHeaders);
        expect(result.body, expectedBody);
      });
    });
  });
}
