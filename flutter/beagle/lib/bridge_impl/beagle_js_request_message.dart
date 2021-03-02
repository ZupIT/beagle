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

import 'package:beagle/networking/beagle_http_method.dart';
import 'package:beagle/networking/beagle_request.dart';
import 'package:beagle/utils/enum.dart';

/// Encapsulates a Beagle javascript HTTP request message.
class BeagleJSRequestMessage {
  BeagleJSRequestMessage.fromJson(Map<String, dynamic> json) {
    _requestId = json['id'];
    _url = json['url'];
    _method = _getHttpMethod(json);
    _headers = _getHeaders(json);
    _body = json['body'];
  }

  String _requestId;
  String _url;
  BeagleHttpMethod _method;
  Map<String, String> _headers;
  String _body;

  BeagleHttpMethod _getHttpMethod(Map<String, dynamic> json) {
    final String httpMethodStr =
        json.containsKey('method') ? json['method'].toLowerCase() : 'get';

    return EnumUtils.fromString(BeagleHttpMethod.values, httpMethodStr);
  }

  Map<String, String> _getHeaders(Map<String, dynamic> json) {
    return json.containsKey('headers')
        // ignore: avoid_as
        ? (json['headers'] as Map<String, dynamic>).cast<String, String>()
        : null;
  }

  String get requestId => _requestId;

  BeagleRequest toRequest() {
    return BeagleRequest(_url, method: _method, headers: _headers, body: _body);
  }
}
