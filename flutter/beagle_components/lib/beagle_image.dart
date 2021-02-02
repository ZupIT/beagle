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

abstract class ImagePath {
  ImagePath._();

  factory ImagePath.local(String mobileId) = LocalImagePath;

  factory ImagePath.remote(String url) = RemoteImagePath;

  factory ImagePath.fromJson(Map<String, dynamic> json) {
    if (json[_jsonImagePathKey] == 'local') {
      return LocalImagePath(json[_jsonMobileIdKey]);
    } else {
      return RemoteImagePath(json[_jsonUrlKey]);
    }
  }

  static const _jsonImagePathKey = '_beagleImagePath_';
  static const _jsonMobileIdKey = 'mobileId';
  static const _jsonUrlKey = '_beagleImagePath_';
}

class LocalImagePath extends ImagePath {
  LocalImagePath(this.mobileId) : super._();

  final String mobileId;
}

class RemoteImagePath extends ImagePath {
  RemoteImagePath(this.url) : super._();
  final String url;
// TODO: placeholder
}
