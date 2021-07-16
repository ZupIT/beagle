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

import 'package:flutter_test/flutter_test.dart';
import 'package:beagle/src/utils/string_utils.dart';

void main() {
  group('Given an all capitalized String', () {
    group('When toKebabCase is called', () {
      test('Then it should convert to kebab case correctly', () {
        final returnedString = 'TEST'.toKebabCase();

        const expectedString = 'test';

        expect(returnedString, expectedString);
      });
    });
  });

  group('Given an all lowercase String', () {
    group('When toKebabCase is called', () {
      test('Then it should convert to kebab case correctly', () {
        final returnedString = 'test'.toKebabCase();

        const expectedString = 'test';

        expect(returnedString, expectedString);
      });
    });
  });

  group('Given a camel case String starting with a lower case letter', () {
    group('When toKebabCase is called', () {
      test('Then it should convert to kebab case correctly', () {
        final returnedString = 'camelCase'.toKebabCase();

        const expectedString = 'camel-case';

        expect(returnedString, expectedString);
      });
    });
  });

  group('Given a camel case String starting with a capitalized letter', () {
    group('When toKebabCase is called', () {
      test('Then it should convert to kebab case correctly', () {
        final returnedString = 'CamelCase'.toKebabCase();

        const expectedString = 'camel-case';

        expect(returnedString, expectedString);
      });
    });
  });

  group('Given a mixed String with spaces, underscores and hyphens', () {
    group('When toKebabCase is called', () {
      test('Then it should convert to kebab case correctly', () {
        final returnedString =
            'some-MIXEDString With spaces_underscores-and-hyphens'
                .toKebabCase();

        const expectedString =
            'some-mixed-string-with-spaces-underscores-and-hyphens';

        expect(returnedString, expectedString);
      });
    });
  });
}
