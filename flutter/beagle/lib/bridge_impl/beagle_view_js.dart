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
import 'package:beagle/interface/beagle_navigator.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/interface/renderer.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle/model/network_options.dart';

class BeagleViewJS implements BeagleView {
  BeagleViewJS(
      {
      // ignore: avoid_unused_constructor_parameters
      NetworkOptions networkOptions,
      // ignore: avoid_unused_constructor_parameters
      String initialControllerId}) {
    _id = BeagleJSEngine.createBeagleView();
    BeagleViewJS.views[_id] = this;
    navigator = BeagleNavigatorJS(_id);
  }

  String _id;
  BeagleNavigatorJS navigator;
  static Map<String, BeagleViewJS> views = {};

  @override
  void Function() addErrorListener(ViewErrorListener listener) {
    return BeagleJSEngine.onViewUpdateError(_id, listener);
  }

  @override
  void destroy() {
    BeagleJSEngine.removeViewListeners(_id);
  }

  @override
  BeagleNavigator getNavigator() {
    return navigator;
  }

  @override
  Renderer getRenderer() {
    // TODO: implement getRenderer
    throw UnimplementedError();
  }

  @override
  BeagleUIElement getTree() {
    // TODO: implement getTree
    throw UnimplementedError();
  }

  @override
  void Function() subscribe(ViewUpdateListener listener) {
    return BeagleJSEngine.onViewUpdate(_id, listener);
  }
}
