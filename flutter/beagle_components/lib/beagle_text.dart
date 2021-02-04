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

import 'package:beagle/setup/beagle_design_system.dart';
import 'package:beagle/utils/color.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class BeagleText extends StatelessWidget {
  const BeagleText({
    Key key,
    this.text,
    this.textColor,
    this.alignment,
    this.styleId,
    DesignSystem designSystem,
  })  : _designSystem = designSystem,
        super(key: key);

  final String text;
  final String textColor;
  final TextAlignment alignment;
  final String styleId;
  final DesignSystem _designSystem;

  @override
  Widget build(BuildContext context) {
    return Text(
      text,
      textAlign: getTextAlign(alignment),
      style: getTextStyle(),
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
      return null;
    }
  }

  Color getTextColor(String color) {
    return color != null ? HexColor(color) : null;
  }

  TextStyle getTextStyle() {
    var textStyle = _designSystem?.textStyle(styleId) ?? const TextStyle();
    if (textColor != null && textColor.isNotEmpty) {
      textStyle = textStyle.copyWith(color: getTextColor(textColor));
    }
    return textStyle;
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
