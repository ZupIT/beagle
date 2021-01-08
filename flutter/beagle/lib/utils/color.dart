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

import 'package:flutter/widgets.dart';

class HexColor extends Color {
  HexColor(final String hexColor) : super(_getColorFromHex(hexColor));

  static int _getColorFromHex(String hexColor) {
    hexColor = hexColor.toUpperCase().replaceAll('#', '');
    switch (hexColor.length) {
      case 3:
        hexColor = _formatHexRGBColor(hexColor);
        break;
      case 4:
        hexColor = _formatHexRGBAColor(hexColor);
        break;
      case 6:
        hexColor = 'FF$hexColor';
        break;
      case 8:
        hexColor = _formatHexColorAlpha(hexColor);
        break;
    }
    return int.parse(hexColor, radix: 16);
  }

  static String _formatHexRGBColor(String hexColor) {
    final regExp =
        RegExp(r'^([0-9A-F])([0-9A-F])([0-9A-F])?$', caseSensitive: false);
    return hexColor.replaceAllMapped(
        regExp,
        (match) => 'FF${match.group(1)}${match.group(1)}'
            '${match.group(2)}${match.group(2)}'
            '${match.group(3)}${match.group(3)}');
  }

  static String _formatHexColorAlpha(String hexColor) {
    final regExp =
        RegExp(r'^([0-9A-F]{6})([0-9A-F]{2})$', caseSensitive: false);
    return hexColor.replaceAllMapped(
        regExp, (match) => '${match.group(2)}${match.group(1)}');
  }

  static String _formatHexRGBAColor(String hexColor) {
    final regExp = RegExp(r'^([0-9A-F])([0-9A-F])([0-9A-F])([0-9A-F])?$',
        caseSensitive: false);
    return hexColor.replaceAllMapped(
        regExp,
        (match) => '${match.group(4)}${match.group(4)}'
            '${match.group(1)}${match.group(1)}'
            '${match.group(2)}${match.group(2)}'
            '${match.group(3)}${match.group(3)}');
  }
}
