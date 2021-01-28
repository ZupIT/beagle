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
import 'package:beagle/render/beagle_flex_widget.dart';
import 'package:beagle/utils/alignment.dart';
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
    // todo all this logic will be extracted to work with other components
    final relatives = <Widget>[];
    final absolutes = <Align>[];
    final flex = widget.style?.flex;

    for (final child in widget.children) {
      if (child is Padding) {
        absolutes.add(
          Align(
            alignment: getAlignment(child.padding, flex),
            child: child.child,
          ),
        );
      } else {
        relatives.add(child);
      }
    }
    return BeagleFlexWidget(flex: flex, children: widget.children);
    // return absolutes.isNotEmpty
    //     ? applyFlexDirection([
    //         Expanded(
    //           child: Stack(
    //             fit: widget.style?.size?.height != null
    //                 ? StackFit.expand
    //                 : StackFit.loose,
    //             children: [
    //               applyFlexDirection(relatives, flex),
    //               ...absolutes,
    //             ],
    //           ),
    //         ),
    //       ], flex)
    //     : applyFlexDirection(relatives, flex);
  }
}

// final custom = CustomSingleChildLayout(delegate: Delegate());
// class Delegate extends MultiChildLayoutDelegate {
//   @override
//   void performLayout(Size size) {
//     // TODO: implement performLayout
//   }
//
//   @override
//   bool shouldRelayout(covariant MultiChildLayoutDelegate oldDelegate) {
//     // TODO: implement shouldRelayout
//     throw UnimplementedError();
//   }
//
//
// }