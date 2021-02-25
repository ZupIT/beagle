import 'package:beagle/action/beagle_open_external_url.dart';
import 'package:beagle/action/beagle_open_native_route.dart';
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
    BeagleOpenNativeRoute mockOpenNativeRoute = MockOpenNativeRoute();
    mockOpenNativeRoute.buildContext = _mockContext;

    setUp(() {
      mockObserver = MockNavigatorObserver();
    });

    Future<void> _buildPage(WidgetTester tester) async {
      await tester.pumpWidget(MaterialApp(
        routes: {mockRoute: (context) => const Text('Test')},
        home: RaisedButton(
            onPressed: () => {mockOpenNativeRoute.navigate(mockRoute)}),
        navigatorObservers: [mockObserver],
      ));
    }

    Future<void> _navigate(WidgetTester tester) async {
      await tester.tap(find.byType(RaisedButton));
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
