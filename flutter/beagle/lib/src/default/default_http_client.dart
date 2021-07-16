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
import 'package:beagle/beagle.dart';
import 'package:http/http.dart' as http;

class DefaultHttpClient implements HttpClient {
  const DefaultHttpClient();
  @override
  Future<Response> sendRequest(BeagleRequest req) async {
    final uri = Uri.parse(req.url);
    final handlers = {
      BeagleHttpMethod.get: () => http.get(uri, headers: req.headers),
      BeagleHttpMethod.post: () =>
          http.post(uri, headers: req.headers, body: req.body),
      BeagleHttpMethod.put: () =>
          http.put(uri, headers: req.headers, body: req.body),
      BeagleHttpMethod.patch: () =>
          http.patch(uri, headers: req.headers, body: req.body),
      BeagleHttpMethod.delete: () => http.delete(uri, headers: req.headers),
    };
    final response = await handlers[req.method]();
    return Response(response.statusCode, response.body, response.headers,
        response.bodyBytes);
  }
}
