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
import 'package:beagle/utils/color.dart';
import 'package:flutter/widgets.dart';

extension ApplyStyle on Widget {
  Widget applyStyle(beagle.BeagleStyle style) {
    return LayoutBuilder(
        builder: (BuildContext context, BoxConstraints constraints) {
          final container = Container(
            width: !_hasAspectRatio(style) && style?.size?.width != null
                ? _pickRealOrPercent(style?.size?.width, constraints.maxWidth)
                : null,
            height: style?.size?.height != null
                ? _pickRealOrPercent(style?.size?.height, constraints.maxHeight)
                : null,
            margin: mapEdgeValue(style.margin, constraints.maxWidth),
            padding: mapEdgeValue(style.padding, constraints.maxWidth),
            constraints: BoxConstraints(
              minWidth: style?.size?.minWidth != null
                  ? _pickRealOrPercent(style?.size?.minWidth, constraints.maxWidth)
                  : 0.0,
              maxWidth: style?.size?.maxWidth != null
                  ? _pickRealOrPercent(style?.size?.maxWidth, constraints.maxWidth)
                  : double.infinity,
              minHeight: style?.size?.minHeight != null
                  ? _pickRealOrPercent(
                  style?.size?.minHeight, constraints.maxHeight)
                  : 0.0,
              maxHeight: style?.size?.maxHeight != null
                  ? _pickRealOrPercent(
                  style?.size?.maxHeight, constraints.maxHeight)
                  : double.infinity,
            ),
            decoration: BoxDecoration(
              color: style?.backgroundColor != null
                  ? HexColor(style.backgroundColor)
                  : null,
              border: (style?.borderColor != null && style?.borderWidth != null)
                  ? Border.all(
                color: HexColor(style.borderColor),
                width: style.borderWidth.toDouble(),
              )
                  : null,
              borderRadius: BorderRadius.circular(
                  style?.cornerRadius?.radius?.toDouble() ?? 0.0),
            ),
            child: _hasAspectRatio(style)
                ? AspectRatio(
                aspectRatio: style.size.aspectRatio.toDouble(), child: this)
                : this,
          );
          return _buildCurrent(style, container, constraints);
        });
  }
}

Widget _buildCurrent(
    beagle.BeagleStyle style, Widget container, BoxConstraints constraints) {
  return _hasPosition(style)
      ? Positioned(
      left: _getLeft(style.position, constraints.maxWidth),
      top: _getTop(style.position, constraints.maxHeight),
      right: _getRight(style.position, constraints.maxWidth),
      bottom: _getBottom(style.position, constraints.maxHeight),
      child: container)
      : container;
}

bool _hasPosition(beagle.BeagleStyle style) => style.position != null;

bool _hasAspectRatio(beagle.BeagleStyle style) =>
    style.size != null && style.size.aspectRatio != null;

Widget applyFlex(Widget child, {beagle.Flex flex}) {
  return child;
}

EdgeInsets mapEdgeValue(beagle.EdgeValue edgeValue, double maxWidth) {
  if (edgeValue != null) {
    if (edgeValue.all != null) {
      return edgeValue.all.type == beagle.UnitType.REAL
          ? EdgeInsets.all(_getAll(edgeValue))
          : EdgeInsets.symmetric(
          vertical: _getAll(edgeValue) * maxWidth,
          horizontal: _getAll(edgeValue) * maxWidth);
    } else {
      return EdgeInsets.fromLTRB(
        _getLeft(edgeValue, maxWidth),
        _getTop(edgeValue, maxWidth),
        _getRight(edgeValue, maxWidth),
        _getBottom(edgeValue, maxWidth),
      );
    }
  } else {
    return null;
  }
}

num _getAll(beagle.EdgeValue edgeValue) {
  if (edgeValue.all != null) {
    return edgeValue.all.type == beagle.UnitType.REAL
        ? edgeValue.all.value.toDouble()
        : edgeValue.all.value / 100;
  } else {
    return 0;
  }
}

num _getLeft(beagle.EdgeValue edgeValue, double maxConstraint) {
  var left = beagle.UnitValue(value: 0, type: beagle.UnitType.REAL);
  if (edgeValue.horizontal != null) {
    left = edgeValue.horizontal;
  } else if (edgeValue.start != null) {
    left = edgeValue.start;
  } else if (edgeValue.left != null) {
    left = edgeValue.left;
  }
  return _pickRealOrPercent(left, maxConstraint);
}

num _getTop(beagle.EdgeValue edgeValue, double maxConstraint) {
  var top = beagle.UnitValue(value: 0, type: beagle.UnitType.REAL);
  if (edgeValue.vertical != null) {
    top = edgeValue.vertical;
  } else if (edgeValue.top != null) {
    top = edgeValue.top;
  }
  return _pickRealOrPercent(top, maxConstraint);
}

num _getRight(beagle.EdgeValue edgeValue, double maxConstraint) {
  var right = beagle.UnitValue(value: 0, type: beagle.UnitType.REAL);
  if (edgeValue.horizontal != null) {
    right = edgeValue.horizontal;
  } else if (edgeValue.end != null) {
    right = edgeValue.end;
  } else if (edgeValue.right != null) {
    right = edgeValue.right;
  }
  return _pickRealOrPercent(right, maxConstraint);
}

num _getBottom(beagle.EdgeValue edgeValue, double maxConstraint) {
  var bottom = beagle.UnitValue(value: 0, type: beagle.UnitType.REAL);
  if (edgeValue.vertical != null) {
    bottom = edgeValue.vertical;
  } else if (edgeValue.bottom != null) {
    bottom = edgeValue.bottom;
  }
  return _pickRealOrPercent(bottom, maxConstraint);
}

num _pickRealOrPercent(beagle.UnitValue unitValue, double maxConstraint) {
  if (unitValue != null) {
    return unitValue.type == beagle.UnitType.REAL
        ? unitValue.value.toDouble()
        : (unitValue.value / 100) * maxConstraint;
  } else {
    return 0.0;
  }
}
