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

import 'package:beagle_components/beagle_scroll_view.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';

import 'service_locator/service_locator.dart';

Widget createWidget({
  ScrollAxis scrollDirection,
  bool scrollBarEnabled,
}) {
  return MaterialApp(
    home: BeagleScrollView(
      key: Key('scrollKey'),
      scrollBarEnabled: scrollBarEnabled,
      scrollDirection: scrollDirection,
      children: [Text('Scrollable content')],
    ),
  );
}

void main() {
  setUpAll(() async {
    await testSetupServiceLocator();
  });

  group('Given a BeagleScrollView', () {
    group('When the widget is created', () {
      testWidgets(
        'Then there should be a vertical ListView with a visible ScrollBar and a Text as its content',
        (WidgetTester tester) async {
          await tester.pumpWidget(createWidget());

          final scrollbarFinder = find.byType(Scrollbar);
          final listViewFinder = find.byType(ListView);
          final textFinder = find.byType(Text);

          expect(scrollbarFinder, findsOneWidget);
          expect(listViewFinder, findsOneWidget);
          expect(textFinder, findsOneWidget);

          final scrollbar = tester.widget<Scrollbar>(scrollbarFinder);
          final ListView listView = scrollbar.child;
          expect(listView.scrollDirection == Axis.vertical, isTrue);
          expect(listView.semanticChildCount == 1, isTrue);
        },
      );
    });

    group('When the widget is created with a horizontal scroll', () {
      testWidgets('Then the list view orientation should be horizontal', (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(scrollDirection: ScrollAxis.HORIZONTAL));
        final listViewFinder = find.byType(ListView);
        final ListView listView = tester.widget<ListView>(listViewFinder);
        expect(listView.scrollDirection == Axis.horizontal, isTrue);
      });
    });

    group('When the widget is created with a hidden scroll bar', () {
      testWidgets('Then there should be a ListView, but no ScrollBar', (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(scrollBarEnabled: false));
        final listViewFinder = find.byType(ListView);
        final scrollbarFinder = find.byType(Scrollbar);
        expect(listViewFinder, findsOneWidget);
        expect(scrollbarFinder, findsNothing);
      });
    });
  });
}
