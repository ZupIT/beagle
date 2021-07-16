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

import 'package:beagle_components/beagle_components.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:webview_flutter/webview_flutter.dart';

void main() {
  const webViewKey = Key('BeagleWebView');
  const defaultUrl = 'https://usebeagle.io/';

  Widget createWidget({
    Key webViewKey = webViewKey,
    String url = defaultUrl,
  }) {
    return MaterialApp(
      home: BeagleWebView(
        key: webViewKey,
        url: url,
      ),
    );
  }

  group('Given a BeagleWebView', () {
    group('When the component is rendered', () {
      testWidgets('Then it should have a WebView child widget',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget());
        final webViewFinder = find.byType(WebView);
        expect(webViewFinder, findsOneWidget);
      });
    });

    group('When I pass an URL', () {
      testWidgets('Then it should load the correct initial URL',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget());

        expect(tester.widget<WebView>(find.byType(WebView)).initialUrl,
            defaultUrl);
      });
    });
  });
}
