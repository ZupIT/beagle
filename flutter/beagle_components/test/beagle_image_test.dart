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

import 'dart:typed_data';

import 'package:beagle/beagle.dart';
import 'package:beagle_components/beagle_components.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

import 'image/image_mock_data.dart';
import 'service_locator/service_locator.dart';

class MockDesignSystem extends Mock implements BeagleDesignSystem {}

class MockBeagleImageDownloader extends Mock implements BeagleImageDownloader {}

class MockBeagleLogger extends Mock implements BeagleLogger {}

class MockBeagleYogaFactory extends Mock implements BeagleYogaFactory {}

void main() {
  final designSystemMock = MockDesignSystem();
  final imageDownloaderMock = MockBeagleImageDownloader();
  final beagleLoggerMock = MockBeagleLogger();
  final beagleYogaFactoryMock = MockBeagleYogaFactory();

  setUpAll(() async {
    await testSetupServiceLocator(
      designSystem: designSystemMock,
      imageDownloader: imageDownloaderMock,
      logger: beagleLoggerMock,
      beagleYogaFactory: beagleYogaFactoryMock,
    );
  });

  const imageUrl = 'https://test.com/beagle.png';
  const imageNotFoundUrl = 'https://notfound.com/beagle.png';
  const defaultPlaceholder = 'mobileId';
  const invalidPlaceholder = 'asset_does_not_exist';
  const errorStatusCode = 404;
  const imageKey = Key('BeagleImage');

  when(beagleYogaFactoryMock.createYogaLayout(
    style: anyNamed('style'),
    children: anyNamed('children'),
  )).thenAnswer((realInvocation) {
    final List<Widget> children = realInvocation.namedArguments.values.last;
    return children.first;
  });

  when(designSystemMock.image(defaultPlaceholder))
      .thenReturn('images/beagle_dog.png');

  when(designSystemMock.image(invalidPlaceholder)).thenReturn(null);

  when(imageDownloaderMock.downloadImage(imageUrl)).thenAnswer((invocation) {
    return Future<Uint8List>.value(mockedBeagleImageData);
  });
  when(imageDownloaderMock.downloadImage(imageNotFoundUrl))
      .thenAnswer((invocation) {
    throw BeagleImageDownloaderException(
        statusCode: errorStatusCode, url: imageNotFoundUrl);
  });

  Widget createWidget({
    Key key = imageKey,
    BeagleImageDownloader imageDownloader,
    ImagePath path,
    ImageContentMode mode,
  }) {
    return MaterialApp(
      home: BeagleImage(
        key: key,
        path: path,
        mode: mode,
      ),
    );
  }

  Widget createLocalWidget({
    String placeholder = defaultPlaceholder,
    ImageContentMode mode,
  }) {
    return createWidget(
      path: ImagePath.local(placeholder),
      mode: mode,
    );
  }

  Widget createRemoteWidget({
    String url = imageUrl,
    String placeholder = defaultPlaceholder,
    ImageContentMode mode,
  }) {
    return createWidget(
      imageDownloader: imageDownloaderMock,
      path: ImagePath.remote(
        url,
        ImagePath.local(placeholder),
      ),
      mode: mode,
    );
  }

  Future<dynamic> precacheImageForTest(WidgetTester tester) async {
    await tester.pumpAndSettle();

    final element = tester.element(find.byType(Image));
    final Image widget = element.widget;
    final image = widget.image;
    await precacheImage(image, element);
    await tester.pumpAndSettle();

    return null;
  }

  group('Given a BeagleImage with a LocalImagePath', () {
    final localImage = createLocalWidget();

    group('When I set a valid path', () {
      testWidgets('Then it should have a Image widget child',
          (WidgetTester tester) async {
        await tester.pumpWidget(localImage);
        final imageFinder = find.byType(Image);

        expect(imageFinder, findsOneWidget);
      });

      testWidgets('Then it should present the correct local image',
          (WidgetTester tester) async {
        await tester.runAsync(() async {
          await tester.pumpWidget(localImage);

          await precacheImageForTest(tester);
        });

        final imageFinder = find.byType(Image);

        await expectLater(
          imageFinder,
          matchesGoldenFile('goldens/beagle_image_local.png'),
        );
      });
    });

    group('When I set an invalid path', () {
      testWidgets('Then it should render an empty container',
          (WidgetTester tester) async {
        await tester
            .pumpWidget(createLocalWidget(placeholder: invalidPlaceholder));

        final containerFinder = find.byType(Container);

        expect(containerFinder, findsOneWidget);
      });
    });

    group('When I set mode to ImageContentMode.CENTER', () {
      testWidgets('Then the widget should have BoxFit.none',
          (WidgetTester tester) async {
        await tester
            .pumpWidget(createLocalWidget(mode: ImageContentMode.CENTER));

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.none);
      });
    });

    group('When I set mode to ImageContentMode.CENTER_CROP', () {
      testWidgets('Then the widget should have BoxFit.cover',
          (WidgetTester tester) async {
        await tester.pumpWidget(createLocalWidget(
          mode: ImageContentMode.CENTER_CROP,
        ));

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.cover);
      });
    });

    group('When I set mode to ImageContentMode.FIT_CENTER', () {
      testWidgets('Then the widget should have BoxFit.contain',
          (WidgetTester tester) async {
        await tester.pumpWidget(createLocalWidget(
          mode: ImageContentMode.FIT_CENTER,
        ));

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.contain);
      });
    });

    group('When I set mode to ImageContentMode.FIT_XY', () {
      testWidgets('Then the widget should have BoxFit.fill',
          (WidgetTester tester) async {
        await tester.pumpWidget(createLocalWidget(
          mode: ImageContentMode.FIT_XY,
        ));

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.fill);
      });
    });

    group('When I do not set ImageContentMode', () {
      testWidgets('Then the widget should have BoxFit.contain',
          (WidgetTester tester) async {
        await tester.pumpWidget(createLocalWidget());

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.contain);
      });
    });
  });

  group('Given a BeagleImage with a RemoteImagePath', () {
    group('When the widget is rendered', () {
      testWidgets('Then it should have a Image widget child',
          (WidgetTester tester) async {
        await tester.pumpWidget(createRemoteWidget());

        final imageFinder = find.byType(Image);

        expect(imageFinder, findsOneWidget);
      });

      testWidgets('Then it should present the correct remote image',
          (WidgetTester tester) async {
        await tester.runAsync(() async {
          await tester.pumpWidget(createRemoteWidget());

          await precacheImageForTest(tester);
        });

        final imageFinder = find.byType(Image);

        await expectLater(
          imageFinder,
          matchesGoldenFile('goldens/beagle_image_remote.png'),
        );
      });
    });

    group('When remote image url is not found', () {
      testWidgets('Then it should present image placeholder',
          (WidgetTester tester) async {
        await tester.runAsync(() async {
          await tester.pumpWidget(createRemoteWidget(url: imageNotFoundUrl));

          await precacheImageForTest(tester);
        });

        final imageFinder = find.byType(Image);

        await expectLater(
          imageFinder,
          matchesGoldenFile('goldens/beagle_image_remote_not_found.png'),
        );
      });
    });
    group('When remote image url is not found and placeholder is invalid', () {
      testWidgets('Then it should render an empty container',
          (WidgetTester tester) async {
        await tester.pumpWidget(createRemoteWidget(
            url: imageNotFoundUrl, placeholder: invalidPlaceholder));

        final containerFinder = find.byType(Container);

        expect(containerFinder, findsOneWidget);
      });
    });

    group('When I set mode to ImageContentMode.CENTER', () {
      testWidgets('Then the widget should have BoxFit.none',
          (WidgetTester tester) async {
        await tester.runAsync(() async {
          await tester.pumpWidget(
            createRemoteWidget(mode: ImageContentMode.CENTER),
          );

          await precacheImageForTest(tester);
        });

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.none);
      });
    });

    group('When I set mode to ImageContentMode.CENTER_CROP', () {
      testWidgets('Then the widget should have BoxFit.cover',
          (WidgetTester tester) async {
        await tester.runAsync(() async {
          await tester.pumpWidget(createRemoteWidget(
            mode: ImageContentMode.CENTER_CROP,
          ));
          await precacheImageForTest(tester);
        });

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.cover);
      });
    });

    group('When I set mode to ImageContentMode.FIT_CENTER', () {
      testWidgets('Then the widget should have BoxFit.contain',
          (WidgetTester tester) async {
        await tester.runAsync(() async {
          await tester.pumpWidget(createRemoteWidget(
            mode: ImageContentMode.FIT_CENTER,
          ));
          await precacheImageForTest(tester);
        });

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.contain);
      });
    });

    group('When I set mode to ImageContentMode.FIT_XY', () {
      testWidgets('Then the widget should have BoxFit.fill',
          (WidgetTester tester) async {
        await tester.runAsync(() async {
          await tester.pumpWidget(createRemoteWidget(
            mode: ImageContentMode.FIT_XY,
          ));
          await precacheImageForTest(tester);
        });

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.fill);
      });
    });

    group('When I do not set ImageContentMode', () {
      testWidgets('Then the widget should have BoxFit.contain',
          (WidgetTester tester) async {
        await tester.runAsync(() async {
          await tester.pumpWidget(createRemoteWidget());
          await precacheImageForTest(tester);
        });

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.contain);
      });
    });
  });
}
