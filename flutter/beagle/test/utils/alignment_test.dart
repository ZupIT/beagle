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
import 'package:beagle/utils/alignment.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';

final column = beagle.Flex(
  flexDirection: beagle.FlexDirection.COLUMN,
);
final row = beagle.Flex(
  flexDirection: beagle.FlexDirection.ROW,
);
final columnReverse = beagle.Flex(
  flexDirection: beagle.FlexDirection.COLUMN_REVERSE,
);
final rowReverse = beagle.Flex(
  flexDirection: beagle.FlexDirection.ROW_REVERSE,
);

void _assertAlignmentNullPadding(beagle.Flex flex, Alignment matcher) {
  const padding = EdgeInsets.all(null);
  _assertAlignment(padding, flex, matcher);
}

void _assertAlignmentLeftPadding(beagle.Flex flex, Alignment matcher) {
  const padding = EdgeInsets.fromLTRB(0, null, null, null);
  _assertAlignment(padding, flex, matcher);
}

void _assertAlignmentTopPadding(beagle.Flex flex, Alignment matcher) {
  const padding = EdgeInsets.fromLTRB(null, 0, null, null);
  _assertAlignment(padding, flex, matcher);
}

void _assertAlignmentRightPadding(beagle.Flex flex, Alignment matcher) {
  const padding = EdgeInsets.fromLTRB(null, null, 0, null);
  _assertAlignment(padding, flex, matcher);
}

void _assertAlignmentBottomPadding(beagle.Flex flex, Alignment matcher) {
  const padding = EdgeInsets.fromLTRB(null, null, null, 0);
  _assertAlignment(padding, flex, matcher);
}

void _assertAlignment(
  EdgeInsets padding,
  beagle.Flex flex,
  Alignment matcher,
) {
  final result = getAlignment(padding, flex);
  expect(result, matcher);
}

void main() {
  group('Alignment with COLUMN and ROW', () {
    test(
        'GIVEN without padding WHEN getAlignment '
        'THEN should return topLeft', () {
      _assertAlignmentNullPadding(column, Alignment.topLeft);
      _assertAlignmentNullPadding(row, Alignment.topLeft);
    });

    test(
        'GIVEN padding left WHEN getAlignment '
        'THEN should return topLeft', () {
      _assertAlignmentLeftPadding(column, Alignment.topLeft);
      _assertAlignmentLeftPadding(row, Alignment.topLeft);
    });

    test(
        'GIVEN padding top WHEN getAlignment '
        'THEN should return topLeft', () {
      _assertAlignmentTopPadding(column, Alignment.topLeft);
      _assertAlignmentTopPadding(row, Alignment.topLeft);
    });

    test(
        'GIVEN padding right WHEN getAlignment '
        'THEN should return topRight', () {
      _assertAlignmentRightPadding(column, Alignment.topRight);
      _assertAlignmentRightPadding(row, Alignment.topRight);
    });

    test(
        'GIVEN padding bottom WHEN getAlignment '
        'THEN should return bottomLeft', () {
      _assertAlignmentBottomPadding(column, Alignment.bottomLeft);
      _assertAlignmentBottomPadding(row, Alignment.bottomLeft);
    });
  });

  group('Alignment with COLUMN_REVERSE', () {
    test(
        'GIVEN without padding WHEN getAlignment '
        'THEN should return bottomLeft', () {
      _assertAlignmentNullPadding(columnReverse, Alignment.bottomLeft);
    });

    test(
        'GIVEN padding left WHEN getAlignment '
        'THEN should return bottomLeft', () {
      _assertAlignmentLeftPadding(columnReverse, Alignment.bottomLeft);
    });

    test(
        'GIVEN padding top WHEN getAlignment '
        'THEN should return topLeft', () {
      _assertAlignmentTopPadding(columnReverse, Alignment.topLeft);
    });

    test(
        'GIVEN padding right WHEN getAlignment '
        'THEN should return bottomRight', () {
      _assertAlignmentRightPadding(columnReverse, Alignment.bottomRight);
    });

    test(
        'GIVEN padding bottom WHEN getAlignment '
        'THEN should return bottomLeft', () {
      _assertAlignmentBottomPadding(columnReverse, Alignment.bottomLeft);
    });
  });

  group('Alignment with ROW_REVERSE', () {
    test(
        'GIVEN without padding WHEN getAlignment '
        'THEN should return topRight', () {
      _assertAlignmentNullPadding(rowReverse, Alignment.topRight);
    });

    test(
        'GIVEN padding left WHEN getAlignment '
        'THEN should return topLeft', () {
      _assertAlignmentLeftPadding(rowReverse, Alignment.topLeft);
    });

    test(
        'GIVEN padding top WHEN getAlignment '
        'THEN should return topRight', () {
      _assertAlignmentTopPadding(rowReverse, Alignment.topRight);
    });

    test(
        'GIVEN padding right WHEN getAlignment '
        'THEN should return topRight', () {
      _assertAlignmentRightPadding(rowReverse, Alignment.topRight);
    });

    test(
        'GIVEN padding bottom WHEN getAlignment '
        'THEN should return bottomRight', () {
      _assertAlignmentBottomPadding(rowReverse, Alignment.bottomRight);
    });
  });

  group('Alignment with any FlexDirection', () {
    test(
        'GIVEN padding left and top WHEN getAlignment '
        'THEN should return topLeft', () {
      const padding = EdgeInsets.fromLTRB(0, 0, null, null);

      _assertAlignment(padding, column, Alignment.topLeft);
      _assertAlignment(padding, row, Alignment.topLeft);
      _assertAlignment(padding, columnReverse, Alignment.topLeft);
      _assertAlignment(padding, rowReverse, Alignment.topLeft);
    });

    test(
        'GIVEN padding top and right WHEN getAlignment '
        'THEN should return topRight', () {
      const padding = EdgeInsets.fromLTRB(null, 0, 0, null);

      _assertAlignment(padding, column, Alignment.topRight);
      _assertAlignment(padding, row, Alignment.topRight);
      _assertAlignment(padding, columnReverse, Alignment.topRight);
      _assertAlignment(padding, rowReverse, Alignment.topRight);
    });

    test(
        'GIVEN padding right and bottom WHEN getAlignment '
        'THEN should return bottomRight', () {
      const padding = EdgeInsets.fromLTRB(null, null, 0, 0);

      _assertAlignment(padding, column, Alignment.bottomRight);
      _assertAlignment(padding, row, Alignment.bottomRight);
      _assertAlignment(padding, columnReverse, Alignment.bottomRight);
      _assertAlignment(padding, rowReverse, Alignment.bottomRight);
    });

    test(
        'GIVEN padding bottom and left WHEN getAlignment '
        'THEN should return bottomLeft', () {
      const padding = EdgeInsets.fromLTRB(0, null, null, 0);

      _assertAlignment(padding, column, Alignment.bottomLeft);
      _assertAlignment(padding, row, Alignment.bottomLeft);
      _assertAlignment(padding, columnReverse, Alignment.bottomLeft);
      _assertAlignment(padding, rowReverse, Alignment.bottomLeft);
    });
  });
}
