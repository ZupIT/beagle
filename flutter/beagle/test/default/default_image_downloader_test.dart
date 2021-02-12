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

import 'dart:convert';
import 'dart:typed_data';

import 'package:beagle/default/default_image_downloader.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/model/response.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

class MockHttpClient extends Mock implements HttpClient {}

void main() {
  group('Given a DefaultBeagleImageDownloader', () {
    group(
        'When downloadImage is called and http response is different than 200',
        () {
      test('Then it should throw BeagleImageDownloaderException', () {
        final fakeResponse = Response(404, '', {}, Uint8List(0));
        final httpClientMock = MockHttpClient();
        when(httpClientMock.sendRequest(any)).thenAnswer((invocation) {
          return Future<Response>.value(fakeResponse);
        });
        const url = 'an url';
        final imageDownloader =
            DefaultBeagleImageDownloader(httpClient: httpClientMock);

        expect(() => imageDownloader.downloadImage(url),
            throwsA(isInstanceOf<BeagleImageDownloaderException>()));
      });
    });

    group('When downloadImage is called and downloaded image file is empty',
        () {
      test('Then it should throw an Exception', () {
        final fakeResponse = Response(200, '', {}, Uint8List(0));
        final httpClientMock = MockHttpClient();
        when(httpClientMock.sendRequest(any)).thenAnswer((invocation) {
          return Future<Response>.value(fakeResponse);
        });
        const url = 'an url';
        final imageDownloader =
            DefaultBeagleImageDownloader(httpClient: httpClientMock);

        expect(() => imageDownloader.downloadImage(url),
            throwsA(isInstanceOf<Exception>()));
      });
    });

    group('When downloadImage is called', () {
      test('Then it should return an Uint8List', () async {
        final fakeImage = base64Decode(
            'iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==');
        final fakeResponse = Response(200, '', {}, fakeImage);
        final httpClientMock = MockHttpClient();
        when(httpClientMock.sendRequest(any)).thenAnswer((invocation) {
          return Future<Response>.value(fakeResponse);
        });
        const url = 'an url';
        final imageDownloader =
            DefaultBeagleImageDownloader(httpClient: httpClientMock);

        final downloadedImage = await imageDownloader.downloadImage(url);

        expect(downloadedImage, fakeImage);
      });
    });
  });
}
