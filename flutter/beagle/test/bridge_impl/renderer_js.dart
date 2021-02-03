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

import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/bridge_impl/renderer_js.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle/model/tree_update_mode.dart';
import 'package:flutter_js/flutter_js.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

class MockJSRuntime extends Mock implements JavascriptRuntime {}

void main() {
  test('Should do full render', () {
    BeagleJSEngine.js = MockJSRuntime();
    final renderer = RendererJS('viewId');
    final tree = BeagleUIElement(
        {'_beagleComponent_': 'beagle:button', 'text': 'Click me!'});
    renderer.doFullRender(tree);
    expect(verify(BeagleJSEngine.js.evaluate(captureAny)).captured.single,
        "global.beagle.getViewById('viewId').getRenderer().doFullRender(${jsonEncode(tree.properties)})");
  });

  test('Should do full render by replacing a branch of the tree', () {
    BeagleJSEngine.js = MockJSRuntime();
    final renderer = RendererJS('viewId');
    final tree = BeagleUIElement(
        {'_beagleComponent_': 'beagle:button', 'text': 'Click me!'});
    renderer.doFullRender(tree, 'elementId');
    expect(verify(BeagleJSEngine.js.evaluate(captureAny)).captured.single,
        "global.beagle.getViewById('viewId').getRenderer().doFullRender(${jsonEncode(tree.properties)}, 'elementId')");
  });

  test('Should do full render by appending an element to a branch of the tree',
      () {
    BeagleJSEngine.js = MockJSRuntime();
    final renderer = RendererJS('viewId');
    final tree = BeagleUIElement(
        {'_beagleComponent_': 'beagle:button', 'text': 'Click me!'});
    renderer.doFullRender(tree, 'elementId', TreeUpdateMode.append);
    expect(verify(BeagleJSEngine.js.evaluate(captureAny)).captured.single,
        "global.beagle.getViewById('viewId').getRenderer().doFullRender(${jsonEncode(tree.properties)}, 'elementId', 'append')");
  });

  test('Should do partial render', () {
    BeagleJSEngine.js = MockJSRuntime();
    final renderer = RendererJS('viewId');
    final tree = BeagleUIElement({
      '_beagleComponent_': 'beagle:button',
      'id': 'beagle1',
      'text': 'Click me!'
    });
    renderer.doPartialRender(tree);
    expect(verify(BeagleJSEngine.js.evaluate(captureAny)).captured.single,
        "global.beagle.getViewById('viewId').getRenderer().doPartialRender(${jsonEncode(tree.properties)})");
  });

  test('Should do partial render by replacing a branch of the tree', () {
    BeagleJSEngine.js = MockJSRuntime();
    final renderer = RendererJS('viewId');
    final tree = BeagleUIElement({
      '_beagleComponent_': 'beagle:button',
      'id': 'beagle1',
      'text': 'Click me!'
    });
    renderer.doPartialRender(tree, 'elementId');
    expect(verify(BeagleJSEngine.js.evaluate(captureAny)).captured.single,
        "global.beagle.getViewById('viewId').getRenderer().doPartialRender(${jsonEncode(tree.properties)}, 'elementId')");
  });

  test(
      'Should do full partial by prepending an element to a branch of the tree',
      () {
    BeagleJSEngine.js = MockJSRuntime();
    final renderer = RendererJS('viewId');
    final tree = BeagleUIElement({
      '_beagleComponent_': 'beagle:button',
      'id': 'beagle1',
      'text': 'Click me!'
    });
    renderer.doPartialRender(tree, 'elementId', TreeUpdateMode.prepend);
    expect(verify(BeagleJSEngine.js.evaluate(captureAny)).captured.single,
        "global.beagle.getViewById('viewId').getRenderer().doPartialRender(${jsonEncode(tree.properties)}, 'elementId', 'prepend')");
  });
}
