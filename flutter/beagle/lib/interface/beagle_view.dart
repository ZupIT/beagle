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

import 'package:beagle/interface/beagle_navigator.dart';
import 'package:beagle/interface/renderer.dart';
import 'package:beagle/interface/types.dart';
import 'package:beagle/model/beagle_ui_element.dart';

typedef ViewUpdateListener = void Function(BeagleUIElement tree);
typedef ViewErrorListener = void Function(List<String> errors);

abstract class BeagleView {
  /// Subscribes [listener] to every change to the beagle tree. This method returns a function that,
  /// when called, undoes the subscription (removes the listener).
  RemoveListener subscribe(ViewUpdateListener listener);

  /// Subscribes [listener] to every error in the fetch and rendering process of a view. This method
  /// returns a function that, when called, undoes the subscription (removes the listener).
  RemoveListener addErrorListener(ViewErrorListener listener);

  /// Gets the renderer of the current BeagleView. Can be used to control the rendering directly.
  Renderer getRenderer();

  /// Gets a copy of the currently rendered tree.
  BeagleUIElement getTree();

  /// Gets the navigator of the Beagle View.
  BeagleNavigator getNavigator();

  /// Destroys the current view. Should be used when the BeagleView won't be used anymore. Avoids
  /// memory leaks and calls to objects that don't exist any longer.
  void destroy();
}
