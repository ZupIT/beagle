/*
 *
 *  Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import 'dart:async';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/model/request.dart';
import 'package:beagle/model/response.dart';
import 'package:flutter_js/extensions/xhr.dart';
import 'package:http/http.dart' as http;

class DefaultHttpClient implements HttpClient {
  @override
  Future<Response> sendRequest(Request req) async {
    final handlers = {
      HttpMethod.get: () => http.get(req.url, headers: req.headers),
      HttpMethod.post: () =>
          http.post(req.url, headers: req.headers, body: req.body),
      HttpMethod.put: () =>
          http.put(req.url, headers: req.headers, body: req.body),
      HttpMethod.patch: () =>
          http.patch(req.url, headers: req.headers, body: req.body),
      HttpMethod.delete: () => http.delete(req.url, headers: req.headers),
    };
    final response = await handlers[req.method]();
    return Response(response.statusCode, response.body, response.headers,
        response.bodyBytes);
  }
}
