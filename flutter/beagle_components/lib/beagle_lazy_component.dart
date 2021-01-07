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

import 'package:after_layout/after_layout.dart';
import 'package:beagle/beagle.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/model/request.dart';
import 'package:beagle/model/tree_update_mode.dart';
import 'package:flutter/material.dart';

class BeagleLazyComponent extends StatefulWidget {
  const BeagleLazyComponent(
      {Key key,
      this.path,
      this.initialState,
      this.beagleId,
      this.view,
      this.child})
      : super(key: key);

  final String path;
  final BeagleUIElement initialState;
  final String beagleId;
  final BeagleView view;
  final Widget child;

  @override
  _BeagleLazyComponent createState() => _BeagleLazyComponent();
}

class _BeagleLazyComponent extends State<BeagleLazyComponent>
    with AfterLayoutMixin<BeagleLazyComponent> {
  String _buildUrl() {
    return BeagleInitializer.getService().urlBuilder.build(widget.path);
  }

  Future<void> _fetchLazyView() async {
    try {
      final result = await BeagleInitializer.getService()
          .httpClient
          .sendRequest(Request(_buildUrl()));
      if (result.status >= 200 && result.status < 400) {
        final jsonMap = jsonDecode(result.body);
        final component = BeagleUIElement(jsonMap);
        widget.view
            .getRenderer()
            .doFullRender(component, widget.beagleId, TreeUpdateMode.replace);
      } else {
        debugPrint(
            'BeagleLazyComponent: connection error: ${result.status} ${result.body}');
      }
      // ignore: empty_catches
    } catch (err) {
      debugPrint('BeagleLazyComponent: error: $err');
    }
  }

  @override
  void afterFirstLayout(BuildContext context) {
    if (widget.initialState != null) {
      widget.view.getRenderer().doFullRender(
          widget.initialState, widget.beagleId, TreeUpdateMode.replace);
    }
    _fetchLazyView();
  }

  @override
  Widget build(BuildContext context) {
    return widget.child ?? Container();
  }
}
