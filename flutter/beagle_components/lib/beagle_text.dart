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
import 'package:flutter/material.dart';

class BeagleText extends StatelessWidget {
  const BeagleText({
    Key key,
    this.text,
    this.textColor,
    this.alignment,
  }) : super(key: key);

  static const defaultTextColor = '#000000';
  static const defaultTextAlign = TextAlign.left;

  final String text;
  final String textColor;
  final TextAlignment alignment;

  @override
  Widget build(BuildContext context) {
    return Text(
      text,
      style: TextStyle(
        color: getTextColor(textColor),
      ),
      textAlign: getTextAlign(alignment),
    );
  }

  TextAlign getTextAlign(TextAlignment alignment) {
    if (alignment == TextAlignment.CENTER) {
      return TextAlign.center;
    } else if (alignment == TextAlignment.RIGHT) {
      return TextAlign.right;
    } else if (alignment == TextAlignment.LEFT) {
      return TextAlign.left;
    } else {
      return defaultTextAlign;
    }
  }

  Color getTextColor(String color) {
    return HexColor(color ?? defaultTextColor);
  }
}

enum TextAlignment {
  /// Text content is LEFT aligned inside the text view.
  ///
  LEFT,

  /// Text content is CENTER aligned inside the text view.
  ///
  CENTER,

  /// Text content is RIGHT aligned inside the text view.
  ///
  RIGHT
}
