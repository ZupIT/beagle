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

import 'package:beagle/model/beagle_button_style.dart';
import 'package:beagle/service_locator.dart';
import 'package:beagle/setup/beagle_design_system.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'dart:developer';

/// Defines a container that makes its content scrollable
class BeagleScrollView extends StatelessWidget {
  const BeagleScrollView({
    Key key,
    this.scrollDirection,
    this.scrollBarEnabled,
    this.children,
  }) : super(key: key);

  /// Define the button text content.
  final ScrollAxis scrollDirection;

  /// References a [BeagleButtonStyle] declared natively and locally in [BeagleDesignSystem]
  /// to be applied to this widget.
  final bool scrollBarEnabled;

  /// The content of the scroll view
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    final scrollView = ListView(
      scrollDirection: scrollDirection == ScrollAxis.HORIZONTAL ? Axis.horizontal : Axis.vertical,
      children: children,
    );
    return scrollBarEnabled == false ? scrollView : Scrollbar(child: scrollView);
  }
}

enum ScrollAxis {
  /// Creates a horizontal scroll bar
  HORIZONTAL,

  /// Creates a vertical scroll bar
  VERTICAL
}
