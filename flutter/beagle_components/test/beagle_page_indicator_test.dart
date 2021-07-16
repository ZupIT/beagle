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
import 'package:beagle_components/src/beagle_page_indicator.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  const pageIndicatorKey = Key('BeaglePageIndicator');
  const selectedColor = '#000000';
  const unselectedColor = '#888888';

  Widget createWidget({
    Key key = pageIndicatorKey,
    String selectedColor = selectedColor,
    String unselectedColor = unselectedColor,
    int numberOfPages,
    int currentPage,
  }) {
    return MaterialApp(
      home: BeaglePageIndicator(
        key: key,
        selectedColor: selectedColor,
        unselectedColor: unselectedColor,
        numberOfPages: numberOfPages,
        currentPage: currentPage,
      ),
    );
  }

  group('Given a BeaglePageIndicator with two pages', () {
    group('When it is rendered', () {
      final pageIndicator = createWidget(numberOfPages: 2, currentPage: 0);
      testWidgets('Then it should have two material widgets',
          (WidgetTester tester) async {
        await tester.pumpWidget(pageIndicator);

        final dotFinder = find.byType(Material);
        expect(dotFinder, findsNWidgets(2));
      });
    });
    group('When currentPage is 0', () {
      final pageIndicator = createWidget(numberOfPages: 2, currentPage: 0);
      testWidgets(
          'Then it should paint first dot with selectedColor and second dot with unselectedColor ',
          (WidgetTester tester) async {
        await tester.pumpWidget(pageIndicator);

        final expectedFirstDotColor = HexColor(selectedColor);
        final expectedSecondDotColor = HexColor(unselectedColor);

        final widgets = tester.widgetList<Material>(find.byType(Material));

        expect(expectedFirstDotColor, widgets.elementAt(0).color);
        expect(expectedSecondDotColor, widgets.elementAt(1).color);
      });
    });
    group('When currentPage is 1', () {
      final pageIndicator = createWidget(numberOfPages: 2, currentPage: 1);
      testWidgets(
          'Then it should paint second dot with selectedColor and first dot with unselectedColor ',
          (WidgetTester tester) async {
        await tester.pumpWidget(pageIndicator);

        final expectedFirstDotColor = HexColor(unselectedColor);
        final expectedSecondDotColor = HexColor(selectedColor);

        final widgets = tester.widgetList<Material>(find.byType(Material));

        expect(expectedFirstDotColor, widgets.elementAt(0).color);
        expect(expectedSecondDotColor, widgets.elementAt(1).color);
      });
    });
  });
}
