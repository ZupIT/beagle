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

import 'package:beagle/bridge_impl/beagle_supported_http_methods.dart';
import 'package:beagle/model/request.dart';
import 'package:flutter_js/extensions/xhr.dart';

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
  HttpMethod _method;
  Map<String, String> _headers;
  String _body;

  HttpMethod _getHttpMethod(Map<String, dynamic> json) {
    final String httpMethodStr =
        json.containsKey('method') ? json['method'].toLowerCase() : 'get';

    return BeagleSupportedHttpMethods().getHttpMethod(httpMethodStr);
  }

  Map<String, String> _getHeaders(Map<String, dynamic> json) {
    return json.containsKey('headers')
        // ignore: avoid_as
        ? (json['headers'] as Map<String, dynamic>).cast<String, String>()
        : null;
  }

  String get requestId => _requestId;

  Request toRequest() {
    return Request(_url, method: _method, headers: _headers, body: _body);
  }
}
