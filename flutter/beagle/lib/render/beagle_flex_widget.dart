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
        _textBaseline = TextBaseline.alphabetic,
        _alignment = _getAlignment(
          justifyContent: flex?.justifyContent,
        ),
        _runAlignment = _getRunAlignment(
          alignContent: flex?.alignContent,
        ),
        super(key: key, children: _mapChildren(children));

  final BeagleFlex flex;

  // shared properties
  final Axis _direction;
  final TextDirection _textDirection;
  final VerticalDirection _verticalDirection;
  final TextBaseline _textBaseline;
  final CrossAxisAlignment _crossAxisAlignment;

  // flex exclusive properties
  final MainAxisSize _mainAxisSize;
  final MainAxisAlignment _mainAxisAlignment;

  // wrap exclusive properties
  final WrapAlignment _alignment;
  final WrapAlignment _runAlignment;

  static List<Widget> _mapChildren(List<Widget> children) {
    final list = children;
    return list.map((child) {
      if (child is BeagleStyleWidget) {
        return BeagleFlexible(
          grow: child.style?.flex?.grow ?? 0.0,
          shrink: child.style?.flex?.shrink ?? 1.0,
          alignSelf: child.style?.flex?.alignSelf ?? AlignSelf.AUTO,
          positionType: child.style?.positionType ?? FlexPosition.RELATIVE,
          margin: child.style?.margin,
          child: child,
        );
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
        textBaseline: _textBaseline,
        verticalDirection: _verticalDirection,
        clipBehavior: Clip.hardEdge,
        flexWrap: flex?.flexWrap ?? FlexWrap.NO_WRAP,
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
      ..textBaseline = _textBaseline
      ..verticalDirection = _verticalDirection;
  }

  static Axis _getDirection({FlexDirection flexDirection}) =>
      (flexDirection == FlexDirection.ROW ||
              flexDirection == FlexDirection.ROW_REVERSE)
          ? Axis.horizontal
          : Axis.vertical;

  static MainAxisSize _getMainAxisSize({FlexDirection flexDirection}) =>
      (flexDirection == FlexDirection.ROW ||
              flexDirection == FlexDirection.ROW_REVERSE)
          ? MainAxisSize.max
          : MainAxisSize.min;

  static MainAxisAlignment _getMainAxisAlignment(
      {JustifyContent justifyContent}) {
    switch (justifyContent) {
      case JustifyContent.FLEX_START:
        return MainAxisAlignment.start;
      case JustifyContent.CENTER:
        return MainAxisAlignment.center;
      case JustifyContent.FLEX_END:
        return MainAxisAlignment.end;
      case JustifyContent.SPACE_BETWEEN:
        return MainAxisAlignment.spaceBetween;
      case JustifyContent.SPACE_AROUND:
        return MainAxisAlignment.spaceAround;
      case JustifyContent.SPACE_EVENLY:
        return MainAxisAlignment.spaceEvenly;
    }
    return MainAxisAlignment.start;
  }

  static CrossAxisAlignment _getCrossAxisAlignment({
    FlexDirection flexDirection,
    AlignItems alignItems,
  }) {
    switch (alignItems) {
      case AlignItems.FLEX_START:
        return CrossAxisAlignment.start;
      case AlignItems.CENTER:
        return CrossAxisAlignment.center;
      case AlignItems.FLEX_END:
        return CrossAxisAlignment.end;
      case AlignItems.BASELINE:
        return CrossAxisAlignment.baseline;
      case AlignItems.STRETCH:
        return CrossAxisAlignment.stretch;
    }
    return (flexDirection == FlexDirection.ROW ||
            flexDirection == FlexDirection.ROW_REVERSE)
        ? CrossAxisAlignment.start
        : CrossAxisAlignment.stretch;
  }

  static TextDirection _getTextDirection({FlexDirection flexDirection}) =>
      flexDirection == FlexDirection.ROW_REVERSE ? TextDirection.rtl : null;

  TextDirection _getEffectiveTextDirection(BuildContext context) {
    return _textDirection ?? Directionality.of(context);
  }

  static VerticalDirection _getVerticalDirection(
          {FlexDirection flexDirection, FlexWrap flexWrap}) =>
      (flexDirection == FlexDirection.COLUMN_REVERSE ||
              flexWrap == FlexWrap.WRAP_REVERSE)
          ? VerticalDirection.up
          : VerticalDirection.down;

  static WrapAlignment _getAlignment({JustifyContent justifyContent}) {
    switch (justifyContent) {
      case JustifyContent.FLEX_START:
        return WrapAlignment.start;
      case JustifyContent.CENTER:
        return WrapAlignment.center;
      case JustifyContent.FLEX_END:
        return WrapAlignment.end;
      case JustifyContent.SPACE_BETWEEN:
        return WrapAlignment.spaceBetween;
      case JustifyContent.SPACE_AROUND:
        return WrapAlignment.spaceAround;
      case JustifyContent.SPACE_EVENLY:
        return WrapAlignment.spaceEvenly;
    }
    return WrapAlignment.start;
  }

  static WrapAlignment _getRunAlignment({AlignContent alignContent}) {
    switch (alignContent) {
      case AlignContent.FLEX_START:
        return WrapAlignment.start;
      case AlignContent.CENTER:
        return WrapAlignment.center;
      case AlignContent.FLEX_END:
        return WrapAlignment.end;
      case AlignContent.SPACE_BETWEEN:
        return WrapAlignment.spaceBetween;
      case AlignContent.SPACE_AROUND:
        return WrapAlignment.spaceAround;
      case AlignContent.STRETCH:
        //todo implement stretch attribute
        return WrapAlignment.spaceEvenly;
    }
    return WrapAlignment.start;
  }
}
