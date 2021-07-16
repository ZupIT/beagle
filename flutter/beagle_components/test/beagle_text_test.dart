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
import 'package:beagle_components/beagle_components.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_test/flutter_test.dart';

import 'service_locator/service_locator.dart';

const text = 'Beagle Text';
const textColor = '#00FF00';
const alignment = TextAlignment.RIGHT;
const textKey = Key('TextKey');
const textStyle = TextStyle(
  color: Colors.black,
  backgroundColor: Colors.indigo,
);

Widget createWidget({
  Key key = textKey,
  String text = text,
  String textColor = textColor,
  TextAlignment alignment = alignment,
  String styleId,
}) {
  return MaterialApp(
    home: BeagleText(
      key: key,
      text: text,
      textColor: textColor,
      alignment: alignment,
      styleId: styleId,
    ),
  );
}

void main() {
  setUpAll(() async {
    await testSetupServiceLocator();
  });

  group('Given a BeagleText', () {
    group('When the widget is rendered', () {
      testWidgets('Then it should have the correct text',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget());

        final textFinder = find.text(text);

        expect(textFinder, findsOneWidget);
      });

      testWidgets('Then it should have the correct text color',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget());

        final expectedTextColor = HexColor(textColor);

        expect(tester.widget<Text>(find.text(text)).style.color,
            expectedTextColor);
      });

      testWidgets('Then it should have the correct text alignment',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget());

        const expectedTextAlign = TextAlign.right;

        expect(
            tester.widget<Text>(find.text(text)).textAlign, expectedTextAlign);
      });
    });

    group('When a text color is not specified', () {
      testWidgets('Then it should not set text color',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(textColor: null));

        expect(tester.widget<Text>(find.text(text)).style.color, null);
      });
    });

    group('When a text alignment is not specified', () {
      testWidgets('Then it should not set text alignment',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(alignment: null));

        expect(tester.widget<Text>(find.text(text)).textAlign, null);
      });
    });

    group('When set style', () {
      testWidgets('Then it should have the correct style',
          (WidgetTester tester) async {
        // WHEN
        await tester
            .pumpWidget(createWidget(styleId: 'text-one', textColor: null));

        //THEN
        final textFinder = find.text(text);
        final textCreated = tester.widget<Text>(find.text(text));

        expect(textFinder, findsOneWidget);
        expect(textCreated.style.color, textStyle.color);
        expect(textCreated.style.backgroundColor, textStyle.backgroundColor);
      });
    });
  });

  group('When set style with text color', () {
    testWidgets('Then it should have the correct style',
        (WidgetTester tester) async {
      // WHEN
      await tester.pumpWidget(createWidget(styleId: 'text-one'));

      //THEN
      final textFinder = find.text(text);
      final textCreated = tester.widget<Text>(find.text(text));
      final expectedTextColor = HexColor(textColor);

      expect(textFinder, findsOneWidget);
      expect(textCreated.style.color, expectedTextColor);
      expect(textCreated.style.backgroundColor, textStyle.backgroundColor);
    });
  });

  group('When set alignment to CENTER ', () {
    testWidgets('Then it should be a Center Widget with text aligned to center',
        (WidgetTester tester) async {
      // WHEN
      await tester.pumpWidget(createWidget(alignment: TextAlignment.CENTER));

      //THEN
      final textFinder = find.text(text);
      final centerFinder = find.byType(Center);
      final centerCreated = tester.widget<Center>(centerFinder);
      final textCreated = centerCreated.child as Text;
      const expectedTextAlign = TextAlign.center;

      expect(centerFinder, findsOneWidget);
      expect(textFinder, findsOneWidget);
      expect(textCreated.textAlign, expectedTextAlign);
    });
  });
}
