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

import 'dart:convert';

import 'package:beagle/networking/beagle_http_method.dart';
import 'package:beagle/networking/beagle_network_strategy.dart';
import 'package:beagle/utils/enum.dart';
import 'package:beagle/utils/network_strategy.dart';

class BeagleNetworkOptions {
  BeagleNetworkOptions({this.method, this.headers, this.strategy});

  BeagleHttpMethod method;
  Map<String, String> headers;
  BeagleNetworkStrategy strategy;

  String toJsonEncode() {
    final params = <String, dynamic>{};

    if (method != null) {
      params['method'] = EnumUtils.name(method);
    }
    if (headers != null) {
      params['headers'] = headers;
    }
    if (strategy != null) {
      params['strategy'] = NetworkStrategyUtils.getJsStrategyName(strategy);
    }

    return jsonEncode(params);
  }
}
