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

const Map<FlexPosition, YGPositionType> _flexPositionMap = {
  FlexPosition.ABSOLUTE: YGPositionType.YGPositionTypeAbsolute,
  FlexPosition.RELATIVE: YGPositionType.YGPositionTypeRelative,
};

YGPositionType _mapPositionType(FlexPosition flexPosition) {
  return _flexPositionMap[flexPosition] ?? YGPositionType.YGPositionTypeStatic;
}

const Map<FlexDisplay, YGDisplay> _flexDisplayMap = {
  FlexDisplay.FLEX: YGDisplay.YGDisplayFlex,
  FlexDisplay.NONE: YGDisplay.YGDisplayNone,
};

YGDisplay _mapDisplay(FlexDisplay flexDisplay) {
  return _flexDisplayMap[flexDisplay] ?? YGDisplay.YGDisplayFlex;
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

const Map<AlignContent, YGAlign> _alignContentMap = {
  AlignContent.FLEX_START: YGAlign.YGAlignFlexStart,
  AlignContent.CENTER: YGAlign.YGAlignFlexStart,
  AlignContent.FLEX_END: YGAlign.YGAlignFlexEnd,
  AlignContent.STRETCH: YGAlign.YGAlignStretch,
  AlignContent.SPACE_AROUND: YGAlign.YGAlignSpaceAround,
  AlignContent.SPACE_BETWEEN: YGAlign.YGAlignSpaceBetween,
};

YGAlign _mapAlignContent(AlignContent alignContent) {
  return _alignContentMap[alignContent] ?? YGAlign.YGAlignFlexStart;
}

const Map<AlignItems, YGAlign> _alignItemsMap = {
  AlignItems.STRETCH: YGAlign.YGAlignStretch,
  AlignItems.FLEX_START: YGAlign.YGAlignFlexStart,
  AlignItems.CENTER: YGAlign.YGAlignCenter,
  AlignItems.FLEX_END: YGAlign.YGAlignFlexEnd,
  AlignItems.BASELINE: YGAlign.YGAlignBaseline,
};

YGAlign _mapAlignItems(AlignItems alignItems) {
  return _alignItemsMap[alignItems] ?? YGAlign.YGAlignStretch;
}

const Map<AlignSelf, YGAlign> _alignSelfMap = {
  AlignSelf.AUTO: YGAlign.YGAlignAuto,
  AlignSelf.FLEX_START: YGAlign.YGAlignFlexStart,
  AlignSelf.CENTER: YGAlign.YGAlignCenter,
  AlignSelf.FLEX_END: YGAlign.YGAlignFlexEnd,
  AlignSelf.STRETCH: YGAlign.YGAlignStretch,
  AlignSelf.BASELINE: YGAlign.YGAlignBaseline,
};

YGAlign _mapAlignSelf(AlignSelf alignSelf) {
  return _alignSelfMap[alignSelf] ?? YGAlign.YGAlignAuto;
}

const Map<FlexDirection, YGFlexDirection> _flexDirectionMap = {
  FlexDirection.COLUMN: YGFlexDirection.YGFlexDirectionColumn,
  FlexDirection.ROW: YGFlexDirection.YGFlexDirectionRow,
  FlexDirection.COLUMN_REVERSE: YGFlexDirection.YGFlexDirectionColumnReverse,
  FlexDirection.ROW_REVERSE: YGFlexDirection.YGFlexDirectionRowReverse,
};

YGFlexDirection _mapFlexDirection(FlexDirection flexDirection) {
  return _flexDirectionMap[flexDirection] ?? YGFlexDirection.YGFlexDirectionColumn;
}

const Map<FlexWrap, YGWrap> _flexWrapMap = {
  FlexWrap.NO_WRAP: YGWrap.YGWrapNoWrap,
  FlexWrap.WRAP: YGWrap.YGWrapWrap,
  FlexWrap.WRAP_REVERSE: YGWrap.YGWrapWrapReverse,
};

YGWrap _mapWrap(FlexWrap flexWrap) {
  return _flexWrapMap[flexWrap] ?? YGWrap.YGWrapNoWrap;
}

const Map<JustifyContent, YGJustify> _justifyContentMap = {
  JustifyContent.FLEX_START: YGJustify.YGJustifyFlexStart,
  JustifyContent.CENTER: YGJustify.YGJustifyCenter,
  JustifyContent.FLEX_END: YGJustify.YGJustifyFlexEnd,
  JustifyContent.SPACE_BETWEEN: YGJustify.YGJustifySpaceBetween,
  JustifyContent.SPACE_AROUND: YGJustify.YGJustifySpaceAround,
  JustifyContent.SPACE_EVENLY: YGJustify.YGJustifySpaceEvenly,
};

YGJustify _mapJustify(JustifyContent justifyContent) {
  return _justifyContentMap[justifyContent] ?? YGJustify.YGJustifyFlexStart;
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
