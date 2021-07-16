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
import 'package:flutter_test/flutter_test.dart';

const touchableKey = Key('BeagleTouchable');

Widget createWidget({
  Key touchableKey = touchableKey,
  Function onPress,
  Widget child,
}) {
  return MaterialApp(
    home: BeagleTouchable(
      key: touchableKey,
      onPress: onPress,
      child: child,
    ),
  );
}

void main() {
  group('Given a BeagleTouchable', () {
    group('When I click on it', () {
      testWidgets('Then it should call onPress callback',
          (WidgetTester tester) async {
        var tapCount = 0;
        void onPress() {
          tapCount++;
        }

        await tester.pumpWidget(createWidget(onPress: onPress));

        await tester.tap(find.byType(BeagleTouchable));
        await tester.tap(find.byType(BeagleTouchable));
        await tester.tap(find.byType(BeagleTouchable));

        const expectedTapCount = 3;
        expect(tapCount, expectedTapCount);
      });
    });
  });
}
