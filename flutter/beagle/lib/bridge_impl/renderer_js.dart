import 'dart:convert';

import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/interface/renderer.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle/model/tree_update_mode.dart';
import 'package:flutter/widgets.dart';

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
