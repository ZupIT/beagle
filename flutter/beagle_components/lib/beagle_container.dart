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
import 'package:flutter/widgets.dart';

class BeagleContainer extends StatefulWidget {
  const BeagleContainer({
    Key key,
    this.children,
    this.context,
    this.onInit,
    this.style,
  }) : super(key: key);

  final List<Widget> children;
  final DataContext context;
  final Function onInit;
  final BeagleStyle style;

  @override
  _BeagleContainerState createState() => _BeagleContainerState();
}

class _BeagleContainerState extends State<BeagleContainer> {
  @override
  void initState() {
    super.initState();
    if (widget.onInit != null) {
      widget.onInit();
    }
  }

  @override
  Widget build(BuildContext context) {
    Widget childContainer;

    //todo check if any child is positionType.ABSOLUTE and remove it
    if (widget.style != null && widget.style.flex != null) {
      switch (widget.style.flex.flexDirection) {
        case FlexDirection.COLUMN:
          childContainer = Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: widget.children);
          break;
        case FlexDirection.ROW:
          childContainer = Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: widget.children);
          break;
        case FlexDirection.COLUMN_REVERSE:
          childContainer = Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            verticalDirection: VerticalDirection.up,
            children: widget.children,
          );
          break;
        case FlexDirection.ROW_REVERSE:
          childContainer = Row(
            mainAxisAlignment: MainAxisAlignment.end,
            crossAxisAlignment: CrossAxisAlignment.end,
            children: widget.children,
          );
          break;
      }
      return childContainer;
    } else {
      return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: widget.children);
    }
  }
}
