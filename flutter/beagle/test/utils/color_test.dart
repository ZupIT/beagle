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

import 'package:beagle/utils/color.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('Given any color', () {
    group('When hexColor is called to #1B3', () {
      test('Then should return #FF11BB33', () {
        const colorRGB = '#1B3';

        final result = HexColor(colorRGB);

        expect(result.value, 0xFF11BB33);
      });
    });

    group('When hexColor is called to #6F32', () {
      test('Then should return #2266FF33', () {
        const colorRGBA = '#6F32';

        final result = HexColor(colorRGBA);

        expect(result.value, 0x2266FF33);
      });
    });

    group('When hexColor is called to #A3D256', () {
      test('Then should return #FFA3D256', () {
        const colorRGB = '#A3D256';

        final result = HexColor(colorRGB);

        expect(result.value, 0xFFA3D256);
      });
    });

    group('When hexColor is called to #ABC12330', () {
      test('Then should return #30ABC123', () {
        const colorRGBA = '#ABC12330';

        final result = HexColor(colorRGBA);

        expect(result.value, 0x30ABC123);
      });
    });
  });
}
