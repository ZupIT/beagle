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

import 'package:beagle_components/beagle_text.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:beagle/utils/color_utils.dart';

const text = 'Beagle Text';
const textColor = '#00FF00';
const alignment = TextAlignment.RIGHT;
const textKey = Key('TextKey');

Widget createWidget({
  Key key = textKey,
  String text = text,
  String textColor = textColor,
  TextAlignment alignment = alignment,
}) {
  return MaterialApp(
    home: BeagleText(
      key: key,
      text: text,
      textColor: textColor,
      alignment: alignment,
    ),
  );
}

void main() {
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

        final expectedTextColor = hexToColor(textColor);

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
      testWidgets('Then it should have the default text color',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(textColor: null));

        final expectedTextColor = hexToColor(kBeagleTextDefaultTextColor);

        expect(tester.widget<Text>(find.text(text)).style.color,
            expectedTextColor);
      });
    });

    group('When a text alignment is not specified', () {
      testWidgets('Then it should have the default text alignment',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(alignment: null));

        const expectedTextAlign = kBeagleTextDefaultTextAlignment;

        expect(
            tester.widget<Text>(find.text(text)).textAlign, expectedTextAlign);
      });
    });
  });
}
