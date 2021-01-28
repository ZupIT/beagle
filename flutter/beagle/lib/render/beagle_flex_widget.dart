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
import 'package:beagle/render/beagle_layout.dart';
import 'package:beagle/render/beagle_style_widget.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';

class BeagleFlexWidget extends MultiChildRenderObjectWidget {
  BeagleFlexWidget({
    Key key,
    this.flex,
    List<Widget> children = const <Widget>[],
  })  : _direction = _getDirection(
          flexDirection: flex?.flexDirection,
        ),
        _mainAxisSize = _getMainAxisSize(
          flexDirection: flex?.flexDirection,
        ),
        _mainAxisAlignment = _getMainAxisAlignment(
          justifyContent: flex?.justifyContent,
        ),
        _crossAxisAlignment = _getCrossAxisAlignment(
          flexDirection: flex?.flexDirection,
          alignItems: flex?.alignItems,
        ),
        _textDirection = _getTextDirection(
          flexDirection: flex?.flexDirection,
        ),
        _verticalDirection = _getVerticalDirection(
          flexDirection: flex?.flexDirection,
          flexWrap: flex?.flexWrap,
        ),
        _alignment = _getAlignment(
          justifyContent: flex?.justifyContent,
        ),
        _runAlignment = _getRunAlignment(
          alignContent: flex?.alignContent,
        ),
        super(key: key, children: _mapChildren(children));

  final beagle.Flex flex;

  // shared properties
  final Axis _direction;
  final TextDirection _textDirection;
  final VerticalDirection _verticalDirection;

  // flex exclusive properties
  final MainAxisSize _mainAxisSize;
  final MainAxisAlignment _mainAxisAlignment;
  final CrossAxisAlignment _crossAxisAlignment;

  // wrap exclusive properties
  final WrapAlignment _alignment;
  final WrapAlignment _runAlignment;

  static List<Widget> _mapChildren(List<Widget> children) {
    final list = children;
    return list.map((child) {
      if (child is BeagleStyleWidget && child.style?.flex != null) {
        if (child.style.flex.grow > 0) {
          return BeagleExpanded(child: child);
        } else {
          return BeagleFlexible(child: child);
        }
      } else {
        return child;
      }
    }).toList();
  }

  @override
  RenderObject createRenderObject(BuildContext context) => RenderWrapFlex(
        direction: _direction,
        mainAxisSize: _mainAxisSize,
        mainAxisAlignment: _mainAxisAlignment,
        crossAxisAlignment: _crossAxisAlignment,
        alignment: _alignment,
        runAlignment: _runAlignment,
        textDirection: _getEffectiveTextDirection(context),
        verticalDirection: _verticalDirection,
        flexWrap: flex?.flexWrap,
      );

  @override
  void updateRenderObject(
    BuildContext context,
    covariant RenderWrapFlex renderObject,
  ) {
    renderObject
      ..direction = _direction
      ..mainAxisAlignment = _mainAxisAlignment
      ..mainAxisSize = _mainAxisSize
      ..crossAxisAlignment = _crossAxisAlignment
      ..alignment = _alignment
      ..runAlignment = _runAlignment
      ..textDirection = _getEffectiveTextDirection(context)
      ..verticalDirection = _verticalDirection;
  }

  static Axis _getDirection({beagle.FlexDirection flexDirection}) =>
      (flexDirection == beagle.FlexDirection.ROW ||
              flexDirection == beagle.FlexDirection.ROW_REVERSE)
          ? Axis.horizontal
          : Axis.vertical;

  static MainAxisSize _getMainAxisSize({beagle.FlexDirection flexDirection}) =>
      (flexDirection == beagle.FlexDirection.ROW ||
              flexDirection == beagle.FlexDirection.ROW_REVERSE)
          ? MainAxisSize.max
          : MainAxisSize.min;

