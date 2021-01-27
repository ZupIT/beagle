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

import 'package:beagle_components/beagle_button.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';

const buttonText = 'Beagle Button';
const buttonKey = Key('BeagleButton');
final widget = MaterialApp(
  home: createBeagleButton(),
);

void buttonOnPress() {}

BeagleButton createBeagleButton({
  Key buttonKey = buttonKey,
  String buttonText = buttonText,
  Function buttonOnPress = buttonOnPress,
  bool buttonDisabled = false,
}) {
  return BeagleButton(
    key: buttonKey,
    text: buttonText,
    onPress: buttonOnPress,
    disabled: buttonDisabled,
  );
}

void main() {
  testWidgets('BeagleButton has a ElevatedButton', (WidgetTester tester) async {
    await tester.pumpWidget(widget);

    final buttonFinder = find.byType(ElevatedButton);

    expect(buttonFinder, findsOneWidget);
  });

  testWidgets('BeagleButton has a text', (WidgetTester tester) async {
    await tester.pumpWidget(widget);

    final textFinder = find.text(buttonText);

    expect(textFinder, findsOneWidget);
  });

  testWidgets('BeagleButton is enabled', (WidgetTester tester) async {
    await tester.pumpWidget(widget);

    expect(tester.widget<ElevatedButton>(find.byType(ElevatedButton)).enabled,
        isTrue);
  });

  testWidgets('BeagleButton is disabled', (WidgetTester tester) async {
    final widget = MaterialApp(home: createBeagleButton(buttonDisabled: true));

    await tester.pumpWidget(widget);

    expect(tester.widget<ElevatedButton>(find.byType(ElevatedButton)).enabled,
        isFalse);
  });

  testWidgets('BeagleButton fire onPress callback when enabled',
      (WidgetTester tester) async {
    final log = <int>[];
    void onPressed() {
      log.add(0);
    }

    final widget =
        MaterialApp(home: createBeagleButton(buttonOnPress: onPressed));

    await tester.pumpWidget(widget);
    await tester.tap(find.byType(BeagleButton));

    expect(log.length, 1);
  });

  testWidgets("BeagleButton don't fire onPress callback when disabled",
      (WidgetTester tester) async {
    final log = <int>[];
    void onPressed() {
      log.add(0);
    }

    final widget = MaterialApp(
        home: createBeagleButton(
      buttonOnPress: onPressed,
      buttonDisabled: true,
    ));

    await tester.pumpWidget(widget);
    await tester.tap(find.byType(BeagleButton));

    expect(log.length, 0);
  });
}
