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

import 'dart:io';
import 'dart:typed_data';

import 'package:beagle/interface/beagle_image_downloader.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/networking/beagle_request.dart';
import 'package:flutter/foundation.dart';

class DefaultBeagleImageDownloader implements BeagleImageDownloader {
  DefaultBeagleImageDownloader({
    @required this.httpClient,
  }) : assert(httpClient != null);

  final HttpClient httpClient;

  @override
  Future<Uint8List> downloadImage(String url) async {
    final request = BeagleRequest(url);
    final response = await httpClient.sendRequest(request);

    if (response.status != HttpStatus.ok) {
      throw BeagleImageDownloaderException(
          statusCode: response.status, url: request.url);
    }

    final bytes = response.bodyBytes;
    if (bytes.lengthInBytes == 0) {
      throw Exception('Image is an empty file: $url');
    }

    return bytes;
  }
}

class BeagleImageDownloaderException implements Exception {
  BeagleImageDownloaderException(
      {@required this.statusCode, @required this.url})
      : assert(url != null),
        assert(statusCode != null),
        _message = 'HTTP request failed, statusCode: $statusCode, $url';

  final int statusCode;

  final String _message;

  final String url;

  @override
  String toString() => _message;
}
