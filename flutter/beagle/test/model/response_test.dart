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

import 'dart:typed_data';

import 'package:beagle/model/response.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('Given a Response', () {
    group('When toJson is called', () {
      test(
          'Then should return a String representing the Response in json format',
          () {
        final response = Response(
          200,
          '',
          {'content-type': 'text/javascript; charset=UTF-8'},
          Uint8List(0),
        );

        const expected =
            '{"status":200,"body":"","headers":{"content-type":"text/javascript; charset=UTF-8"}}';

        final result = response.toJson();

        expect(result, expected);
      });
    });
  });
}
