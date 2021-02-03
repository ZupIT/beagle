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
import 'dart:ui';

import 'package:beagle/interface/beagle_image_downloader.dart';
import 'package:beagle/setup/beagle_design_system.dart';
import 'package:beagle_components/beagle_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:mockito/mockito.dart';
import 'package:flutter_test/flutter_test.dart';

import 'image/image_mock_data.dart';

class MockDesignSystem extends Mock implements DesignSystem {}

class MockBeagleImageDownloader extends Mock implements BeagleImageDownloader {}

void main() {
  final designSystemMock = MockDesignSystem();
  when(designSystemMock.image(any)).thenReturn('images/beagle_dog.png');

  final imageDownloaderMock = MockBeagleImageDownloader();
  when(imageDownloaderMock.downloadImage(any)).thenAnswer((invocation) {
    return Future<Uint8List>.value(mockedBeagleImageData);
  });

  const imageUrl = 'https://test.com/beagle.png';

  const imageKey = Key('BeagleImage');

  Widget createWidget({
    Key key = imageKey,
    DesignSystem designSystem,
    BeagleImageDownloader imageDownloader,
    ImagePath path,
    ImageContentMode mode,
  }) {
    return MaterialApp(
      home: BeagleImage(
        key: key,
        designSystem: designSystem,
        imageDownloader: imageDownloader,
        path: path,
        mode: mode,
      ),
    );
  }

  group('Given a BeagleImage', () {
    group('When path its a LocalImagePath', () {
      final localImage = createWidget(
        designSystem: designSystemMock,
        path: ImagePath.local('mobileId'),
      );

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

          final element = tester.element(find.byType(Image));
          final Image widget = element.widget;
          final image = widget.image;
          await precacheImage(image, element);
          await tester.pumpAndSettle();
        });

        final imageFinder = find.byType(Image);

        await expectLater(
          imageFinder,
          matchesGoldenFile('goldens/beagle_image_local.png'),
        );
      });
    });

    group('When path is RemoteImagePath', () {
      final remoteImage = createWidget(
        designSystem: designSystemMock,
        imageDownloader: imageDownloaderMock,
        path: ImagePath.remote(
          imageUrl,
          ImagePath.local('mobileId'),
        ),
      );

      testWidgets('Then it should have a Image widget child',
          (WidgetTester tester) async {
        await tester.pumpWidget(remoteImage);

        final imageFinder = find.byType(Image);

        expect(imageFinder, findsOneWidget);
      });

      testWidgets('Then it should present the correct remote image',
          (WidgetTester tester) async {
        await tester.runAsync(() async {
          await tester.pumpWidget(remoteImage);

          await tester.pumpAndSettle();

          final element = tester.element(find.byType(Image));
          final Image widget = element.widget;
          final image = widget.image;
          await precacheImage(image, element);
          await tester.pumpAndSettle();
        });

        final imageFinder = find.byType(Image);

        await expectLater(
          imageFinder,
          matchesGoldenFile('goldens/beagle_image_remote.png'),
        );
      });
    });

    group('When I set mode to ImageContentMode.CENTER', () {
      testWidgets('Then the widget should have BoxFit.none',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(
          designSystem: designSystemMock,
          path: ImagePath.local('mobileId'),
          mode: ImageContentMode.CENTER,
        ));

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.none);
      });
    });

    group('When I set mode to ImageContentMode.CENTER_CROP', () {
      testWidgets('Then the widget should have BoxFit.cover',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(
          designSystem: designSystemMock,
          path: ImagePath.local('mobileId'),
          mode: ImageContentMode.CENTER_CROP,
        ));

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.cover);
      });
    });

    group('When I set mode to ImageContentMode.FIT_CENTER', () {
      testWidgets('Then the widget should have BoxFit.contain',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(
          designSystem: designSystemMock,
          path: ImagePath.local('mobileId'),
          mode: ImageContentMode.FIT_CENTER,
        ));

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.contain);
      });
    });

    group('When I set mode to ImageContentMode.FIT_XY', () {
      testWidgets('Then the widget should have BoxFit.fill',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(
          designSystem: designSystemMock,
          path: ImagePath.local('mobileId'),
          mode: ImageContentMode.FIT_XY,
        ));

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.fill);
      });
    });

    group('When I do not set ImageContentMode', () {
      testWidgets('Then the widget should have BoxFit.none',
          (WidgetTester tester) async {
        await tester.pumpWidget(createWidget(
          designSystem: designSystemMock,
          path: ImagePath.local('mobileId'),
        ));

        expect(tester.widget<Image>(find.byType(Image)).fit, BoxFit.none);
      });
    });
  });
}
