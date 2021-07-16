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
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

final List<MethodCall> log = <MethodCall>[];
MethodChannel channel = const MethodChannel('plugins.flutter.io/url_launcher');

class BeagleLoggerMock extends Mock implements BeagleLogger {}

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('Given Beagle Open External URL Action ', () {
    group('When I call launchURL successfully', () {
      test(
          'Then it should call launch library method with given URL as argument',
          () async {
        String url;
        String method;
        const expectedUrl = 'http://example.com';

        channel.setMockMethodCallHandler((MethodCall methodCall) async {
          url = methodCall.arguments['url'];
          method = methodCall.method;
          if (method == 'canLaunch') {
            return true;
          }
        }); // Register the mock handler.

        await BeagleOpenExternalUrl.launchURL(expectedUrl);

        expect(method, equals('launch'));
        expect(url, equals(expectedUrl));

        channel.setMockMethodCallHandler(null); // Unregister the mock handler.
      });
    });
  });
}
