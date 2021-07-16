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

import 'package:beagle/beagle.dart';

import 'beagle_js_engine.dart';

class GlobalContextSerializationError implements Exception {
  GlobalContextSerializationError(Type type) {
    Exception(
        'Cannot set global context value. The value passed as parameter is not encodable ($type). Please, use a value of the type Map, Array, String, num or bool.');
  }
}

/// Access to the Global Context API. Use it to set persistent values that can be retrieved and
/// manipulated by the widgets rendered by Beagle.
class GlobalContextJS implements GlobalContext {
  GlobalContextJS(this._beagleJSEngine);

  final BeagleJSEngine _beagleJSEngine;

  @override
  void clear([String path]) {
    final args = path == null ? '' : "'$path'";
    _beagleJSEngine.evaluateJavascriptCode(
        'global.beagle.getService().globalContext.clear($args)');
  }

  @override
  T get<T>([String path]) {
    final args = path == null ? '' : "'$path'";
    return _beagleJSEngine
        .evaluateJavascriptCode(
            'global.beagle.getService().globalContext.get($args)')
        .rawResult;
  }

  @override
  void set<T>(T value, [String path]) {
    if (!_isEncodable(value)) {
      throw GlobalContextSerializationError(value.runtimeType);
    }

    final jsonString = json.encode(value);
    final args = path == null ? jsonString : "$jsonString, '$path'";
    _beagleJSEngine.evaluateJavascriptCode(
        'global.beagle.getService().globalContext.set($args)');
  }

  bool _isEncodable(dynamic value) {
    return value is num || value is String || value is List || value is Map;
  }
}
