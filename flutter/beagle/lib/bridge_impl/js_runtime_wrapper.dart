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

import 'package:flutter_js/flutter_js.dart';

class JavascriptRuntimeWrapper {
  JavascriptRuntimeWrapper(this._jsRuntime);

  final JavascriptRuntime _jsRuntime;

  JsEvalResult evaluate(String code) {
    return _jsRuntime.evaluate(code);
  }

  Future<JsEvalResult> evaluateAsync(String code) {
    return _jsRuntime.evaluateAsync(code);
  }

  dynamic onMessage(String channelName, void Function(dynamic args) fn) {
    return _jsRuntime.onMessage(channelName, fn);
  }

  Future<JsEvalResult> handlePromise(JsEvalResult value, {Duration timeout}) {
    return _jsRuntime.handlePromise(value, timeout: timeout);
  }

  dynamic enableHandlePromises() {
    return _jsRuntime.enableHandlePromises();
  }
}
