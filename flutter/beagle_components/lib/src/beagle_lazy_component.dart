/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import 'dart:convert';

import 'package:beagle/beagle.dart';
import 'package:flutter/material.dart';

import 'after_layout.dart';

/// Displays a Widget obtained from the network.
///
/// An [initialState] can be provided to present a placeholder Widget while
/// the component is being fetched.
class BeagleLazyComponent extends StatefulWidget {
  const BeagleLazyComponent(
      {Key key,
      this.path,
      this.initialState,
      this.beagleId,
      this.view,
      this.child})
      : super(key: key);

  /// An URL that can be either absolute or a relative path from Beagle's base
  /// url.
  final String path;

  /// An element that will be displayed while the component is being fetched.
  final BeagleUIElement initialState;

  /// [BeagleUIElement] id. Identifies this component on Beagle's components
  /// tree.
  final String beagleId;

  /// Used to access the render engine to re-render the component when the fetch
  /// ends.
  final BeagleView view;

  /// Initially, [BeagleLazyComponent] has no child. When loading ends, the
  /// loaded component becomes its child and [BeagleLazyComponent] is
  /// re-rendered.
  final Widget child;

  @override
  _BeagleLazyComponent createState() => _BeagleLazyComponent();
}

class _BeagleLazyComponent extends State<BeagleLazyComponent>
    with AfterLayoutMixin<BeagleLazyComponent> {
  final service = beagleServiceLocator<BeagleService>();

  String _buildUrl() {
    final urlBuilder = beagleServiceLocator<UrlBuilder>();
    return urlBuilder.build(widget.path);
  }

  Future<void> _fetchLazyView() async {
    try {
      final result =
          await service.httpClient.sendRequest(BeagleRequest(_buildUrl()));
      if (result.status >= 200 && result.status < 400) {
        final jsonMap = jsonDecode(result.body);
        final component = BeagleUIElement(jsonMap);
        widget.view
            .getRenderer()
            .doFullRender(component, widget.beagleId, TreeUpdateMode.replace);
      } else {
        beagleServiceLocator<BeagleLogger>().error(
            'BeagleLazyComponent: connection error: ${result.status} ${result.body}');
      }
    } catch (err) {
      beagleServiceLocator<BeagleLogger>()
          .error('BeagleLazyComponent: error: $err');
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
