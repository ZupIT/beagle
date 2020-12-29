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

import 'package:beagle/action/beagle_alert.dart';
import 'package:beagle/beagle_initializer.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:flutter/material.dart';

class BeagleRemoteView extends StatefulWidget {
  const BeagleRemoteView({Key key, this.route}) : super(key: key);

  final String route;

  @override
  _BeagleRemoteView createState() => _BeagleRemoteView();
}

class _BeagleRemoteView extends State<BeagleRemoteView> {
  BeagleUIElement currentTree;
  BeagleView _view;

  @override
  void initState() {
    super.initState();
    _view = BeagleInitializer.service.createView(route: widget.route);
    // ignore: cascade_invocations
    _view.subscribe((tree) {
      setState(() {
        currentTree = tree;
      });
    });
  }

  Widget buildViewFromTree(BeagleUIElement tree) {
    final widgetChildren = tree.getChildren().map(buildViewFromTree).toList();
    final builder = BeagleInitializer.service.components[tree.getType()];
    if (builder == null) {
      debugPrint("Can't find builder for component ${tree.getType()}");
      return Container();
    }
    return builder(tree, widgetChildren);
  }

  @override
  Widget build(BuildContext context) {
    return BeagleAlert(
        child:
            currentTree == null ? Container() : buildViewFromTree(currentTree));
  }
}
