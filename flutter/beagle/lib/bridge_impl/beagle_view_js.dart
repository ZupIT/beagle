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

import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/bridge_impl/beagle_navigator_js.dart';
import 'package:beagle/bridge_impl/renderer_js.dart';
import 'package:beagle/interface/beagle_navigator.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/interface/renderer.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle/networking/beagle_network_options.dart';

/// Creates a new Beagle View. There are two optional parameters: the networkOptions and the
/// initialControllerId. The first one sets network options for every view requested by this
/// Beagle View (headers, http method and cache strategy). If nothing is specied, the default
/// network options are used (beagle headers, get and beagle-with-fallback-to-cache). The
/// initialControllerId is the id of the navigation controller for the first navigation stack.
/// If not specified, the default navigation controller is used.
class BeagleViewJS implements BeagleView {
  BeagleViewJS(
    this._beagleJSEngine, {
    BeagleNetworkOptions networkOptions,
    String initialControllerId,
  }) {
    _id = _beagleJSEngine.createBeagleView(
      networkOptions: networkOptions,
      initialControllerId: initialControllerId,
    );
    BeagleViewJS.views[_id] = this;
    _navigator = BeagleNavigatorJS(_beagleJSEngine, _id);
    _renderer = RendererJS(_beagleJSEngine, _id);
  }

  String _id;
  BeagleNavigatorJS _navigator;
  Renderer _renderer;
  static Map<String, BeagleViewJS> views = {};
  final BeagleJSEngine _beagleJSEngine;

  @override
  void Function() addErrorListener(ViewErrorListener listener) {
    return _beagleJSEngine.onViewUpdateError(_id, listener);
  }

  @override
  void destroy() {
    _beagleJSEngine.removeViewListeners(_id);
  }

  @override
  BeagleNavigator getNavigator() {
    return _navigator;
  }

  @override
  Renderer getRenderer() {
    return _renderer;
  }

  @override
  BeagleUIElement getTree() {
    final result = _beagleJSEngine
        .evaluateJavascriptCode("global.beagle.getViewById('$_id').getTree()")
        .rawResult;
    return BeagleUIElement(result);
  }

  @override
  void Function() subscribe(ViewUpdateListener listener) {
    return _beagleJSEngine.onViewUpdate(_id, listener);
  }
}
