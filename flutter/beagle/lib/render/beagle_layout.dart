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

import 'dart:math' as math;

import 'package:beagle/model/beagle_style.dart' as beagle;
import 'package:flutter/foundation.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';

typedef ChildSizingFunction = double Function(RenderBox child, double extent);

const double precisionErrorTolerance = 1e-10;

bool _startIsTopLeft(Axis direction,
    {TextDirection textDirection, VerticalDirection verticalDirection}) {
  assert(direction != null);
  switch (direction) {
    case Axis.horizontal:
      switch (textDirection) {
        case TextDirection.ltr:
          return true;
        case TextDirection.rtl:
          return false;
      }
      return null;
    case Axis.vertical:
      switch (verticalDirection) {
        case VerticalDirection.down:
          return true;
        case VerticalDirection.up:
          return false;
      }
      return null;
  }
  return null;
}

class _RunMetrics {
  _RunMetrics(
    this.mainAxisExtent,
    this.crossAxisExtent,
    this.childCount,
  );

  final double mainAxisExtent;
  final double crossAxisExtent;
  final int childCount;
}

class BeagleWrapFlexParentData extends ContainerBoxParentData<RenderBox> {
  int flex;
  FlexFit fit;
  beagle.AlignContent alignContent;
  beagle.FlexPosition flexPosition;

  int _runIndex = 0;
}

// class BeagleAlignSelf

class BeagleFlexible extends ParentDataWidget<BeagleWrapFlexParentData> {
  const BeagleFlexible({
    Key key,
    this.flex = 1,
    this.fit = FlexFit.loose,
    @required Widget child,
  }) : super(key: key, child: child);

  final int flex;

  final FlexFit fit;

  @override
  void applyParentData(RenderObject renderObject) {
    assert(renderObject.parentData is BeagleWrapFlexParentData);
    final parentData = renderObject.parentData as BeagleWrapFlexParentData;
    var needsLayout = false;

    if (parentData.flex != flex) {
      parentData.flex = flex;
      needsLayout = true;
    }

    if (parentData.fit != fit) {
      parentData.fit = fit;
      needsLayout = true;
    }

    if (needsLayout) {
      final targetParent = renderObject.parent;
      if (targetParent is RenderObject) {
        targetParent.markNeedsLayout();
      }
    }
  }

  @override
  Type get debugTypicalAncestorWidgetClass => Flex;

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties.add(IntProperty('flex', flex));
  }
}

class BeagleExpanded extends BeagleFlexible {
  const BeagleExpanded({
    Key key,
    int flex = 1,
    @required Widget child,
  }) : super(key: key, flex: flex, fit: FlexFit.tight, child: child);
}

