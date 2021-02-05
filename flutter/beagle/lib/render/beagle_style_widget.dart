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
import 'package:beagle/render/layout_builder.dart' as beagle;
import 'package:beagle/utils/color.dart';
import 'package:flutter/widgets.dart';

class BeagleStyleWidget extends StatelessWidget {
  const BeagleStyleWidget({
    Key key,
    this.style,
    this.child,
    this.children,
  }) : super(key: key);

  final BeagleStyle style;
  final Widget child;
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    return style != null
        ? _buildWidget(style, child: child, children: children)
        : child;
  }

  Widget _buildWidget(
    BeagleStyle style, {
    Widget child,
    List<Widget> children,
  }) {
    if (_isDisplayNone(style)) {
      return Container();
    } else {
      // todo apply position property to relative views
      final builder =
          _buildLayoutBuilder(style, child: child, children: children);
      return _isAbsolute(style) ? _buildPadding(style, builder) : builder;
    }
  }

  bool _isDisplayNone(BeagleStyle style) =>
      !(style.display != null && style.display == FlexDisplay.FLEX);

  bool _isAbsolute(BeagleStyle style) =>
      style.positionType == FlexPosition.ABSOLUTE;

  beagle.LayoutBuilder _buildLayoutBuilder(
    BeagleStyle style, {
    Widget child,
    List<Widget> children,
  }) {
    return beagle.LayoutBuilder(
        builder: (BuildContext context, BoxConstraints constraints) {
      return _buildCurrent(
        style,
        constraints,
        child: child,
        children: children,
      );
    });
  }

  Widget _buildCurrent(
    BeagleStyle style,
    BoxConstraints constraints, {
    Widget child,
    List<Widget> children,
  }) {
    var current = child;

    if (_hasDecoration(style)) {
      current = DecoratedBox(
        decoration: _mapDecoration(style),
        child: current,
      );
    }

    if (_hasAspectRatio(style)) {
      current = AspectRatio(
        aspectRatio: style.size.aspectRatio.toDouble(),
        child: current,
      );
    }

    if (style.size != null) {
      BoxConstraints boxConstraints;
      if (style.size.width != null || style.size.height != null) {
        boxConstraints = BoxConstraints.tightFor(
          width: _mapWidth(style, constraints),
          height: _mapHeight(style, constraints),
        );
      } else {
        boxConstraints = _mapConstraints(style, constraints);
      }
      current = ConstrainedBox(constraints: boxConstraints, child: current);
    }

    if (style.padding != null) {
      current = Padding(
        padding: _mapEdgeValue(style.padding, constraints.maxWidth),
        child: current,
      );
    }

    if (_hasPosition(style)) {
      current = Padding(
        padding: EdgeInsets.fromLTRB(
          _getLeft(style.position, maxConstraint: constraints.maxWidth),
          _getTop(style.position, maxConstraint: constraints.maxHeight),
          _getRight(style.position, maxConstraint: constraints.maxWidth),
          _getBottom(style.position, maxConstraint: constraints.maxHeight),
        ),
        child: current,
      );
    }

    return current;
  }

  bool _hasDecoration(BeagleStyle style) =>
      style.backgroundColor != null ||
      style.borderColor != null ||
      style.borderWidth != null ||
      style.cornerRadius != null;

  bool _hasAspectRatio(BeagleStyle style) =>
      style.size != null && style.size.aspectRatio != null;

  bool _hasPosition(BeagleStyle style) => style.position != null;

  Padding _buildPadding(BeagleStyle style, beagle.LayoutBuilder builder) => Padding(
        padding: EdgeInsets.fromLTRB(
          _getLeft(style.position, nullable: true),
          _getTop(style.position, nullable: true),
          _getRight(style.position, nullable: true),
          _getBottom(style.position, nullable: true),
        ),
        child: builder,
      );

  num _mapWidth(BeagleStyle style, BoxConstraints constraints) =>
      !_hasAspectRatio(style) && style?.size?.width != null
          ? _pickRealOrPercent(style?.size?.width, constraints.maxWidth)
          : null;

  num _mapHeight(BeagleStyle style, BoxConstraints constraints) =>
      style?.size?.height != null
          ? _pickRealOrPercent(style?.size?.height, constraints.maxHeight)
          : null;

  BoxConstraints _mapConstraints(
    BeagleStyle style,
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
        borderRadius: BorderRadius.circular(
            style?.cornerRadius?.radius?.toDouble() ?? 0.0),
      );

  EdgeInsets _mapEdgeValue(EdgeValue edgeValue, double maxWidth) {
    if (edgeValue != null) {
      if (edgeValue.all != null) {
        return edgeValue.all.type == UnitType.REAL
            ? EdgeInsets.all(_getAll(edgeValue))
            : EdgeInsets.symmetric(
                vertical: _getAll(edgeValue) * maxWidth,
                horizontal: _getAll(edgeValue) * maxWidth);
      } else {
        return EdgeInsets.fromLTRB(
          _getLeft(edgeValue, maxConstraint: maxWidth),
          _getTop(edgeValue, maxConstraint: maxWidth),
          _getRight(edgeValue, maxConstraint: maxWidth),
          _getBottom(edgeValue, maxConstraint: maxWidth),
        );
      }
    } else {
      return null;
    }
  }

  num _getAll(EdgeValue edgeValue) => edgeValue.all.type == UnitType.REAL
      ? edgeValue.all.value.toDouble()
      : edgeValue.all.value / 100;

  num _getLeft(EdgeValue edgeValue,
      {double maxConstraint, bool nullable = false}) {
    var left = UnitValue(value: 0, type: UnitType.REAL);
    if (edgeValue.horizontal != null) {
      left = edgeValue.horizontal;
    } else if (edgeValue.start != null) {
      left = edgeValue.start;
    } else if (edgeValue.left != null) {
      left = edgeValue.left;
    } else if (nullable) {
      return null;
    }
    return _pickRealOrPercent(left, maxConstraint);
  }

  num _getTop(EdgeValue edgeValue,
      {double maxConstraint, bool nullable = false}) {
    var top = UnitValue(value: 0, type: UnitType.REAL);
    if (edgeValue.vertical != null) {
      top = edgeValue.vertical;
    } else if (edgeValue.top != null) {
      top = edgeValue.top;
    } else if (nullable) {
      return null;
    }
    return _pickRealOrPercent(top, maxConstraint);
  }

  num _getRight(EdgeValue edgeValue,
      {double maxConstraint, bool nullable = false}) {
    var right = UnitValue(value: 0, type: UnitType.REAL);
    if (edgeValue.horizontal != null) {
      right = edgeValue.horizontal;
    } else if (edgeValue.end != null) {
      right = edgeValue.end;
    } else if (edgeValue.right != null) {
      right = edgeValue.right;
    } else if (nullable) {
      return null;
    }
    return _pickRealOrPercent(right, maxConstraint);
  }

  num _getBottom(EdgeValue edgeValue,
      {double maxConstraint, bool nullable = false}) {
    var bottom = UnitValue(value: 0, type: UnitType.REAL);
    if (edgeValue.vertical != null) {
      bottom = edgeValue.vertical;
    } else if (edgeValue.bottom != null) {
      bottom = edgeValue.bottom;
    } else if (nullable) {
      return null;
    }
    return _pickRealOrPercent(bottom, maxConstraint);
  }

  num _pickRealOrPercent(UnitValue unitValue, [double maxConstraint]) =>
      unitValue.type == UnitType.REAL
          ? unitValue.value.toDouble()
          : (unitValue.value / 100) * (maxConstraint ?? 100);
}
