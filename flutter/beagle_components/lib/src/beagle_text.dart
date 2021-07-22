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
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

/// A Text widget that displays a string of text with single style.
class BeagleText extends StatelessWidget {
  const BeagleText({
    Key key,
    this.text,
    this.textColor,
    this.alignment,
    this.styleId,
  }) : super(key: key);

  /// The text to display.
  final dynamic text;

  /// This is a string value and it must be filled as HEX (Hexadecimal).
  final String textColor;

  /// Defines the content alignment inside the widget.
  final TextAlignment alignment;

  /// Reference a native style in your local styles file to be applied on this
  /// Text.
  final String styleId;

  @override
  Widget build(BuildContext context) {
    final beagleText = Text(
      text.toString() ?? '',
      textAlign: getTextAlign(alignment),
      style: getTextStyle(),
    );
    return alignment == TextAlignment.CENTER
        ? Center(child: beagleText)
        : beagleText;
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
    final designSystem = beagleServiceLocator<BeagleDesignSystem>();
    var textStyle = designSystem?.textStyle(styleId) ?? const TextStyle();
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
