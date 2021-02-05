/*
 *
 *  Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import 'package:beagle/utils/enum.dart';

enum UnitType { REAL, PERCENT }

enum FlexDirection { COLUMN, ROW, COLUMN_REVERSE, ROW_REVERSE }

enum FlexWrap { NO_WRAP, WRAP, WRAP_REVERSE }

enum JustifyContent {
  FLEX_START,
  CENTER,
  FLEX_END,
  SPACE_BETWEEN,
  SPACE_AROUND,
  SPACE_EVENLY
}

enum AlignContent {
  FLEX_START,
  CENTER,
  FLEX_END,
  SPACE_BETWEEN,
  SPACE_AROUND,
  STRETCH
}

enum AlignItems { FLEX_START, CENTER, FLEX_END, BASELINE, STRETCH }

enum AlignSelf { FLEX_START, CENTER, FLEX_END, BASELINE, STRETCH, AUTO }

enum FlexDisplay { FLEX, NONE }

enum FlexPosition { ABSOLUTE, RELATIVE }

class BeagleFlex {
  BeagleFlex(
      {this.alignContent,
      this.alignItems,
      this.alignSelf,
      this.basis,
      this.flex,
      this.flexDirection,
      this.flexWrap,
      this.grow,
      this.justifyContent,
      this.shrink});

  BeagleFlex.fromMap(Map<String, dynamic> map) {
    if (map.containsKey('flexDirection')) {
      flexDirection =
          EnumUtils.fromString(FlexDirection.values, map['flexDirection']);
    }
    if (map.containsKey('flexWrap')) {
      flexWrap = EnumUtils.fromString(FlexWrap.values, map['flexWrap']);
    }
    if (map.containsKey('justifyContent')) {
      justifyContent =
          EnumUtils.fromString(JustifyContent.values, map['justifyContent']);
    }
    if (map.containsKey('alignItems')) {
      alignItems = EnumUtils.fromString(AlignItems.values, map['alignItems']);
    }
    if (map.containsKey('alignSelf')) {
      alignSelf = EnumUtils.fromString(AlignSelf.values, map['alignSelf']);
    }
    if (map.containsKey('alignContent')) {
      alignContent =
          EnumUtils.fromString(AlignContent.values, map['alignContent']);
    }
    if (map.containsKey('basis')) {
      basis = UnitValue.fromMap(map['basis']);
    }
    if (map.containsKey('flex')) {
      flex = map['flex'];
    }
    if (map.containsKey('grow')) {
      grow = map['grow'];
    }
    if (map.containsKey('shrink')) {
      shrink = map['shrink'];
    }
  }

  FlexDirection flexDirection = FlexDirection.COLUMN;
  FlexWrap flexWrap = FlexWrap.NO_WRAP;
  JustifyContent justifyContent = JustifyContent.FLEX_START;
  AlignItems alignItems = AlignItems.STRETCH;
  AlignSelf alignSelf = AlignSelf.AUTO;
  AlignContent alignContent = AlignContent.FLEX_START;
  UnitValue basis = UnitValue(value: 0, type: UnitType.REAL);
  num flex = 0;
  num grow = 0;
  num shrink = 1;
}

class UnitValue {
  UnitValue({this.value, this.type});

  UnitValue.fromMap(Map<String, dynamic> map) {
    value = map['value'];
    type = EnumUtils.fromString(UnitType.values, map['type']);
  }

  num value;
  UnitType type;
}

class BeagleSize {
  BeagleSize(
      {this.width,
      this.height,
      this.maxWidth,
      this.maxHeight,
      this.minWidth,
      this.minHeight,
      this.aspectRatio});

  BeagleSize.fromMap(Map<String, dynamic> map) {
    if (map.containsKey('width')) {
      width = UnitValue.fromMap(map['width']);
    }
    if (map.containsKey('height')) {
      height = UnitValue.fromMap(map['height']);
    }
    if (map.containsKey('maxWidth')) {
      maxWidth = UnitValue.fromMap(map['maxWidth']);
    }
    if (map.containsKey('maxHeight')) {
      maxHeight = UnitValue.fromMap(map['maxHeight']);
    }
    if (map.containsKey('minWidth')) {
      minWidth = UnitValue.fromMap(map['minWidth']);
    }
    if (map.containsKey('minHeight')) {
      minHeight = UnitValue.fromMap(map['minHeight']);
    }
    if (map.containsKey('aspectRatio')) {
      aspectRatio = map['aspectRatio'];
    }
  }

  UnitValue width;
  UnitValue height;
  UnitValue maxWidth;
  UnitValue maxHeight;
  UnitValue minWidth;
  UnitValue minHeight;
  num aspectRatio;
}

class EdgeValue {
  EdgeValue(
      {this.all,
      this.bottom,
      this.end,
      this.horizontal,
      this.left,
      this.right,
      this.start,
      this.top,
      this.vertical});

  EdgeValue.fromMap(Map<String, dynamic> map) {
    if (map.containsKey('left')) {
      left = UnitValue.fromMap(map['left']);
    }
    if (map.containsKey('top')) {
      top = UnitValue.fromMap(map['top']);
    }
    if (map.containsKey('right')) {
      right = UnitValue.fromMap(map['right']);
    }
    if (map.containsKey('bottom')) {
      bottom = UnitValue.fromMap(map['bottom']);
    }
    if (map.containsKey('start')) {
      start = UnitValue.fromMap(map['start']);
    }
    if (map.containsKey('end')) {
      end = UnitValue.fromMap(map['end']);
    }
    if (map.containsKey('horizontal')) {
      horizontal = UnitValue.fromMap(map['horizontal']);
    }
    if (map.containsKey('vertical')) {
      vertical = UnitValue.fromMap(map['vertical']);
    }
    if (map.containsKey('all')) {
      all = UnitValue.fromMap(map['all']);
    }
  }

  UnitValue left;
  UnitValue top;
  UnitValue right;
  UnitValue bottom;
  UnitValue start;
  UnitValue end;
  UnitValue horizontal;
  UnitValue vertical;
  UnitValue all;
}

class CornerRadius {
  CornerRadius({this.radius});

  CornerRadius.fromMap(Map<String, dynamic> map) {
    if (map.containsKey('radius')) {
      radius = map['radius'];
    }
  }

  num radius;
}

class BeagleStyle {
  BeagleStyle(
      {this.backgroundColor,
      this.borderColor,
      this.borderWidth,
      this.cornerRadius,
      this.display,
      this.flex,
      this.margin,
      this.padding,
      this.position,
      this.positionType,
      this.size});

  BeagleStyle.fromMap(Map<String, dynamic> map) {
    if (map.containsKey('backgroundColor')) {
      backgroundColor = map['backgroundColor'];
    }
    if (map.containsKey('cornerRadius')) {
      cornerRadius = CornerRadius.fromMap(map['cornerRadius']);
    }
    if (map.containsKey('flex')) {
      flex = BeagleFlex.fromMap(map['flex']);
    }
    if (map.containsKey('positionType')) {
      positionType =
          EnumUtils.fromString(FlexPosition.values, map['positionType']);
    }
    if (map.containsKey('display')) {
      display = EnumUtils.fromString(FlexDisplay.values, map['display']);
    }
    if (map.containsKey('size')) {
      size = BeagleSize.fromMap(map['size']);
    }
    if (map.containsKey('margin')) {
      margin = EdgeValue.fromMap(map['margin']);
    }
    if (map.containsKey('padding')) {
      padding = EdgeValue.fromMap(map['padding']);
    }
    if (map.containsKey('position')) {
      position = EdgeValue.fromMap(map['position']);
    }
    if (map.containsKey('borderWidth')) {
      borderWidth = map['borderWidth'];
    }
    if (map.containsKey('borderColor')) {
      borderColor = map['borderColor'];
    }
  }

  String backgroundColor;
  CornerRadius cornerRadius;
  BeagleFlex flex;
  FlexPosition positionType = FlexPosition.RELATIVE;
  FlexDisplay display = FlexDisplay.FLEX;
  BeagleSize size;
  EdgeValue margin;
  EdgeValue padding;
  EdgeValue position;
  num borderWidth;
  String borderColor;
}
