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

import 'package:beagle/beagle_initializer.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle/model/route.dart';
import 'package:beagle/utils/style.dart';
import 'package:flutter/material.dart';

typedef OnCreateViewListener = void Function(BeagleView view);

class BeagleRemoteView extends StatefulWidget {
  const BeagleRemoteView({Key key, this.route, this.onCreateView})
      : super(key: key);

  final String route;
  final OnCreateViewListener onCreateView;

  @override
  _BeagleRemoteView createState() => _BeagleRemoteView();
}

class _BeagleRemoteView extends State<BeagleRemoteView> {
  BeagleUIElement currentTree;
  BeagleView _view;

  @override
  void initState() {
    super.initState();
    _view = BeagleInitializer.getService().createView();
    if (widget.onCreateView != null) {
      widget.onCreateView(_view);
    }
    _view.subscribe((tree) {
      setState(() {
        currentTree = tree;
      });
    });
    if (widget.route != null && widget.route.isNotEmpty) {
      _view.getNavigator().pushView(RemoteView(widget.route));
    }
  }

  Widget buildViewFromTree(BeagleUIElement tree) {
    final widgetChildren = tree.getChildren().map(buildViewFromTree).toList();
    final builder = BeagleInitializer.getService().components[tree.getType()];
    if (builder == null) {
      debugPrint("Can't find builder for component ${tree.getType()}");
      return Container();
    }
    return builder(tree, widgetChildren).applyStyle(style: tree.getStyle());
  }

  @override
  Widget build(BuildContext context) =>
      currentTree == null ? Container() : buildViewFromTree(currentTree);
}
