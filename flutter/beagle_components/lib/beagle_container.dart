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

import 'package:beagle_components/after_layout.dart';
import 'package:flutter/material.dart';

/// A simple container that can execute an action as soon as it gets created
class BeagleContainer extends StatefulWidget {
  const BeagleContainer(
      {Key key,
      this.onInit,
      this.children})
      : super(key: key);

  /// optional function to run once the container is created
  final Function onInit;

  /// content of the container
  final List<Widget> children;

  @override
  _BeagleContainer createState() => _BeagleContainer();
}

class _BeagleContainer extends State<BeagleContainer> with AfterLayoutMixin<BeagleContainer> {
  @override
  void afterFirstLayout(BuildContext context) {
    if (widget.onInit != null) widget.onInit();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      key: widget.key,
      child: Column(children: widget.children),
    );
  }
}
