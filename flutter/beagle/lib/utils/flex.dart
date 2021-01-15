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

import 'package:beagle/model/beagle_style.dart' as beagle;
import 'package:flutter/widgets.dart';

Widget applyFlex(beagle.Flex flex, Widget child) => child;

Widget applyFlexDirection(List<Widget> children, [beagle.Flex flex]) {
  if (flex != null) {
    Widget childContainer;
    switch (flex.flexDirection) {
      case beagle.FlexDirection.COLUMN:
        childContainer = Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: children,
        );
        break;
      case beagle.FlexDirection.ROW:
        childContainer = Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: children,
        );
        break;
      case beagle.FlexDirection.COLUMN_REVERSE:
        childContainer = Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          verticalDirection: VerticalDirection.up,
          children: children,
        );
        break;
      case beagle.FlexDirection.ROW_REVERSE:
        childContainer = Row(
          textDirection: TextDirection.rtl,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: children,
        );
        break;
    }
    return childContainer;
  } else {
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: children,
    );
  }
}

Alignment getAlignment(EdgeInsets padding, [beagle.Flex flex]) {
  final left = padding.left;
  final top = padding.top;
  final right = padding.right;
  final bottom = padding.bottom;

  var alignment = Alignment.topLeft;

  switch (flex?.flexDirection) {
    case beagle.FlexDirection.COLUMN:
    case beagle.FlexDirection.ROW:
      if (left != null && bottom != null) {
        alignment = Alignment.bottomLeft;
      } else if (top != null && right != null) {
        alignment = Alignment.topRight;
      } else if (right != null) {
        if (bottom != null) {
          alignment = Alignment.bottomRight;
        } else {
          alignment = Alignment.topRight;
        }
      } else if (bottom != null) {
        alignment = Alignment.bottomLeft;
      }
      break;
    case beagle.FlexDirection.COLUMN_REVERSE:
      alignment = Alignment.bottomLeft;
      if (left != null && top != null) {
        alignment = Alignment.topLeft;
      } else if (bottom != null && right != null) {
        alignment = Alignment.bottomRight;
      } else if (top != null) {
        if (right != null) {
          alignment = Alignment.topRight;
        } else {
          alignment = Alignment.topLeft;
        }
      } else if (right != null) {
        alignment = Alignment.bottomRight;
      }
      break;
    case beagle.FlexDirection.ROW_REVERSE:
      alignment = Alignment.topRight;
      if (right != null && bottom != null) {
        alignment = Alignment.bottomRight;
      } else if (top != null && left != null) {
        alignment = Alignment.topLeft;
      } else if (bottom != null) {
        if (left != null) {
          alignment = Alignment.bottomLeft;
        } else {
          alignment = Alignment.bottomRight;
        }
      } else if (left != null) {
        alignment = Alignment.topLeft;
      }
      break;
  }
  return alignment;
}
