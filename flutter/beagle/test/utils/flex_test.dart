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

import 'package:beagle/utils/flex.dart';
import 'package:flutter/widgets.dart';
import 'package:beagle/model/beagle_style.dart' as beagle;
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('Column', () {
    test(
        'GIVEN a null flex WHEN applyFlexDirection THEN should return a '
        'column with mainAxisSize min and crossAxisAlignment stretch', () {
      const children = <Widget>[];

      final Column result = applyFlexDirection(children);

      expect(result, isA<Column>());
      expect(result.mainAxisSize, MainAxisSize.min);
      expect(result.crossAxisAlignment, CrossAxisAlignment.stretch);
      expect(result.children, children);
    });

    test(
        'GIVEN a FlexDirection.COLUMN WHEN applyFlexDirection THEN should '
        'return a column with mainAxisSize min and '
        'crossAxisAlignment stretch', () {
      const children = <Widget>[];
      final flex = beagle.Flex(flexDirection: beagle.FlexDirection.COLUMN);

      final Column result = applyFlexDirection(children, flex: flex);

      expect(result, isA<Column>());
      expect(result.mainAxisSize, MainAxisSize.min);
      expect(result.crossAxisAlignment, CrossAxisAlignment.stretch);
      expect(result.children, children);
    });

    test(
        'GIVEN a FlexDirection.COLUMN_REVERSE WHEN applyFlexDirection '
        'THEN should return a column with mainAxisSize min and '
        'crossAxisAlignment stretch and verticalDirection up', () {
      const children = <Widget>[];
      final flex =
          beagle.Flex(flexDirection: beagle.FlexDirection.COLUMN_REVERSE);

      final Column result = applyFlexDirection(children, flex: flex);

      expect(result, isA<Column>());
      expect(result.mainAxisSize, MainAxisSize.min);
      expect(result.crossAxisAlignment, CrossAxisAlignment.stretch);
      expect(result.verticalDirection, VerticalDirection.up);
      expect(result.children, children);
    });
  });

  group('Row', () {
    test(
        'GIVEN a FlexDirection.ROW WHEN applyFlexDirection THEN should return a '
            'row with crossAxisAlignment start', () {
      const children = <Widget>[];
      final flex = beagle.Flex(flexDirection: beagle.FlexDirection.ROW);

      final Row result = applyFlexDirection(children, flex: flex);

      expect(result, isA<Row>());
      expect(result.crossAxisAlignment, CrossAxisAlignment.start);
      expect(result.children, children);
    });

    test(
        'GIVEN a FlexDirection.ROW_REVERSE WHEN applyFlexDirection THEN should '
            'return a row with textDirection rtl and '
            'crossAxisAlignment start', () {
      const children = <Widget>[];
      final flex = beagle.Flex(flexDirection: beagle.FlexDirection.ROW_REVERSE);

      final Row result = applyFlexDirection(children, flex: flex);

      expect(result, isA<Row>());
      expect(result.textDirection, TextDirection.rtl);
      expect(result.crossAxisAlignment, CrossAxisAlignment.start);
      expect(result.children, children);
    });
  });
}
