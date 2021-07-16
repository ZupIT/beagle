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

import 'package:beagle/beagle.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('Given an Enum', () {
    group(
        'When fromString is called by passing a list of enum values and a string that represents an enum value',
        () {
      test('Then it should return the corresponding enum value for the string',
          () {
        const stringValue = 'TWO';
        const expectedValue = FakeEnum.TWO;

        final evaluatedValue =
            EnumUtils.fromString(FakeEnum.values, stringValue);

        expect(evaluatedValue, expectedValue);
      });
    });

    group(
        'When fromString is called by passing a list of enum values and a string that does not represents an enum value',
        () {
      test('Then it should return null', () {
        const stringValue = 'UONE';

        final evaluatedValue =
            EnumUtils.fromString(FakeEnum.values, stringValue);

        expect(evaluatedValue, isNull);
      });
    });

    group('When getEnumValueName is called', () {
      test('Then it should return a string containing only the value name', () {
        final evaluatedValueName =
            EnumUtils.getEnumValueName(FakeEnum.thisIsTheFour);

        const expectedValueName = 'thisIsTheFour';

        expect(evaluatedValueName, expectedValueName);
      });
    });

    group('When getEnumValueNameInKebabCase is called', () {
      test(
          'Then it should return a string containing only the value name in kebab case',
          () {
        final evaluatedValueName =
            EnumUtils.getEnumValueNameInKebabCase(FakeEnum.thisIsTheFour);

        const expectedValueName = 'this-is-the-four';

        expect(evaluatedValueName, expectedValueName);
      });
    });
  });
}

enum FakeEnum { ONE, TWO, THREE, thisIsTheFour }
