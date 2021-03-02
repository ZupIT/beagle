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
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('Given a style map', () {
    group('When I call BeagleStyle.fromMap passing the style map', () {
      test(
          'Then it should create a BeagleStyle object mapping the values correctly',
          () {
        final styleMap = {
          'backgroundColor': '#FFFFF',
          'cornerRadius': {'radius': 5},
          'flex': {
            'flex': 1,
            'flexDirection': 'ROW',
            'flexWrap': 'WRAP',
            'justifyContent': 'CENTER',
            'alignItems': 'BASELINE',
            'alignSelf': 'FLEX_END',
            'alignContent': 'SPACE_AROUND',
            'basis': {'value': 1, 'type': 'REAL'},
            'grow': 1.1,
            'shrink': 1.2,
          },
          'positionType': 'ABSOLUTE',
          'display': 'NONE',
          'size': {
            'width': {'value': 50, 'type': 'REAL'},
            'height': {'value': 50.5, 'type': 'REAL'},
            'maxWidth': {'value': 100.8, 'type': 'PERCENT'},
            'maxHeight': {'value': 100, 'type': 'REAL'},
            'minWidth': {'value': 35.8, 'type': 'PERCENT'},
            'minHeight': {'value': 35.5, 'type': 'REAL'},
            'aspectRatio': 5.34
          },
          'margin': {
            'left': {'value': 1, 'type': 'REAL'},
            'right': {'value': 2, 'type': 'REAL'},
            'top': {'value': 3, 'type': 'REAL'},
            'bottom': {'value': 4, 'type': 'REAL'},
            'start': {'value': 5, 'type': 'PERCENT'},
            'end': {'value': 6, 'type': 'PERCENT'},
            'horizontal': {'value': 7, 'type': 'PERCENT'},
            'vertical': {'value': 8, 'type': 'PERCENT'},
            'all': {'value': 9, 'type': 'PERCENT'}
          },
          'padding': {
            'all': {'value': 10, 'type': 'REAL'}
          },
          'position': {
            'all': {'value': 11, 'type': 'REAL'}
          },
          'borderWidth': 3,
          'borderColor': '#00000'
        };
        final style = BeagleStyle.fromMap(styleMap);

        expect(style.backgroundColor, '#FFFFF');
        expect(style.cornerRadius.radius, 5);

        expect(style.flex.flex, 1);
        expect(style.flex.flexDirection, FlexDirection.ROW);
        expect(style.flex.flexWrap, FlexWrap.WRAP);
        expect(style.flex.justifyContent, JustifyContent.CENTER);
        expect(style.flex.alignItems, AlignItems.BASELINE);
        expect(style.flex.alignSelf, AlignSelf.FLEX_END);
        expect(style.flex.alignContent, AlignContent.SPACE_AROUND);
        expect(style.flex.basis.value, 1.0);
        expect(style.flex.basis.type, UnitType.REAL);
        expect(style.flex.grow, 1.1);
        expect(style.flex.shrink, 1.2);

        expect(style.positionType, FlexPosition.ABSOLUTE);
        expect(style.display, FlexDisplay.NONE);

        expect(style.size.width.value, 50);
        expect(style.size.width.type, UnitType.REAL);
        expect(style.size.height.value, 50.5);
        expect(style.size.height.type, UnitType.REAL);
        expect(style.size.maxWidth.value, 100.8);
        expect(style.size.maxWidth.type, UnitType.PERCENT);
        expect(style.size.maxHeight.value, 100);
        expect(style.size.maxHeight.type, UnitType.REAL);
        expect(style.size.minWidth.value, 35.8);
        expect(style.size.minWidth.type, UnitType.PERCENT);
        expect(style.size.minHeight.value, 35.5);
        expect(style.size.minHeight.type, UnitType.REAL);
        expect(style.size.aspectRatio, 5.34);

        expect(style.margin.left.value, 1);
        expect(style.margin.left.type, UnitType.REAL);
        expect(style.margin.right.value, 2);
        expect(style.margin.right.type, UnitType.REAL);
        expect(style.margin.top.value, 3);
        expect(style.margin.top.type, UnitType.REAL);
        expect(style.margin.bottom.value, 4);
        expect(style.margin.bottom.type, UnitType.REAL);
        expect(style.margin.start.value, 5);
        expect(style.margin.start.type, UnitType.PERCENT);
        expect(style.margin.end.value, 6);
        expect(style.margin.end.type, UnitType.PERCENT);
        expect(style.margin.horizontal.value, 7);
        expect(style.margin.horizontal.type, UnitType.PERCENT);
        expect(style.margin.vertical.value, 8);
        expect(style.margin.vertical.type, UnitType.PERCENT);
        expect(style.margin.all.value, 9);
        expect(style.margin.all.type, UnitType.PERCENT);

        expect(style.padding.all.value, 10);
        expect(style.padding.all.type, UnitType.REAL);

        expect(style.position.all.value, 11);
        expect(style.position.all.type, UnitType.REAL);

        expect(style.borderWidth, 3);
        expect(style.borderColor, '#00000');
      });
    });
  });
}
