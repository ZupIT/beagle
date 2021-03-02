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

import 'package:beagle/networking/beagle_http_method.dart';
import 'package:beagle/networking/beagle_network_options.dart';
import 'package:beagle/networking/beagle_network_strategy.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('Given a BeagleNetworkOptions', () {
    final networkOptions = BeagleNetworkOptions(
      method: BeagleHttpMethod.get,
      headers: {'content-type': 'text/javascript; charset=UTF-8'},
      strategy: BeagleNetworkStrategy.beagleCacheOnly,
    );

    group('When toJsonEncode is called by passing null as parameter', () {
      test('Then should return an empty json', () {
        const expected = '{}';

        final result = BeagleNetworkOptions.toJsonEncode(null);

        expect(result, expected);
      });
    });

    group('When toJsonEncode is called', () {
      test('Then should return a valid json with BeagleNetworkOptions values',
          () {
        const expected =
            '{"method":"get","headers":{"content-type":"text/javascript; charset=UTF-8"},"strategy":"beagle-cache-only"}';

        final result = BeagleNetworkOptions.toJsonEncode(networkOptions);

        expect(result, expected);
      });
    });
  });
}
