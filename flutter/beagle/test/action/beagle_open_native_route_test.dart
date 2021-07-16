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
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

class ContextMock extends Mock implements BuildContext {}

class MockNavigatorObserver extends Mock implements NavigatorObserver {}

class MockOpenNativeRoute extends Mock implements BeagleOpenNativeRoute {}

void main() {
  group('Given Beagle Open Native Route Action ', () {
    ContextMock _mockContext;
    const mockRoute = '/route';
    NavigatorObserver mockObserver;
    final BeagleOpenNativeRoute mockOpenNativeRoute = MockOpenNativeRoute();

    setUp(() {
      mockObserver = MockNavigatorObserver();
    });

    Future<void> _buildPage(WidgetTester tester) async {
      await tester.pumpWidget(MaterialApp(
        routes: {mockRoute: (context) => const Text('Test')},
        home: ElevatedButton(
          onPressed: () =>
              {mockOpenNativeRoute.navigate(_mockContext, mockRoute)},
          child: const SizedBox.shrink(),
        ),
        navigatorObservers: [mockObserver],
      ));
    }

    Future<void> _navigate(WidgetTester tester) async {
      await tester.tap(find.byType(ElevatedButton));
      await tester.pumpAndSettle();
    }

    group('When I call navigate successfully', () {
      testWidgets('Then it should push a route', (WidgetTester tester) async {
        await _buildPage(tester);
        await _navigate(tester);
        verify(mockObserver.didPush(any, any));
      });
    });
  });
}