  static MainAxisAlignment _getMainAxisAlignment(
      {beagle.JustifyContent justifyContent}) {
    switch (justifyContent) {
      case beagle.JustifyContent.FLEX_START:
        return MainAxisAlignment.start;
      case beagle.JustifyContent.CENTER:
        return MainAxisAlignment.center;
      case beagle.JustifyContent.FLEX_END:
        return MainAxisAlignment.end;
      case beagle.JustifyContent.SPACE_BETWEEN:
        return MainAxisAlignment.spaceBetween;
      case beagle.JustifyContent.SPACE_AROUND:
        return MainAxisAlignment.spaceAround;
      case beagle.JustifyContent.SPACE_EVENLY:
        return MainAxisAlignment.spaceEvenly;
    }
    return MainAxisAlignment.start;
  }

  static CrossAxisAlignment _getCrossAxisAlignment({
    beagle.FlexDirection flexDirection,
    beagle.AlignItems alignItems,
  }) {
    switch (alignItems) {
      case beagle.AlignItems.FLEX_START:
        return CrossAxisAlignment.start;
      case beagle.AlignItems.CENTER:
        return CrossAxisAlignment.center;
      case beagle.AlignItems.FLEX_END:
        return CrossAxisAlignment.end;
      case beagle.AlignItems.BASELINE:
        return CrossAxisAlignment.baseline;
      case beagle.AlignItems.STRETCH:
        return CrossAxisAlignment.stretch;
    }
    return (flexDirection == beagle.FlexDirection.ROW ||
            flexDirection == beagle.FlexDirection.ROW_REVERSE)
        ? CrossAxisAlignment.start
        : CrossAxisAlignment.stretch;
  }

  static TextDirection _getTextDirection(
          {beagle.FlexDirection flexDirection}) =>
      flexDirection == beagle.FlexDirection.ROW_REVERSE
          ? TextDirection.rtl
          : null;

  TextDirection _getEffectiveTextDirection(BuildContext context) {
    return _textDirection ??
        (_needTextDirection ? Directionality.of(context) : null);
  }

  bool get _needTextDirection {
    switch (_direction) {
      case Axis.horizontal:
        return true;
      case Axis.vertical:
        return _crossAxisAlignment == CrossAxisAlignment.start ||
            _crossAxisAlignment == CrossAxisAlignment.end;
    }
    return null;
  }

  static VerticalDirection _getVerticalDirection(
          {beagle.FlexDirection flexDirection, beagle.FlexWrap flexWrap}) =>
      (flexDirection == beagle.FlexDirection.COLUMN_REVERSE ||
              flexWrap == beagle.FlexWrap.WRAP_REVERSE)
          ? VerticalDirection.up
          : VerticalDirection.down;

  static WrapAlignment _getAlignment({beagle.JustifyContent justifyContent}) {
    switch (justifyContent) {
      case beagle.JustifyContent.FLEX_START:
        return WrapAlignment.start;
      case beagle.JustifyContent.CENTER:
        return WrapAlignment.center;
      case beagle.JustifyContent.FLEX_END:
        return WrapAlignment.end;
      case beagle.JustifyContent.SPACE_BETWEEN:
        return WrapAlignment.spaceBetween;
      case beagle.JustifyContent.SPACE_AROUND:
        return WrapAlignment.spaceAround;
      case beagle.JustifyContent.SPACE_EVENLY:
        return WrapAlignment.spaceEvenly;
    }
    return WrapAlignment.start;
  }

  static WrapAlignment _getRunAlignment({beagle.AlignContent alignContent}) {
    switch (alignContent) {
      case beagle.AlignContent.FLEX_START:
        return WrapAlignment.start;
      case beagle.AlignContent.CENTER:
        return WrapAlignment.center;
      case beagle.AlignContent.FLEX_END:
        return WrapAlignment.end;
      case beagle.AlignContent.SPACE_BETWEEN:
        return WrapAlignment.spaceBetween;
      case beagle.AlignContent.SPACE_AROUND:
        return WrapAlignment.spaceAround;
      case beagle.AlignContent.STRETCH:
        //todo implement stretch attribute
        return WrapAlignment.spaceEvenly;
    }
    return WrapAlignment.start;
  }
}
