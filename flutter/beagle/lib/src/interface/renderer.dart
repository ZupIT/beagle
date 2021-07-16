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

import 'package:beagle/beagle.dart';

abstract class Renderer {
  /// Does a partial render to the BeagleView. Compared to the full render, it will skip every step
  /// until the view snapshot, i.e, it will start by taking the view snapshot and end doing a render
  /// to the screen. Useful when updating components that have already been rendered once. If any
  /// component in [tree] haven't been rendered before, you should use `doFullRender` instead.
  ///
  /// To see the full documentation of the renderization process, please follow this link:
  /// https://github.com/ZupIT/beagle-web-core/blob/master/docs/renderization.md
  ///
  /// The parameter [tree] is the new tree to render, it can be just a new branch to add to the
  /// current tree or the entire tree. If it's the entire tree, the second and third parameter can
  /// be ommited.
  /// If you wish to update a branch of the tree, you must specify the second parameter [anchor]
  /// which is the id of the element in the current tree to add the new branch ([tree]) to.
  /// The third parameter [mode] tells Beagle how to add this tree to [anchor]. Prepend adds the new
  /// element to the start of the list of children. Append ads the children at the end. Replace
  /// remove all current children and adds [tree] as the only child. ReplaceComponent will replace
  /// the element identified by [anchor] entirely by [tree]. Default is replaceComponent.
  void doPartialRender(BeagleUIElement tree, [String anchor, TreeUpdateMode mode]);

  /// Does a full render to the BeagleView. A full render means that every renderization step will
  /// be executed for the [tree] passed as parameter. If the components in [tree] have been rendered
  /// at least once, you can help performance by calling `doPartialRender` instead.
  ///
  /// To see the full documentation of the renderization process, please follow this link:
  /// https://github.com/ZupIT/beagle-web-core/blob/master/docs/renderization.md
  ///
  /// The parameter [tree] is the new tree to render, it can be just a new branch to add to the
  /// current tree or the entire tree. If it's the entire tree, the second and third parameter can
  /// be ommited.
  /// If you wish to update a branch of the tree, you must specify the second parameter [anchor]
  /// which is the id of the element in the current tree to add the new branch ([tree]) to.
  /// The third parameter [mode] tells Beagle how to add this tree to [anchor]. Prepend adds the new
  /// element to the start of the list of children. Append ads the children at the end. Replace
  /// remove all current children and adds [tree] as the only child. ReplaceComponent will replace
  /// the element identified by [anchor] entirely by [tree]. Default is replaceComponent.
  void doFullRender(BeagleUIElement tree, [String anchor, TreeUpdateMode mode]);
}
