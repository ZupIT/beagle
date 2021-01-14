import 'dart:convert';

import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/interface/global_context.dart';

class GlobalContextSerializationError implements Exception {
  GlobalContextSerializationError(Type type) {
    Exception(
        'Cannot set global context value. The value passed as parameter is not encodable ($type). Please, use a value of the type Map, Array, String, num or bool.');
  }
}

class GlobalContextJS implements GlobalContext {
  static bool _isEncodable(dynamic value) {
    return value is num || value is String || value is List || value is Map;
  }

  @override
  void clear([String path]) {
    final args = path == null ? '' : "'$path'";
    BeagleJSEngine.js
        .evaluate('global.beagle.getService().globalContext.clear($args)');
  }

  @override
  T get<T>([String path]) {
    final args = path == null ? '' : "'$path'";
    return BeagleJSEngine.js
        .evaluate('global.beagle.getService().globalContext.get($args)')
        .rawResult;
  }

  @override
  void set<T>(T value, [String path]) {
    if (!_isEncodable(value)) {
      throw GlobalContextSerializationError(value.runtimeType);
    }

    final jsonString = json.encode(value);
    final args = path == null ? jsonString : "$jsonString, '$path'";
    BeagleJSEngine.js
        .evaluate('global.beagle.getService().globalContext.set($args)');
  }
}
