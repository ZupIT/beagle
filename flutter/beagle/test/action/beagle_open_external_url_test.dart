import 'package:beagle/action/beagle_open_external_url.dart';
import 'package:beagle/logger/beagle_logger.dart';
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

final List<MethodCall> log = <MethodCall>[];
MethodChannel channel = const MethodChannel('plugins.flutter.io/url_launcher');

class BeagleLoggerMock extends Mock implements BeagleLogger {}

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('Given Beagle Open External URL Action ', () {
    group('When I call launchURL successfully', () {
      test(
          'Then it should call launch library method with given URL as argument',
          () async {
        String url;
        String method;
        const expectedUrl = 'http://example.com';

        channel.setMockMethodCallHandler((MethodCall methodCall) async {
          url = methodCall.arguments['url'];
          method = methodCall.method;
          if (method == 'canLaunch') {
            return true;
          }
        }); // Register the mock handler.

        await BeagleOpenExternalUrl.launchURL(expectedUrl);

        expect(method, equals('launch'));
        expect(url, equals(expectedUrl));

        channel.setMockMethodCallHandler(null); // Unregister the mock handler.
      });
    });
  });
}
