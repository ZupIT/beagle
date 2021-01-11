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
import 'package:beagle/utils/flex.dart';
import 'package:flutter/widgets.dart';

extension ApplyStyle on Widget {
  Widget applyStyle({beagle.BeagleStyle style}) {
    return style != null ? _buildWidget(style, this) : this;
  }
}

Widget _buildWidget(
  beagle.BeagleStyle style,
  Widget widget,
) =>
    // todo implement PositionType.ABSOLUTE attribute behavior
    _isAbsolute(style)
        ? Expanded(
            child: Stack(
              children: [
                Positioned.fill(child: _buildLayoutBuilder(style, widget))
              ],
            ),
          )
        : _buildLayoutBuilder(style, widget);

bool _isAbsolute(beagle.BeagleStyle style) =>
    style.positionType == beagle.FlexPosition.ABSOLUTE;

LayoutBuilder _buildLayoutBuilder(beagle.BeagleStyle style, Widget widget) {
  return LayoutBuilder(
      builder: (BuildContext context, BoxConstraints constraints) {
    final container = Container(
      width: _mapWidth(style, constraints),
      height: _mapHeight(style, constraints),
      margin: _mapEdgeValue(style.margin, constraints.maxWidth),
      padding: _mapEdgeValue(style.padding, constraints.maxWidth),
      constraints: _mapConstraints(style, constraints),
      decoration: _mapDecoration(style),
      child: _mapChild(style, widget),
    );
    return _buildCurrent(style, container, constraints);
  });
}

num _mapWidth(
  beagle.BeagleStyle style,
  BoxConstraints constraints,
) =>
    !_hasAspectRatio(style) && style?.size?.width != null
        ? _pickRealOrPercent(style?.size?.width, constraints.maxWidth)
        : null;

bool _hasAspectRatio(beagle.BeagleStyle style) =>
    style.size != null && style.size.aspectRatio != null;

num _mapHeight(
  beagle.BeagleStyle style,
  BoxConstraints constraints,
) =>
    style?.size?.height != null
        ? _pickRealOrPercent(style?.size?.height, constraints.maxHeight)
        : null;

BoxConstraints _mapConstraints(
  beagle.BeagleStyle style,
  BoxConstraints constraints,
) =>
    BoxConstraints(
      minWidth: style?.size?.minWidth != null
          ? _pickRealOrPercent(style?.size?.minWidth, constraints.maxWidth)
          : 0.0,
      maxWidth: style?.size?.maxWidth != null
          ? _pickRealOrPercent(style?.size?.maxWidth, constraints.maxWidth)
          : double.infinity,
      minHeight: style?.size?.minHeight != null
          ? _pickRealOrPercent(style?.size?.minHeight, constraints.maxHeight)
          : 0.0,
      maxHeight: style?.size?.maxHeight != null
          ? _pickRealOrPercent(style?.size?.maxHeight, constraints.maxHeight)
          : double.infinity,
    );

BoxDecoration _mapDecoration(
  beagle.BeagleStyle style,
) =>
    BoxDecoration(
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

Widget _mapChild(beagle.BeagleStyle style, Widget widget) {
  final child = style?.flex != null ? applyFlex(style.flex, widget) : widget;
  return _hasAspectRatio(style)
      ? AspectRatio(
          aspectRatio: style.size.aspectRatio.toDouble(),
          child: child,
        )
      : child;
}

Widget _buildCurrent(
  beagle.BeagleStyle style,
  Widget container,
  BoxConstraints constraints,
) {
  var current = container;
  // todo implement Position attribute behavior
  current = _hasPosition(style)
      ? Padding(
          padding: EdgeInsets.only(
            left: _getLeft(style.position, constraints.maxWidth),
            top: _getTop(style.position, constraints.maxHeight),
            right: _getRight(style.position, constraints.maxWidth),
            bottom: _getBottom(style.position, constraints.maxHeight),
          ),
          child: current,
        )
      : current;
  current = _isDisplayNone(style)
      ? Visibility(visible: false, child: current)
      : current;
  return current;
}

bool _isDisplayNone(beagle.BeagleStyle style) =>
    !(style.display != null && style.display == beagle.FlexDisplay.FLEX);

bool _hasPosition(beagle.BeagleStyle style) => style.position != null;

EdgeInsets _mapEdgeValue(beagle.EdgeValue edgeValue, double maxWidth) {
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
