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

import 'package:beagle/beagle.dart';
import 'package:beagle/src/bridge_impl/beagle_js_engine.dart';

class RendererJS implements Renderer {
  RendererJS(this._beagleJSEngine, this._viewId);

  final String _viewId;
  final BeagleJSEngine _beagleJSEngine;

  String _getJsTreeUpdateModeName(TreeUpdateMode mode) {
    /* When calling toString in an enum, it returns EnumName.EnumValue, we just need the part after
    the ".", which will give us the strategy name. */
    return mode.toString().split('.')[1];
  }

  void _doRender(bool isFull, BeagleUIElement tree,
      [String anchor, TreeUpdateMode mode]) {
    final method = isFull ? 'doFullRender' : 'doPartialRender';
    final jsonTree = jsonEncode(tree.properties);
    final anchorArg = anchor == null ? '' : ", '$anchor'";
    final modeArg = mode == null ? '' : ", '${_getJsTreeUpdateModeName(mode)}'";
    _beagleJSEngine.evaluateJavascriptCode(
        "global.beagle.getViewById('$_viewId').getRenderer().$method($jsonTree$anchorArg$modeArg)");
  }

  @override
  void doFullRender(BeagleUIElement tree,
      [String anchor, TreeUpdateMode mode]) {
    _doRender(true, tree, anchor, mode);
  }

  @override
  void doPartialRender(BeagleUIElement tree,
      [String anchor, TreeUpdateMode mode]) {
    _doRender(false, tree, anchor, mode);
  }
}
