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
import 'package:beagle/components/beagle_undefined_widget.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

const text = 'Undefined Component';

Widget createWidget(
    {String text = text,
    BeagleEnvironment environment = BeagleEnvironment.debug}) {
  return MaterialApp(
    home: BeagleUndefinedWidget(
      environment: environment,
    ),
  );
}

void main() {
  group('Given a Undefined widget', () {
    group('When the widget is rendered', () {
      testWidgets('Then it should have the correct text',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget());

        final textFinder = find.text(text);

        expect(textFinder, findsOneWidget);
      });

      testWidgets('Then it should not have text widget',
          (WidgetTester tester) async {
        await tester.pumpWidget(
            createWidget(environment: BeagleEnvironment.production));

        final textFinder = find.text(text);

        expect(textFinder, findsNothing);
      });
    });
  });
}
