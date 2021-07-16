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
import 'package:beagle/src/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/src/bridge_impl/renderer_js.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

class MockBeagleJSEngine extends Mock implements BeagleJSEngine {}

void main() {
  final beagleJSEngine = MockBeagleJSEngine();
  final tree = BeagleUIElement(
      {'_beagleComponent_': 'beagle:button', 'text': 'Click me!'});

  group('Given a RendererJS object', () {
    final renderer = RendererJS(beagleJSEngine, 'viewId');

    group('When doFullRender is called', () {
      test('Then it should do full render', () {
        renderer.doFullRender(tree);
        expect(
            verify(beagleJSEngine.evaluateJavascriptCode(captureAny))
                .captured
                .single,
            "global.beagle.getViewById('viewId').getRenderer().doFullRender(${jsonEncode(tree.properties)})");
      });
    });

    group('When doFullRender is called passing an anchor', () {
      test('Then it should do full render by replacing a branch of the tree',
          () {
        renderer.doFullRender(tree, 'elementId');
        expect(
            verify(beagleJSEngine.evaluateJavascriptCode(captureAny))
                .captured
                .single,
            "global.beagle.getViewById('viewId').getRenderer().doFullRender(${jsonEncode(tree.properties)}, 'elementId')");
      });
    });

    group('When doFullRender is called passing an anchor and append mode', () {
      test(
          'Then it should do full render by appending an element to a branch of the tree',
          () {
        renderer.doFullRender(tree, 'elementId', TreeUpdateMode.append);
        expect(
            verify(beagleJSEngine.evaluateJavascriptCode(captureAny))
                .captured
                .single,
            "global.beagle.getViewById('viewId').getRenderer().doFullRender(${jsonEncode(tree.properties)}, 'elementId', 'append')");
      });
    });

    group('When doPartialRender is called', () {
      test('Then it should do partial render', () {
        final tree = BeagleUIElement({
          '_beagleComponent_': 'beagle:button',
          'id': 'beagle1',
          'text': 'Click me!'
        });
        renderer.doPartialRender(tree);
        expect(
            verify(beagleJSEngine.evaluateJavascriptCode(captureAny))
                .captured
                .single,
            "global.beagle.getViewById('viewId').getRenderer().doPartialRender(${jsonEncode(tree.properties)})");
      });
    });

    group('When doPartialRender is called passing an anchor', () {
      test('Then it should do partial render by replacing a branch of the tree',
          () {
        renderer.doPartialRender(tree, 'elementId');
        expect(
            verify(beagleJSEngine.evaluateJavascriptCode(captureAny))
                .captured
                .single,
            "global.beagle.getViewById('viewId').getRenderer().doPartialRender(${jsonEncode(tree.properties)}, 'elementId')");
      });
    });

    group('When doPartialRender is called passing an anchor and prepend mode',
        () {
      test(
          'Should do full partial by prepending an element to a branch of the tree',
          () {
        renderer.doPartialRender(tree, 'elementId', TreeUpdateMode.prepend);
        expect(
            verify(beagleJSEngine.evaluateJavascriptCode(captureAny))
                .captured
                .single,
            "global.beagle.getViewById('viewId').getRenderer().doPartialRender(${jsonEncode(tree.properties)}, 'elementId', 'prepend')");
      });
    });
  });
}