class RenderWrapFlex extends RenderBox
    with
        ContainerRenderObjectMixin<RenderBox, BeagleWrapFlexParentData>,
        RenderBoxContainerDefaultsMixin<RenderBox, BeagleWrapFlexParentData>,
        DebugOverflowIndicatorMixin {
  RenderWrapFlex({
    List<RenderBox> children,
    Axis direction = Axis.horizontal,
    MainAxisSize mainAxisSize = MainAxisSize.max,
    MainAxisAlignment mainAxisAlignment = MainAxisAlignment.start,
    CrossAxisAlignment crossAxisAlignment = CrossAxisAlignment.center,
    WrapAlignment alignment = WrapAlignment.start,
    WrapAlignment runAlignment = WrapAlignment.start,
    TextDirection textDirection,
    VerticalDirection verticalDirection = VerticalDirection.down,
    beagle.FlexWrap flexWrap = beagle.FlexWrap.NO_WRAP,
    TextBaseline textBaseline,
    Clip clipBehavior = Clip.none,
  })  : assert(direction != null),
        assert(mainAxisAlignment != null),
        assert(mainAxisSize != null),
        assert(crossAxisAlignment != null),
        assert(alignment != null),
        assert(runAlignment != null),
        assert(crossAxisAlignment != null),
        assert(clipBehavior != null),
        _direction = direction,
        _mainAxisAlignment = mainAxisAlignment,
        _mainAxisSize = mainAxisSize,
        _crossAxisAlignment = crossAxisAlignment,
        _alignment = alignment,
        _runAlignment = runAlignment,
        _textDirection = textDirection,
        _verticalDirection = verticalDirection,
        _textBaseline = textBaseline,
        _flexWrap = flexWrap {
    addAll(children);
  }

  final beagle.FlexWrap _flexWrap;

  Axis get direction => _direction;
  Axis _direction;

  set direction(Axis value) {
    assert(value != null);
    if (_direction != value) {
      _direction = value;
      markNeedsLayout();
    }
  }

  MainAxisAlignment get mainAxisAlignment => _mainAxisAlignment;
  MainAxisAlignment _mainAxisAlignment;

  set mainAxisAlignment(MainAxisAlignment value) {
    assert(value != null);
    if (_mainAxisAlignment != value) {
      _mainAxisAlignment = value;
      markNeedsLayout();
    }
  }

  MainAxisSize get mainAxisSize => _mainAxisSize;
  MainAxisSize _mainAxisSize;

  set mainAxisSize(MainAxisSize value) {
    assert(value != null);
    if (_mainAxisSize != value) {
      _mainAxisSize = value;
      markNeedsLayout();
    }
  }

  CrossAxisAlignment get crossAxisAlignment => _crossAxisAlignment;
  CrossAxisAlignment _crossAxisAlignment;

  set crossAxisAlignment(CrossAxisAlignment value) {
    assert(value != null);
    if (_crossAxisAlignment != value) {
      _crossAxisAlignment = value;
      markNeedsLayout();
    }
  }

  WrapAlignment get alignment => _alignment;
  WrapAlignment _alignment;

  set alignment(WrapAlignment value) {
    assert(value != null);
    if (_alignment == value) {
      return;
    }
    _alignment = value;
    markNeedsLayout();
  }

  WrapAlignment get runAlignment => _runAlignment;
  WrapAlignment _runAlignment;

  set runAlignment(WrapAlignment value) {
    assert(value != null);
    if (_runAlignment == value) {
      return;
    }
    _runAlignment = value;
    markNeedsLayout();
  }

  TextDirection get textDirection => _textDirection;
  TextDirection _textDirection;

  set textDirection(TextDirection value) {
    if (_textDirection != value) {
      _textDirection = value;
      markNeedsLayout();
    }
  }

  VerticalDirection get verticalDirection => _verticalDirection;
  VerticalDirection _verticalDirection;

  set verticalDirection(VerticalDirection value) {
    if (_verticalDirection != value) {
      _verticalDirection = value;
      markNeedsLayout();
    }
  }

  TextBaseline get textBaseline => _textBaseline;
  TextBaseline _textBaseline;

  set textBaseline(TextBaseline value) {
    assert(_crossAxisAlignment != CrossAxisAlignment.baseline || value != null);
    if (_textBaseline != value) {
      _textBaseline = value;
      markNeedsLayout();
    }
  }

  bool get _debugFlexHasNecessaryDirections {
    assert(direction != null);
    assert(crossAxisAlignment != null);
    if (firstChild != null && lastChild != firstChild) {
      // i.e. there's more than one child
      switch (direction) {
        case Axis.horizontal:
          assert(textDirection != null,
              'Horizontal $runtimeType with multiple children has a null textDirection, so the layout order is undefined.');
          break;
        case Axis.vertical:
          assert(verticalDirection != null,
              'Vertical $runtimeType with multiple children has a null verticalDirection, so the layout order is undefined.');
          break;
      }
    }
    if (mainAxisAlignment == MainAxisAlignment.start ||
        mainAxisAlignment == MainAxisAlignment.end) {
      switch (direction) {
        case Axis.horizontal:
          assert(textDirection != null,
              'Horizontal $runtimeType with $mainAxisAlignment has a null textDirection, so the alignment cannot be resolved.');
          break;
        case Axis.vertical:
          assert(verticalDirection != null,
              'Vertical $runtimeType with $mainAxisAlignment has a null verticalDirection, so the alignment cannot be resolved.');
          break;
      }
    }
    if (crossAxisAlignment == CrossAxisAlignment.start ||
        crossAxisAlignment == CrossAxisAlignment.end) {
      switch (direction) {
        case Axis.horizontal:
          assert(verticalDirection != null,
              'Horizontal $runtimeType with $crossAxisAlignment has a null verticalDirection, so the alignment cannot be resolved.');
          break;
        case Axis.vertical:
          assert(textDirection != null,
              'Vertical $runtimeType with $crossAxisAlignment has a null textDirection, so the alignment cannot be resolved.');
          break;
      }
    }
    return true;
  }

  bool get _debugWrapHasNecessaryDirections {
    assert(direction != null);
    assert(alignment != null);
    assert(runAlignment != null);
    assert(crossAxisAlignment != null);
    if (firstChild != null && lastChild != firstChild) {
      // i.e. there's more than one child
      switch (direction) {
        case Axis.horizontal:
          assert(textDirection != null,
              'Horizontal $runtimeType with multiple children has a null textDirection, so the layout order is undefined.');
          break;
        case Axis.vertical:
          assert(verticalDirection != null,
              'Vertical $runtimeType with multiple children has a null verticalDirection, so the layout order is undefined.');
          break;
      }
    }
    if (alignment == WrapAlignment.start || alignment == WrapAlignment.end) {
      switch (direction) {
        case Axis.horizontal:
          assert(textDirection != null,
              'Horizontal $runtimeType with alignment $alignment has a null textDirection, so the alignment cannot be resolved.');
          break;
        case Axis.vertical:
          assert(verticalDirection != null,
              'Vertical $runtimeType with alignment $alignment has a null verticalDirection, so the alignment cannot be resolved.');
          break;
      }
    }
    if (runAlignment == WrapAlignment.start ||
        runAlignment == WrapAlignment.end) {
      switch (direction) {
        case Axis.horizontal:
          assert(verticalDirection != null,
              'Horizontal $runtimeType with runAlignment $runAlignment has a null verticalDirection, so the alignment cannot be resolved.');
          break;
        case Axis.vertical:
          assert(textDirection != null,
              'Vertical $runtimeType with runAlignment $runAlignment has a null textDirection, so the alignment cannot be resolved.');
          break;
      }
    }
    return true;
  }

  // Set during layout if overflow occurred on the main axis.
  double _flexOverflow;

  // Check whether any meaningful overflow is present. Values below an epsilon
  // are treated as not overflowing.
  bool get _hasFlexOverflow => _flexOverflow > precisionErrorTolerance;

  bool _hasWrapVisualOverflow = false;

  Clip get clipBehavior => _clipBehavior;
  Clip _clipBehavior = Clip.none;

  set clipBehavior(Clip value) {
    assert(value != null);
    if (value != _clipBehavior) {
      _clipBehavior = value;
      markNeedsPaint();
      markNeedsSemanticsUpdate();
    }
  }

  var _isWrap = false;

  @override
  void setupParentData(RenderBox child) {
    if (child.parentData is! BeagleWrapFlexParentData) {
      child.parentData = BeagleWrapFlexParentData();
    }
  }

  bool _isWrapNeeded(Axis sizingDirection, double extent) {
    var childrenSize = 0.0;
    if (sizingDirection == Axis.horizontal) {
      for (final child in getChildrenAsList()) {
        child.layout(BoxConstraints(maxWidth: constraints.maxWidth),
            parentUsesSize: true);
        childrenSize += child.size.width;
      }
    } else {
      for (final child in getChildrenAsList()) {
        child.layout(BoxConstraints(maxHeight: constraints.maxHeight),
            parentUsesSize: true);
        childrenSize += child.size.height;
      }
    }
    return _isWrap =
        childrenSize > extent && _flexWrap != beagle.FlexWrap.NO_WRAP;
  }

  double _getFlexIntrinsicSize({
    @required Axis sizingDirection,
    @required double extent,
    @required ChildSizingFunction childSize,
  }) {
    if (_direction == sizingDirection) {
      // INTRINSIC MAIN SIZE
      // Intrinsic main size is the smallest size the flex container can take
      // while maintaining the min/max-content contributions of its flex items.
      var totalFlex = 0.0;
      var inflexibleSpace = 0.0;
      var maxFlexFractionSoFar = 0.0;
      var child = firstChild;
      while (child != null) {
        final flex = _getFlex(child);
        totalFlex += flex;
        if (flex > 0) {
          final flexFraction = childSize(child, extent) / _getFlex(child);
          maxFlexFractionSoFar = math.max(maxFlexFractionSoFar, flexFraction);
        } else {
          inflexibleSpace += childSize(child, extent);
        }
        final childParentData = child.parentData as BeagleWrapFlexParentData;
        child = childParentData.nextSibling;
      }
      return maxFlexFractionSoFar * totalFlex + inflexibleSpace;
    } else {
      // INTRINSIC CROSS SIZE
      // Intrinsic cross size is the max of the intrinsic cross sizes of the
      // children, after the flexible children are fit into the available space,
      // with the children sized using their max intrinsic dimensions.

      // Get inflexible space using the max intrinsic dimensions of fixed children in the main direction.
      final availableMainSpace = extent;
      var totalFlex = 0;
      var inflexibleSpace = 0.0;
      var maxCrossSize = 0.0;
      var child = firstChild;
      while (child != null) {
        final flex = _getFlex(child);
        totalFlex += flex;
        var mainSize = 0.0;
        var crossSize = 0.0;
        if (flex == 0) {
          switch (_direction) {
            case Axis.horizontal:
              mainSize = child.getMaxIntrinsicWidth(double.infinity);
              crossSize = childSize(child, mainSize);
              break;
            case Axis.vertical:
              mainSize = child.getMaxIntrinsicHeight(double.infinity);
              crossSize = childSize(child, mainSize);
              break;
          }
          inflexibleSpace += mainSize;
          maxCrossSize = math.max(maxCrossSize, crossSize);
        }
        final childParentData = child.parentData as BeagleWrapFlexParentData;
        child = childParentData.nextSibling;
      }

      // Determine the spacePerFlex by allocating the remaining available space.
      // When you're overconstrained spacePerFlex can be negative.
      final spacePerFlex =
          math.max(0, (availableMainSpace - inflexibleSpace) / totalFlex);

      // Size remaining (flexible) items, find the maximum cross size.
      child = firstChild;
      while (child != null) {
        final flex = _getFlex(child);
        if (flex > 0) {
          maxCrossSize =
              math.max(maxCrossSize, childSize(child, spacePerFlex * flex));
        }
        final childParentData = child.parentData as BeagleWrapFlexParentData;
        child = childParentData.nextSibling;
      }

      return maxCrossSize;
    }
  }

  double _computeWrapIntrinsicHeightForWidth(double width) {
    assert(direction == Axis.horizontal);
    var height = 0.0;
    var runWidth = 0.0;
    var runHeight = 0.0;
    var childCount = 0;
    var child = firstChild;
    while (child != null) {
      final childWidth =
          math.min(child.getMaxIntrinsicWidth(double.infinity), width);
      final childHeight = child.getMaxIntrinsicHeight(childWidth);
      // There must be at least one child before we move on to the next run.
      if (childCount > 0 && runWidth + childWidth > width) {
        height += runHeight;
        runWidth = 0.0;
        runHeight = 0.0;
        childCount = 0;
      }
      runWidth += childWidth;
      runHeight = math.max(runHeight, childHeight);
      childCount += 1;
      child = childAfter(child);
    }
    return height += runHeight;
  }

  double _computeWrapIntrinsicWidthForHeight(double height) {
    assert(direction == Axis.vertical);
    var width = 0.0;
    var runHeight = 0.0;
    var runWidth = 0.0;
    var childCount = 0;
    var child = firstChild;
    while (child != null) {
      final childHeight =
          math.min(child.getMaxIntrinsicHeight(double.infinity), height);
      final childWidth = child.getMaxIntrinsicWidth(childHeight);
      // There must be at least one child before we move on to the next run.
      if (childCount > 0 && runHeight + childHeight > height) {
        width += runWidth;
        runHeight = 0.0;
        runWidth = 0.0;
        childCount = 0;
      }
      runHeight += childHeight;
      runWidth = math.max(runWidth, childWidth);
      childCount += 1;
      child = childAfter(child);
    }
    return width += runWidth;
  }

  @override
  double computeMinIntrinsicWidth(double height) {
    if (_isWrap) {
      if (direction == Axis.horizontal) {
        var width = 0.0;
        var child = firstChild;
        while (child != null) {
          width = math.max(width, child.getMinIntrinsicWidth(double.infinity));
          child = childAfter(child);
        }
        return width;
      } else {
        return _computeWrapIntrinsicWidthForHeight(height);
      }
    }
    return _getFlexIntrinsicSize(
      sizingDirection: Axis.horizontal,
      extent: height,
      childSize: (RenderBox child, double extent) =>
          child.getMinIntrinsicWidth(extent),
    );
  }

  @override
  double computeMaxIntrinsicWidth(double height) {
    if (_isWrap) {
      if (direction == Axis.horizontal) {
        var width = 0.0;
        var child = firstChild;
        while (child != null) {
          width += child.getMaxIntrinsicWidth(double.infinity);
          child = childAfter(child);
        }
        return width;
      } else {
        return _computeWrapIntrinsicWidthForHeight(height);
      }
    }
    return _getFlexIntrinsicSize(
      sizingDirection: Axis.horizontal,
      extent: height,
      childSize: (RenderBox child, double extent) =>
          child.getMaxIntrinsicWidth(extent),
    );
  }

  @override
  double computeMinIntrinsicHeight(double width) {
    if (_isWrap) {
      if (direction == Axis.horizontal) {
        return _computeWrapIntrinsicHeightForWidth(width);
      } else {
        var height = 0.0;
        var child = firstChild;
        while (child != null) {
          height =
              math.max(height, child.getMinIntrinsicHeight(double.infinity));
          child = childAfter(child);
        }
        return height;
      }
    }
    return _getFlexIntrinsicSize(
      sizingDirection: Axis.vertical,
      extent: width,
      childSize: (RenderBox child, double extent) =>
          child.getMinIntrinsicHeight(extent),
    );
  }

  @override
  double computeMaxIntrinsicHeight(double width) {
    if (_isWrap) {
      if (direction == Axis.horizontal) {
        return _computeWrapIntrinsicHeightForWidth(width);
      } else {
        var height = 0.0;
        var child = firstChild;
        while (child != null) {
          height += child.getMaxIntrinsicHeight(double.infinity);
          child = childAfter(child);
        }
        return height;
      }
    }
    return _getFlexIntrinsicSize(
      sizingDirection: Axis.vertical,
      extent: width,
      childSize: (RenderBox child, double extent) =>
          child.getMaxIntrinsicHeight(extent),
    );
  }

  @override
  double computeDistanceToActualBaseline(TextBaseline baseline) {
    if (direction == Axis.horizontal || _isWrap) {
      return defaultComputeDistanceToHighestActualBaseline(baseline);
    }
    return defaultComputeDistanceToFirstActualBaseline(baseline);
  }

  int _getFlex(RenderBox child) {
    final childParentData = child.parentData as BeagleWrapFlexParentData;
    return childParentData.flex ?? 0;
  }

  FlexFit _getFit(RenderBox child) {
    final childParentData = child.parentData as BeagleWrapFlexParentData;
    return childParentData.fit ?? FlexFit.tight;
  }

  double _getCrossSize(RenderBox child) {
    switch (_direction) {
      case Axis.horizontal:
        return child.size.height;
      case Axis.vertical:
        return child.size.width;
    }
    return 0;
  }

  double _getMainSize(RenderBox child) {
    switch (_direction) {
      case Axis.horizontal:
        return child.size.width;
      case Axis.vertical:
        return child.size.height;
    }
    return 0;
  }

  double _getWrapMainAxisExtent(RenderBox child) =>
      direction == Axis.horizontal ? child.size.width : child.size.height;

  double _getWrapCrossAxisExtent(RenderBox child) =>
      direction == Axis.horizontal ? child.size.height : child.size.width;

  Offset _getWrapOffset(double mainAxisOffset, double crossAxisOffset) =>
      direction == Axis.horizontal
          ? Offset(mainAxisOffset, crossAxisOffset)
          : Offset(crossAxisOffset, mainAxisOffset);

  double _getWrapChildCrossAxisOffset(bool flipCrossAxis,
      double runCrossAxisExtent, double childCrossAxisExtent) {
    final freeSpace = runCrossAxisExtent - childCrossAxisExtent;
    return flipCrossAxis ? freeSpace : 0.0;
  }

  void _performWrapLayout() {
    final constraints = this.constraints;
    assert(_debugWrapHasNecessaryDirections);
    _hasWrapVisualOverflow = false;
    var child = firstChild;
    if (child == null) {
      size = constraints.smallest;
      return;
    }
    BoxConstraints childConstraints;
    var mainAxisLimit = 0.0;
    var flipMainAxis = false;
    var flipCrossAxis = false;
    switch (direction) {
      case Axis.horizontal:
        childConstraints = BoxConstraints(maxWidth: constraints.maxWidth);
        mainAxisLimit = constraints.maxWidth;
        if (textDirection == TextDirection.rtl) {
          flipMainAxis = true;
        }
        if (verticalDirection == VerticalDirection.up) {
          flipCrossAxis = true;
        }
        break;
      case Axis.vertical:
        childConstraints = BoxConstraints(maxHeight: constraints.maxHeight);
        mainAxisLimit = constraints.maxHeight;
        if (verticalDirection == VerticalDirection.up) {
          flipMainAxis = true;
        }
        if (textDirection == TextDirection.rtl) {
          flipCrossAxis = true;
        }
        break;
    }
    assert(childConstraints != null);
    assert(mainAxisLimit != null);

    final runMetrics = <_RunMetrics>[];
    var mainAxisExtent = 0.0;
    var crossAxisExtent = 0.0;
    var runMainAxisExtent = 0.0;
    var runCrossAxisExtent = 0.0;
    var childCount = 0;

    child = firstChild;
    while (child != null) {
      child.layout(childConstraints, parentUsesSize: true);
      final childMainAxisExtent = _getWrapMainAxisExtent(child);
      final childCrossAxisExtent = _getWrapCrossAxisExtent(child);
      if (childCount > 0 &&
          runMainAxisExtent + childMainAxisExtent > mainAxisLimit) {
        mainAxisExtent = math.max(mainAxisExtent, runMainAxisExtent);
        crossAxisExtent += runCrossAxisExtent;
        runMetrics.add(_RunMetrics(
          runMainAxisExtent,
          runCrossAxisExtent,
          childCount,
        ));
        runMainAxisExtent = 0.0;
        runCrossAxisExtent = 0.0;
        childCount = 0;
      }
      runMainAxisExtent += childMainAxisExtent;
      runCrossAxisExtent = math.max(runCrossAxisExtent, childCrossAxisExtent);
      childCount += 1;
      final childParentData = child.parentData as BeagleWrapFlexParentData;
      childParentData._runIndex = runMetrics.length;
      child = childParentData.nextSibling;
    }
    if (childCount > 0) {
      mainAxisExtent = math.max(mainAxisExtent, runMainAxisExtent);
      crossAxisExtent += runCrossAxisExtent;
      runMetrics.add(_RunMetrics(
        runMainAxisExtent,
        runCrossAxisExtent,
        childCount,
      ));
    }

    final runCount = runMetrics.length;
    assert(runCount > 0);

    final children = getChildrenAsList();
    var runInit = 0;
    for (final run in runMetrics) {
      var totalFlex = 0;
      var allocatedSize = 0.0;
      RenderBox lastFlexChild;
      for (var i = runInit; i < runInit + run.childCount; i++) {
        final flex = _getFlex(children[i]);
        if (flex > 0) {
          totalFlex += flex;
          lastFlexChild = children[i];
        } else {
          allocatedSize += _getMainSize(children[i]);
        }
      }
      final freeSpace = math.max(0, mainAxisLimit - allocatedSize);
      final spacePerFlex = freeSpace / totalFlex;
      for (var i = runInit; i < runInit + run.childCount; i++) {
        BoxConstraints innerConstraints;
        final flex = _getFlex(children[i]);
        var allocatedFlexSpace = 0.0;
        if (flex > 0) {
          final maxChildExtent = children[i] == lastFlexChild
              ? freeSpace - allocatedFlexSpace
              : spacePerFlex * flex;
          if (direction == Axis.horizontal) {
            innerConstraints = BoxConstraints(
              minWidth: maxChildExtent,
              maxWidth: constraints.maxWidth,
            );
          } else {
            innerConstraints = BoxConstraints(
              minHeight: maxChildExtent,
              maxHeight: constraints.maxHeight,
            );
          }
          children[i].layout(innerConstraints, parentUsesSize: true);
          allocatedFlexSpace += maxChildExtent;
        }
      }
      runInit = run.childCount;
    }

    var containerMainAxisExtent = 0.0;
    var containerCrossAxisExtent = 0.0;

    switch (direction) {
      case Axis.horizontal:
        size = constraints.constrain(Size(mainAxisExtent, crossAxisExtent));
        containerMainAxisExtent = size.width;
        containerCrossAxisExtent = size.height;
        break;
      case Axis.vertical:
        size = constraints.constrain(Size(crossAxisExtent, mainAxisExtent));
        containerMainAxisExtent = size.height;
        containerCrossAxisExtent = size.width;
        break;
    }

    _hasWrapVisualOverflow = containerMainAxisExtent < mainAxisExtent ||
        containerCrossAxisExtent < crossAxisExtent;

    final crossAxisFreeSpace =
        math.max(0, containerCrossAxisExtent - crossAxisExtent);
    var runLeadingSpace = 0.0;
    var runBetweenSpace = 0.0;
    switch (runAlignment) {
      case WrapAlignment.start:
        break;
      case WrapAlignment.end:
        runLeadingSpace = crossAxisFreeSpace;
        break;
      case WrapAlignment.center:
        runLeadingSpace = crossAxisFreeSpace / 2.0;
        break;
      case WrapAlignment.spaceBetween:
        runBetweenSpace =
            runCount > 1 ? crossAxisFreeSpace / (runCount - 1) : 0.0;
        break;
      case WrapAlignment.spaceAround:
        runBetweenSpace = crossAxisFreeSpace / runCount;
        runLeadingSpace = runBetweenSpace / 2.0;
        break;
      case WrapAlignment.spaceEvenly:
        runBetweenSpace = crossAxisFreeSpace / (runCount + 1);
        runLeadingSpace = runBetweenSpace;
        break;
    }

    var crossAxisOffset = flipCrossAxis
        ? containerCrossAxisExtent - runLeadingSpace
        : runLeadingSpace;

    child = firstChild;
    for (var i = 0; i < runCount; ++i) {
      final metrics = runMetrics[i];
      final runMainAxisExtent = metrics.mainAxisExtent;
      final runCrossAxisExtent = metrics.crossAxisExtent;
      final childCount = metrics.childCount;

      final mainAxisFreeSpace =
          math.max(0, containerMainAxisExtent - runMainAxisExtent);
      var childLeadingSpace = 0.0;
      var childBetweenSpace = 0.0;

      switch (alignment) {
        case WrapAlignment.start:
          break;
        case WrapAlignment.end:
          childLeadingSpace = mainAxisFreeSpace;
          break;
        case WrapAlignment.center:
          childLeadingSpace = mainAxisFreeSpace / 2.0;
          break;
        case WrapAlignment.spaceBetween:
          childBetweenSpace =
              childCount > 1 ? mainAxisFreeSpace / (childCount - 1) : 0.0;
          break;
        case WrapAlignment.spaceAround:
          childBetweenSpace = mainAxisFreeSpace / childCount;
          childLeadingSpace = childBetweenSpace / 2.0;
          break;
        case WrapAlignment.spaceEvenly:
          childBetweenSpace = mainAxisFreeSpace / (childCount + 1);
          childLeadingSpace = childBetweenSpace;
          break;
      }

      var childMainPosition = flipMainAxis
          ? containerMainAxisExtent - childLeadingSpace
          : childLeadingSpace;

      if (flipCrossAxis) {
        crossAxisOffset -= runCrossAxisExtent;
      }

      while (child != null) {
        final childParentData = child.parentData as BeagleWrapFlexParentData;
        if (childParentData._runIndex != i) {
          break;
        }
        final childMainAxisExtent = _getWrapMainAxisExtent(child);
        final childCrossAxisExtent = _getWrapCrossAxisExtent(child);
        final childCrossAxisOffset = _getWrapChildCrossAxisOffset(
            flipCrossAxis, runCrossAxisExtent, childCrossAxisExtent);
        if (flipMainAxis) {
          childMainPosition -= childMainAxisExtent;
        }
        childParentData.offset = _getWrapOffset(
            childMainPosition, crossAxisOffset + childCrossAxisOffset);
        if (flipMainAxis) {
          childMainPosition -= childBetweenSpace;
        } else {
          childMainPosition += childMainAxisExtent + childBetweenSpace;
        }
        child = childParentData.nextSibling;
      }

      if (flipCrossAxis) {
        crossAxisOffset -= runBetweenSpace;
      } else {
        crossAxisOffset += runCrossAxisExtent + runBetweenSpace;
      }
    }
  }

  void _performFlexLayout() {
    assert(_debugFlexHasNecessaryDirections);
    final constraints = this.constraints;

    // Determine used flex factor, size inflexible items, calculate free space.
    var totalFlex = 0;
    var totalChildren = 0;
    assert(constraints != null);
    final maxMainSize = _direction == Axis.horizontal
        ? constraints.maxWidth
        : constraints.maxHeight;
    final canFlex = maxMainSize < double.infinity;

    var crossSize = 0.0;
    var allocatedSize = 0.0; // Sum of the sizes of the non-flexible children.
    var child = firstChild;
    RenderBox lastFlexChild;
    while (child != null) {
      final childParentData = child.parentData as BeagleWrapFlexParentData;
      totalChildren++;
      final flex = _getFlex(child);
      if (flex > 0) {
        assert(() {
          final identity = _direction == Axis.horizontal ? 'row' : 'column';
          final axis =
              _direction == Axis.horizontal ? 'horizontal' : 'vertical';
          final dimension = _direction == Axis.horizontal ? 'width' : 'height';
          DiagnosticsNode error, message;
          final addendum = <DiagnosticsNode>[];
          if (!canFlex &&
              (mainAxisSize == MainAxisSize.max ||
                  _getFit(child) == FlexFit.tight)) {
            error = ErrorSummary(
                'RenderFlex children have non-zero flex but incoming $dimension constraints are unbounded.');
            message = ErrorDescription(
                'When a $identity is in a parent that does not provide a finite $dimension constraint, for example '
                'if it is in a $axis scrollable, it will try to shrink-wrap its children along the $axis '
                'axis. Setting a flex on a child (e.g. using Expanded) indicates that the child is to '
                'expand to fill the remaining space in the $axis direction.');
            RenderBox node = this;
            switch (_direction) {
              case Axis.horizontal:
                while (!node.constraints.hasBoundedWidth &&
                    node.parent is RenderBox) {
                  node = node.parent as RenderBox;
                }
                if (!node.constraints.hasBoundedWidth) {
                  node = null;
                }
                break;
              case Axis.vertical:
                while (!node.constraints.hasBoundedHeight &&
                    node.parent is RenderBox) {
                  node = node.parent as RenderBox;
                }
                if (!node.constraints.hasBoundedHeight) {
                  node = null;
                }
                break;
            }
            if (node != null) {
              addendum.add(node.describeForError(
                  'The nearest ancestor providing an unbounded width constraint is'));
            }
            addendum.add(ErrorHint('See also: https://flutter.dev/layout/'));
          } else {
            return true;
          }
          throw FlutterError.fromParts(<DiagnosticsNode>[
            error,
            message,
            ErrorDescription(
                'These two directives are mutually exclusive. If a parent is to shrink-wrap its child, the child '
                'cannot simultaneously expand to fit its parent.'),
            ErrorHint(
                'Consider setting mainAxisSize to MainAxisSize.min and using FlexFit.loose fits for the flexible '
                'children (using Flexible rather than Expanded). This will allow the flexible children '
                'to size themselves to less than the infinite remaining space they would otherwise be '
                'forced to take, and then will cause the RenderFlex to shrink-wrap the children '
                'rather than expanding to fit the maximum constraints provided by the parent.'),
            ErrorDescription(
                'If this message did not help you determine the problem, consider using debugDumpRenderTree():\n'
                '  https://flutter.dev/debugging/#rendering-layer\n'
                '  http://api.flutter.dev/flutter/rendering/debugDumpRenderTree.html'),
            describeForError('The affected RenderFlex is',
                style: DiagnosticsTreeStyle.errorProperty),
            DiagnosticsProperty<dynamic>(
                'The creator information is set to', debugCreator,
                style: DiagnosticsTreeStyle.errorProperty),
            ...addendum,
            ErrorDescription(
                "If none of the above helps enough to fix this problem, please don't hesitate to file a bug:\n"
                '  https://github.com/flutter/flutter/issues/new?template=BUG.md'),
          ]);
        }());
        totalFlex += flex;
        lastFlexChild = child;
      } else {
        BoxConstraints innerConstraints;
        if (crossAxisAlignment == CrossAxisAlignment.stretch) {
          switch (_direction) {
            case Axis.horizontal:
              innerConstraints =
                  BoxConstraints.tightFor(height: constraints.maxHeight);
              break;
            case Axis.vertical:
              innerConstraints =
                  BoxConstraints.tightFor(width: constraints.maxWidth);
              break;
          }
        } else {
          switch (_direction) {
            case Axis.horizontal:
              innerConstraints =
                  BoxConstraints(maxHeight: constraints.maxHeight);
              break;
            case Axis.vertical:
              innerConstraints = BoxConstraints(maxWidth: constraints.maxWidth);
              break;
          }
        }
        child.layout(innerConstraints, parentUsesSize: true);
        allocatedSize += _getMainSize(child);
        crossSize = math.max(crossSize, _getCrossSize(child));
      }
      assert(child.parentData == childParentData);
      child = childParentData.nextSibling;
    }

    // Distribute free space to flexible children, and determine baseline.
    final freeSpace =
        math.max(0, (canFlex ? maxMainSize : 0.0) - allocatedSize);
    var allocatedFlexSpace = 0.0;
    var maxBaselineDistance = 0.0;
    if (totalFlex > 0 || crossAxisAlignment == CrossAxisAlignment.baseline) {
      final spacePerFlex =
          canFlex && totalFlex > 0 ? (freeSpace / totalFlex) : double.nan;
      child = firstChild;
      var maxSizeAboveBaseline = 0;
      var maxSizeBelowBaseline = 0;
      while (child != null) {
        final flex = _getFlex(child);
        if (flex > 0) {
          final maxChildExtent = canFlex
              ? (child == lastFlexChild
                  ? (freeSpace - allocatedFlexSpace)
                  : spacePerFlex * flex)
              : double.infinity;
          double minChildExtent;
          switch (_getFit(child)) {
            case FlexFit.tight:
              assert(maxChildExtent < double.infinity);
              minChildExtent = maxChildExtent;
              break;
            case FlexFit.loose:
              minChildExtent = 0.0;
              break;
          }
          assert(minChildExtent != null);
          BoxConstraints innerConstraints;
          if (crossAxisAlignment == CrossAxisAlignment.stretch) {
            switch (_direction) {
              case Axis.horizontal:
                innerConstraints = BoxConstraints(
                    minWidth: minChildExtent,
                    maxWidth: maxChildExtent,
                    minHeight: constraints.maxHeight,
                    maxHeight: constraints.maxHeight);
                break;
              case Axis.vertical:
                innerConstraints = BoxConstraints(
                    minWidth: constraints.maxWidth,
                    maxWidth: constraints.maxWidth,
                    minHeight: minChildExtent,
                    maxHeight: maxChildExtent);
                break;
            }
          } else {
            switch (_direction) {
              case Axis.horizontal:
                innerConstraints = BoxConstraints(
                    minWidth: minChildExtent,
                    maxWidth: maxChildExtent,
                    maxHeight: constraints.maxHeight);
                break;
              case Axis.vertical:
                innerConstraints = BoxConstraints(
                    maxWidth: constraints.maxWidth,
                    minHeight: minChildExtent,
                    maxHeight: maxChildExtent);
                break;
            }
          }
          child.layout(innerConstraints, parentUsesSize: true);
          final childSize = _getMainSize(child);
          assert(childSize <= maxChildExtent);
          allocatedSize += childSize;
          allocatedFlexSpace += maxChildExtent;
          crossSize = math.max(crossSize, _getCrossSize(child));
        }
        if (crossAxisAlignment == CrossAxisAlignment.baseline) {
          assert(() {
            if (textBaseline == null) {
              throw FlutterError(
                  'To use FlexAlignItems.baseline, you must also specify which baseline to use using the "baseline" argument.');
            }
            return true;
          }());
          final distance =
              child.getDistanceToBaseline(textBaseline, onlyReal: true);
          if (distance != null) {
            maxBaselineDistance = math.max(maxBaselineDistance, distance);
            maxSizeAboveBaseline = math.max(
              distance.toInt(),
              maxSizeAboveBaseline,
            );
            maxSizeBelowBaseline = math.max(
              (child.size.height - distance).toInt(),
              maxSizeBelowBaseline,
            );
            crossSize = math.max(
                (maxSizeAboveBaseline + maxSizeBelowBaseline).toDouble(),
                crossSize);
          }
        }
        final childParentData = child.parentData as BeagleWrapFlexParentData;
        child = childParentData.nextSibling;
      }
    }

    // Align items along the main axis.
    final idealSize = canFlex && mainAxisSize == MainAxisSize.max
        ? maxMainSize
        : allocatedSize;
    double actualSize;
    switch (_direction) {
      case Axis.horizontal:
        size = constraints.constrain(Size(idealSize, crossSize));
        actualSize = size.width;
        crossSize = size.height;
        break;
      case Axis.vertical:
        size = constraints.constrain(Size(crossSize, idealSize));
        actualSize = size.height;
        crossSize = size.width;
        break;
    }
    final actualSizeDelta = actualSize - allocatedSize;
    _flexOverflow = math.max(0, -actualSizeDelta);
    final remainingSpace = math.max(0, actualSizeDelta);
    var leadingSpace = 0.0;
    var betweenSpace = 0.0;
    // flipMainAxis is used to decide whether to lay out left-to-right/top-to-bottom (false), or
    // right-to-left/bottom-to-top (true). The _startIsTopLeft will return null if there's only
    // one child and the relevant direction is null, in which case we arbitrarily decide not to
    // flip, but that doesn't have any detectable effect.
    final flipMainAxis = !(_startIsTopLeft(direction,
            textDirection: textDirection,
            verticalDirection: verticalDirection) ??
        true);
    switch (_mainAxisAlignment) {
      case MainAxisAlignment.start:
        leadingSpace = 0.0;
        betweenSpace = 0.0;
        break;
      case MainAxisAlignment.end:
        leadingSpace = remainingSpace;
        betweenSpace = 0.0;
        break;
      case MainAxisAlignment.center:
        leadingSpace = remainingSpace / 2.0;
        betweenSpace = 0.0;
        break;
      case MainAxisAlignment.spaceBetween:
        leadingSpace = 0.0;
        betweenSpace =
            totalChildren > 1 ? remainingSpace / (totalChildren - 1) : 0.0;
        break;
      case MainAxisAlignment.spaceAround:
        betweenSpace = totalChildren > 0 ? remainingSpace / totalChildren : 0.0;
        leadingSpace = betweenSpace / 2.0;
        break;
      case MainAxisAlignment.spaceEvenly:
        betweenSpace =
            totalChildren > 0 ? remainingSpace / (totalChildren + 1) : 0.0;
        leadingSpace = betweenSpace;
        break;
    }

    // Position elements
    var childMainPosition =
        flipMainAxis ? actualSize - leadingSpace : leadingSpace;
    child = firstChild;
    while (child != null) {
      final childParentData = child.parentData as BeagleWrapFlexParentData;
      double childCrossPosition;
      switch (_crossAxisAlignment) {
        case CrossAxisAlignment.start:
        case CrossAxisAlignment.end:
          childCrossPosition = _startIsTopLeft(flipAxis(direction),
                      textDirection: textDirection,
                      verticalDirection: verticalDirection) ==
                  (_crossAxisAlignment == CrossAxisAlignment.start)
              ? 0.0
              : crossSize - _getCrossSize(child);
          break;
        case CrossAxisAlignment.center:
          childCrossPosition = crossSize / 2.0 - _getCrossSize(child) / 2.0;
          break;
        case CrossAxisAlignment.stretch:
          childCrossPosition = 0.0;
          break;
        case CrossAxisAlignment.baseline:
          childCrossPosition = 0.0;
          if (_direction == Axis.horizontal) {
            assert(textBaseline != null);
            final distance =
                child.getDistanceToBaseline(textBaseline, onlyReal: true);
            if (distance != null) {
              childCrossPosition = maxBaselineDistance - distance;
            }
          }
          break;
      }
      if (flipMainAxis) {
        childMainPosition -= _getMainSize(child);
      }
      switch (_direction) {
        case Axis.horizontal:
          childParentData.offset =
              Offset(childMainPosition, childCrossPosition);
          break;
        case Axis.vertical:
          childParentData.offset =
              Offset(childCrossPosition, childMainPosition);
          break;
      }
      if (flipMainAxis) {
        childMainPosition -= betweenSpace;
      } else {
        childMainPosition += _getMainSize(child) + betweenSpace;
      }
      child = childParentData.nextSibling;
    }
  }

  @override
  void performLayout() {
    final isWrap = _isWrapNeeded(
        _direction,
        _direction == Axis.horizontal
            ? constraints.maxWidth
            : constraints.maxHeight);
    if (isWrap) {
      _performWrapLayout();
    } else {
      _performFlexLayout();
    }
  }

  @override
  bool hitTestChildren(BoxHitTestResult result, {@required Offset position}) {
    return defaultHitTestChildren(result, position: position);
  }

  @override
  void paint(PaintingContext context, Offset offset) {
    if (_isWrap) {
      if (_hasWrapVisualOverflow && clipBehavior != Clip.none) {
        context.pushClipRect(
            needsCompositing, offset, Offset.zero & size, defaultPaint,
            clipBehavior: clipBehavior);
      } else {
        defaultPaint(context, offset);
      }
    } else {
      if (!_hasFlexOverflow) {
        defaultPaint(context, offset);
        return;
      }

      // There's no point in drawing the children if we're empty.
      if (size.isEmpty) {
        return;
      }

      if (clipBehavior == Clip.none) {
        defaultPaint(context, offset);
      } else {
        // We have overflow and the clipBehavior isn't none. Clip it.
        context.pushClipRect(
            needsCompositing, offset, Offset.zero & size, defaultPaint,
            clipBehavior: clipBehavior);
      }

      assert(() {
        // Only set this if it's null to save work. It gets reset to null if the
        // _direction changes.
        final debugOverflowHints = <DiagnosticsNode>[
          ErrorDescription(
              'The overflowing $runtimeType has an orientation of $_direction.'),
          ErrorDescription(
              'The edge of the $runtimeType that is overflowing has been marked '
              'in the rendering with a yellow and black striped pattern. This is '
              'usually caused by the contents being too big for the $runtimeType.'),
          ErrorHint(
              'Consider applying a flex factor (e.g. using an Expanded widget) to '
              'force the children of the $runtimeType to fit within the available '
              'space instead of being sized to their natural size.'),
          ErrorHint(
              'This is considered an error condition because it indicates that there '
              'is content that cannot be seen. If the content is legitimately bigger '
              'than the available space, consider clipping it with a ClipRect widget '
              'before putting it in the flex, or using a scrollable container rather '
              'than a Flex, like a ListView.'),
        ];

        // Simulate a child rect that overflows by the right amount. This child
        // rect is never used for drawing, just for determining the overflow
        // location and amount.
        Rect overflowChildRect;
        switch (_direction) {
          case Axis.horizontal:
            overflowChildRect =
                Rect.fromLTWH(0, 0, size.width + _flexOverflow, 0);
            break;
          case Axis.vertical:
            overflowChildRect =
                Rect.fromLTWH(0, 0, 0, size.height + _flexOverflow);
            break;
        }
        paintOverflowIndicator(
            context, offset, Offset.zero & size, overflowChildRect,
            overflowHints: debugOverflowHints);
        return true;
      }());
    }
  }

  @override
  Rect describeApproximatePaintClip(RenderObject child) =>
      _hasFlexOverflow ? Offset.zero & size : null;

  @override
  String toStringShort() {
    var header = super.toStringShort();
    if (_flexOverflow != null && _hasFlexOverflow) {
      header += ' OVERFLOWING';
    }
    return header;
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties.add(EnumProperty<Axis>('direction', direction));
    properties.add(EnumProperty<MainAxisAlignment>(
        'mainAxisAlignment', mainAxisAlignment));
    properties.add(EnumProperty<MainAxisSize>('mainAxisSize', mainAxisSize));
    properties.add(EnumProperty<CrossAxisAlignment>(
        'crossAxisAlignment', crossAxisAlignment));
    properties.add(EnumProperty<TextDirection>('textDirection', textDirection,
        defaultValue: null));
    properties.add(EnumProperty<VerticalDirection>(
        'verticalDirection', verticalDirection,
        defaultValue: null));
    properties.add(EnumProperty<WrapAlignment>('alignment', alignment));
    properties.add(EnumProperty<WrapAlignment>('runAlignment', runAlignment));
    properties.add(EnumProperty<TextBaseline>('textBaseline', textBaseline,
        defaultValue: null));
  }
}
