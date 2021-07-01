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
import 'package:yoga_engine/yoga_engine.dart';

NodeProperties mapToNodeProperties(BeagleStyle style) {
  if (style == null) {
    return NodeProperties();
  }
  final nodeProperties = NodeProperties()
    ..setPositionType(_mapPositionType(style.positionType))
    ..setDisplay(_mapDisplay(style.display));

  _mapFlex(nodeProperties, style?.flex);
  if (style.size != null) {
    _mapSize(nodeProperties, style.size);
  }
  if (style.margin != null) {
    _mapMargin(nodeProperties, style.margin);
  }
  if (style.padding != null) {
    _mapPadding(nodeProperties, style.padding);
  }
  if (style.position != null) {
    _mapPosition(nodeProperties, style.position);
  }
  return nodeProperties;
}

YGPositionType _mapPositionType(FlexPosition flexPosition) {
  var positionType = YGPositionType.YGPositionTypeStatic;
  switch (flexPosition) {
    case FlexPosition.ABSOLUTE:
      positionType = YGPositionType.YGPositionTypeAbsolute;
      break;
    case FlexPosition.RELATIVE:
      positionType = YGPositionType.YGPositionTypeRelative;
      break;
  }
  return positionType;
}

YGDisplay _mapDisplay(FlexDisplay flexDisplay) {
  var display = YGDisplay.YGDisplayFlex;
  switch (flexDisplay) {
    case FlexDisplay.FLEX:
      display = YGDisplay.YGDisplayFlex;
      break;
    case FlexDisplay.NONE:
      display = YGDisplay.YGDisplayNone;
      break;
  }
  return display;
}

void _mapFlex(NodeProperties nodeProperties, BeagleFlex flex) {
  nodeProperties
    ..setAlignContent(_mapAlignContent(flex?.alignContent))
    ..setAlignItems(_mapAlignItems(flex?.alignItems))
    ..setAlignSelf(_mapAlignSelf(flex?.alignSelf))
    ..setFlex(flex?.flex?.toDouble() ?? 0.0)
    ..setFlexDirection(_mapFlexDirection(flex?.flexDirection))
    ..setFlexWrap(_mapWrap(flex?.flexWrap))
    ..setGrow(flex?.grow?.toDouble() ?? 0.0)
    ..setJustifyContent(_mapJustify(flex?.justifyContent))
    ..setShrink(flex?.shrink?.toDouble() ?? 1.0);

  if (flex?.basis?.value == null || flex?.basis?.value == 0) {
    nodeProperties.setBasisAuto();
  } else {
    if (flex.basis.type == UnitType.PERCENT) {
      nodeProperties.setBasisPercent(flex.basis.value.toDouble());
    } else {
      nodeProperties.setBasis(flex.basis.value.toDouble());
    }
  }
}

YGAlign _mapAlignContent(AlignContent alignContent) {
  var align = YGAlign.YGAlignFlexStart;
  switch (alignContent) {
    case AlignContent.FLEX_START:
      align = YGAlign.YGAlignFlexStart;
      break;
    case AlignContent.CENTER:
      align = YGAlign.YGAlignCenter;
      break;
    case AlignContent.FLEX_END:
      align = YGAlign.YGAlignFlexEnd;
      break;
    case AlignContent.STRETCH:
      align = YGAlign.YGAlignStretch;
      break;
    case AlignContent.SPACE_AROUND:
      align = YGAlign.YGAlignSpaceAround;
      break;
    case AlignContent.SPACE_BETWEEN:
      align = YGAlign.YGAlignSpaceBetween;
      break;
  }
  return align;
}

YGAlign _mapAlignItems(AlignItems alignItems) {
  var align = YGAlign.YGAlignStretch;
  switch (alignItems) {
    case AlignItems.STRETCH:
      align = YGAlign.YGAlignStretch;
      break;
    case AlignItems.FLEX_START:
      align = YGAlign.YGAlignFlexStart;
      break;
    case AlignItems.CENTER:
      align = YGAlign.YGAlignCenter;
      break;
    case AlignItems.FLEX_END:
      align = YGAlign.YGAlignFlexEnd;
      break;
    case AlignItems.BASELINE:
      align = YGAlign.YGAlignBaseline;
      break;
  }
  return align;
}

YGAlign _mapAlignSelf(AlignSelf alignSelf) {
  var align = YGAlign.YGAlignAuto;
  switch (alignSelf) {
    case AlignSelf.AUTO:
      align = YGAlign.YGAlignAuto;
      break;
    case AlignSelf.FLEX_START:
      align = YGAlign.YGAlignFlexStart;
      break;
    case AlignSelf.CENTER:
      align = YGAlign.YGAlignCenter;
      break;
    case AlignSelf.FLEX_END:
      align = YGAlign.YGAlignFlexEnd;
      break;
    case AlignSelf.STRETCH:
      align = YGAlign.YGAlignStretch;
      break;
    case AlignSelf.BASELINE:
      align = YGAlign.YGAlignBaseline;
      break;
  }
  return align;
}

YGFlexDirection _mapFlexDirection(FlexDirection flexDirection) {
  var direction = YGFlexDirection.YGFlexDirectionColumn;
  switch (flexDirection) {
    case FlexDirection.COLUMN:
      direction = YGFlexDirection.YGFlexDirectionColumn;
      break;
    case FlexDirection.ROW:
      direction = YGFlexDirection.YGFlexDirectionRow;
      break;
    case FlexDirection.COLUMN_REVERSE:
      direction = YGFlexDirection.YGFlexDirectionColumnReverse;
      break;
    case FlexDirection.ROW_REVERSE:
      direction = YGFlexDirection.YGFlexDirectionRowReverse;
      break;
  }
  return direction;
}

