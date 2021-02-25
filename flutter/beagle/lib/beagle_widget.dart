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

import 'dart:async';
import 'dart:convert';

import 'package:beagle/beagle_sdk.dart';
import 'package:beagle/components/beagle_undefined_widget.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle/model/route.dart';
import 'package:beagle/networking/beagle_screen_request.dart';
import 'package:flutter/widgets.dart';

typedef OnCreateViewListener = void Function(BeagleView view);

/// TODO: THE UNIT TEST WILL BE WRITE AFTER RESOLVE DEPENDENCY INJECTION
/// A widget that displays content of beagle.
class BeagleWidget extends StatefulWidget {
  const BeagleWidget({
    Key key,
    this.onCreateView,
    this.screenJson,
    this.controllerId,
    this.screenRequest,
  }) : super(key: key);

  /// that represents a local screen to be shown.
  final String screenJson;

  /// that represents a controllerId.
  final String controllerId;

  /// provides the url, method, headers and body to the request.
  final BeagleScreenRequest screenRequest;

  /// get a current BeagleView.
  final OnCreateViewListener onCreateView;

  @override
  _BeagleWidget createState() => _BeagleWidget();
}

class _BeagleWidget extends State<BeagleWidget> {
  BeagleView _view;
  Widget widgetState;

  @override
  void initState() {
    super.initState();

    _startBeagleView();
  }

  Future<void> _startBeagleView() async {
    await BeagleSdk.getService().start();
    _view = BeagleSdk.getService().createView(
      networkOptions: widget.screenRequest,
      initialControllerId: widget.controllerId,
    )..subscribe((tree) {
        final widgetLoaded = _buildViewFromTree(tree);
        setState(() {
          widgetState = widgetLoaded;
        });
      });

    if (widget.screenRequest != null) {
      await _view.getNavigator().pushView(RemoteView(widget.screenRequest.url));
    } else {
      await _view
          .getNavigator()
          .pushView(LocalView(BeagleUIElement(jsonDecode(widget.screenJson))));
    }
  }

  Widget _buildViewFromTree(BeagleUIElement tree) {
    final widgetChildren = tree.getChildren().map(_buildViewFromTree).toList();
    final builder = BeagleSdk.getService().components[tree.getType()];
    if (builder == null) {
      BeagleSdk.logger
          .error("Can't find builder for component ${tree.getType()}");
      return BeagleUndefinedWidget(
        environment: BeagleSdk.config.environment,
      );
    }
    return builder(tree, widgetChildren, _view);
  }

  @override
  Widget build(BuildContext context) {
    return widgetState ?? const SizedBox.shrink();
  }
}
