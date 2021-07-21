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

void main() {
  const pageViewKey = Key('BeaglePageView');
  const pageOneText = 'Page 1';
  const pageTwoText = 'Page 2';
  const pageThreeText = 'Page 3';
  const pages = [
    Text(pageOneText),
    Text(pageTwoText),
    Text(pageThreeText),
  ];

  Widget createWidget({
    Key key = pageViewKey,
    List<Widget> pages = pages,
    void Function(int) onPageChange,
    int currentPage = 0,
  }) {
    return MaterialApp(
      home: Scaffold(
        body: BeaglePageView(
          key: key,
          onPageChange: onPageChange,
          currentPage: currentPage,
          children: pages,
        ),
      ),
    );
  }

  Future<dynamic> swipePageViewRight(WidgetTester tester) async {
    await tester.drag(find.byType(PageView), const Offset(-500, 0));
    await tester.pumpAndSettle();
  }

  Future<dynamic> swipePageViewLeft(WidgetTester tester) async {
    await tester.drag(find.byType(PageView), const Offset(500, 0));
    await tester.pumpAndSettle();
  }

  group('Given a BeaglePageView', () {
    group('When the component is rendered', () {
      testWidgets('Then it should have a PageView child',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget());
        final pageViewFinder = find.byType(PageView);
        expect(pageViewFinder, findsOneWidget);
      });
    });

    group('When it is swiped', () {
      testWidgets('Then it should show pages correctly',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(onPageChange: (int page) {}));

        // check first page
        var textFinder = find.text(pageOneText);
        expect(textFinder, findsOneWidget);

        // check second page
        await swipePageViewRight(tester);
        textFinder = find.text(pageTwoText);
        expect(textFinder, findsOneWidget);

        // check third page
        await swipePageViewRight(tester);
        textFinder = find.text(pageThreeText);
        expect(textFinder, findsOneWidget);

        // back to second page
        await swipePageViewLeft(tester);
        textFinder = find.text(pageTwoText);
        expect(textFinder, findsOneWidget);

        // back to first page
        await swipePageViewLeft(tester);
        textFinder = find.text(pageOneText);
        expect(textFinder, findsOneWidget);
      });

      testWidgets(
          'Then it should call onPageChange callback with correct page number',
          (WidgetTester tester) async {
        var currentPage = 0;
        void onPageChange(int page) {
          currentPage = page;
        }

        await tester.pumpWidget(createWidget(onPageChange: onPageChange));

        await swipePageViewRight(tester);
        expect(currentPage, 1);

        await swipePageViewRight(tester);
        expect(currentPage, 2);

        await swipePageViewLeft(tester);
        expect(currentPage, 1);

        await swipePageViewLeft(tester);
        expect(currentPage, 0);
      });
    });
  });
}