YGWrap _mapWrap(FlexWrap flexWrap) {
  var wrap = YGWrap.YGWrapNoWrap;
  switch (flexWrap) {
    case FlexWrap.NO_WRAP:
      wrap = YGWrap.YGWrapNoWrap;
      break;
    case FlexWrap.WRAP:
      wrap = YGWrap.YGWrapWrap;
      break;
    case FlexWrap.WRAP_REVERSE:
      wrap = YGWrap.YGWrapWrapReverse;
      break;
  }
  return wrap;
}

YGJustify _mapJustify(JustifyContent justifyContent) {
  var justify = YGJustify.YGJustifyFlexStart;
  switch (justifyContent) {
    case JustifyContent.FLEX_START:
      justify = YGJustify.YGJustifyFlexStart;
      break;
    case JustifyContent.CENTER:
      justify = YGJustify.YGJustifyCenter;
      break;
    case JustifyContent.FLEX_END:
      justify = YGJustify.YGJustifyFlexEnd;
      break;
    case JustifyContent.SPACE_BETWEEN:
      justify = YGJustify.YGJustifySpaceBetween;
      break;
    case JustifyContent.SPACE_AROUND:
      justify = YGJustify.YGJustifySpaceAround;
      break;
    case JustifyContent.SPACE_EVENLY:
      justify = YGJustify.YGJustifySpaceEvenly;
      break;
  }
  return justify;
}

void _mapSize(NodeProperties nodeProperties, BeagleSize size) {
  if (size.width != null) {
    if (size.width.type == UnitType.REAL) {
      nodeProperties.setWidth(size.width.value.toDouble());
    } else {
      nodeProperties.setWidthPercent(size.width.value.toDouble());
    }
  }
  if (size.height != null) {
    if (size.height.type == UnitType.REAL) {
      nodeProperties.setHeight(size.height.value.toDouble());
    } else {
      nodeProperties.setHeightPercent(size.height.value.toDouble());
    }
  }
  if (size.maxWidth != null) {
    if (size.maxWidth.type == UnitType.REAL) {
      nodeProperties.setMaxWidth(size.maxWidth.value.toDouble());
    } else {
      nodeProperties.setMaxWidthPercent(size.maxWidth.value.toDouble());
    }
  }
  if (size.maxHeight != null) {
    if (size.maxHeight.type == UnitType.REAL) {
      nodeProperties.setMaxHeight(size.maxHeight.value.toDouble());
    } else {
      nodeProperties.setMaxHeightPercent(size.maxHeight.value.toDouble());
    }
  }
  if (size.minWidth != null) {
    if (size.minWidth.type == UnitType.REAL) {
      nodeProperties.setMinWidth(size.minWidth.value.toDouble());
    } else {
      nodeProperties.setMinWidthPercent(size.minWidth.value.toDouble());
    }
  }
  if (size.minHeight != null) {
    if (size.minHeight.type == UnitType.REAL) {
      nodeProperties.setMinHeight(size.minHeight.value.toDouble());
    } else {
      nodeProperties.setMinHeightPercent(size.minHeight.value.toDouble());
    }
  }
  if (size.aspectRatio != null) {
    nodeProperties.setAspectRatio(size.aspectRatio.toDouble());
  }
}

void _mapMargin(NodeProperties nodeProperties, EdgeValue margin) {
  _mapEdgeValue(margin, (YGEdge edge, UnitValue unitValue) {
    if (unitValue.type == UnitType.REAL) {
      nodeProperties.setMargin(edge, unitValue.value.toDouble());
    } else {
      nodeProperties.setMarginPercent(edge, unitValue.value.toDouble());
    }
  });
}

void _mapPadding(NodeProperties nodeProperties, EdgeValue padding) {
  _mapEdgeValue(padding, (YGEdge edge, UnitValue unitValue) {
    if (unitValue.type == UnitType.REAL) {
      nodeProperties.setPadding(edge, unitValue.value.toDouble());
    } else {
      nodeProperties.setPaddingPercent(edge, unitValue.value.toDouble());
    }
  });
}

void _mapPosition(NodeProperties nodeProperties, EdgeValue position) {
  _mapEdgeValue(position, (YGEdge edge, UnitValue unitValue) {
    if (unitValue.type == UnitType.REAL) {
      nodeProperties.setPosition(edge, unitValue.value.toDouble());
    } else {
      nodeProperties.setPositionPercent(edge, unitValue.value.toDouble());
    }
  });
}

void _mapEdgeValue(EdgeValue edgeValue, Function apply) {
  if (edgeValue.all != null) {
    apply(YGEdge.YGEdgeAll, edgeValue.all);
  }
  if (edgeValue.bottom != null) {
    apply(YGEdge.YGEdgeBottom, edgeValue.bottom);
  }
  if (edgeValue.end != null) {
    apply(YGEdge.YGEdgeEnd, edgeValue.end);
  }
  if (edgeValue.horizontal != null) {
    apply(YGEdge.YGEdgeHorizontal, edgeValue.horizontal);
  }
  if (edgeValue.left != null) {
    apply(YGEdge.YGEdgeLeft, edgeValue.left);
  }
  if (edgeValue.right != null) {
    apply(YGEdge.YGEdgeRight, edgeValue.right);
  }
  if (edgeValue.start != null) {
    apply(YGEdge.YGEdgeStart, edgeValue.start);
  }
  if (edgeValue.top != null) {
    apply(YGEdge.YGEdgeTop, edgeValue.top);
  }
  if (edgeValue.vertical != null) {
    apply(YGEdge.YGEdgeVertical, edgeValue.vertical);
  }
}
