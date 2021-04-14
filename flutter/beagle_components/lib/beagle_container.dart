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

import 'package:beagle/model/beagle_style.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle/style/beagle_style_widget.dart';
import 'package:flutter/widgets.dart';

class BeagleContainer extends StatefulWidget with StyleWidget {
  const BeagleContainer({
    Key key,
    this.context,
    this.style,
    this.onInit,
    this.children,
  }) : super(key: key);

  final DataContext context;
  final BeagleStyle style;
  final Function onInit;
  final List<Widget> children;

  @override
  _BeagleContainerState createState() => _BeagleContainerState();
}

class _BeagleContainerState extends State<BeagleContainer> {
  @override
  void initState() {
    if (widget.onInit != null) {
      widget.onInit();
    }
    super.initState();
  }

  @override
  Widget build(BuildContext context) => buildBeagleWidget(
    style: widget.style,
    children: widget.children,
  );
}
