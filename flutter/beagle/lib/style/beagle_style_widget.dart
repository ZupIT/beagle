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
import 'package:beagle/style/style_mapper.dart';
import 'package:beagle/utils/color.dart';
import 'package:flutter/widgets.dart';
import 'package:yoga_engine/yoga_engine.dart';

mixin StyleWidget {}

Widget buildBeagleWidget({
  BeagleStyle style,
  Widget child,
  List<Widget> children,
}) {
  final yogaNode = mapToYogaNode(style);
  if (children != null && children.isNotEmpty) {
    return style != null
        ? _buildWidget(
            style,
            YogaTree(yogaNode: yogaNode, children: children),
          )
        : YogaTree(yogaNode: yogaNode, children: children);
  } else {
    return YogaLeaf(yogaNode: yogaNode, child: _buildWidget(style, child));
  }
}

Widget prepareYogaLeaf({
  Key key,
  BeagleStyle style,
  Widget child,
}) =>
    YogaLeaf(
      key: key,
      yogaNode: mapToYogaNode(style),
      child: child,
    );

Widget _buildWidget(BeagleStyle style, Widget child) => _hasDecoration(style)
    ? DecoratedBox(decoration: _mapDecoration(style), child: child)
    : child;

bool _hasDecoration(BeagleStyle style) =>
    style?.backgroundColor != null ||
    style?.borderColor != null ||
    style?.borderWidth != null ||
    style?.cornerRadius != null;

BoxDecoration _mapDecoration(BeagleStyle style) => BoxDecoration(
      color: style?.backgroundColor != null
          ? HexColor(style.backgroundColor)
          : null,
      border: (style?.borderColor != null && style?.borderWidth != null)
          ? Border.all(
              color: HexColor(style.borderColor),
              width: style.borderWidth.toDouble(),
            )
          : null,
      borderRadius:
          BorderRadius.circular(style?.cornerRadius?.radius?.toDouble() ?? 0.0),
    );
