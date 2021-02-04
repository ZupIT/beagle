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

import 'package:beagle/model/beagle_button_style.dart';
import 'package:beagle/setup/beagle_design_system.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class BeagleButton extends StatelessWidget {
  const BeagleButton({
    Key key,
    this.text,
    this.onPress,
    this.enabled,
    this.styleId,
    DesignSystem designSystem,
  })  : _designSystem = designSystem,
        super(key: key);

  final String text;
  final String styleId;
  final Function onPress;
  final bool enabled;
  final DesignSystem _designSystem;

  BeagleButtonStyle get _buttonStyle => _designSystem?.buttonStyle(styleId);

  @override
  Widget build(BuildContext context) {
    final _platform = Theme.of(context).platform;

    return _platform == TargetPlatform.iOS
        ? buildCupertinoWidget()
        : buildMaterialWidget();
  }

  Widget buildCupertinoWidget() {
    return CupertinoButton(
      color: _buttonStyle?.iosButtonStyle?.color,
      disabledColor: _buttonStyle?.iosButtonStyle?.disabledColor ??
          CupertinoColors.quaternarySystemFill,
      padding: _buttonStyle?.iosButtonStyle?.padding,
      borderRadius: _buttonStyle?.iosButtonStyle?.borderRadius ??
          const BorderRadius.all(Radius.circular(8)),
      pressedOpacity: _buttonStyle?.iosButtonStyle?.pressedOpacity ?? 0.4,
      onPressed: getOnPressedFunction(),
      child: buildButtonChild(),
    );
  }

  Widget buildMaterialWidget() {
    return ElevatedButton(
      style: _buttonStyle?.androidButtonStyle,
      onPressed: getOnPressedFunction(),
      child: buildButtonChild(),
    );
  }

  Widget buildButtonChild() {
    return Text(
      text,
      style: _buttonStyle?.buttonTextStyle,
    );
  }

  Function getOnPressedFunction() {
    return (enabled ?? true) ? onPress : null;
  }
}
