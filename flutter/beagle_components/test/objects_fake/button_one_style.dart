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

import 'dart:ui';

import 'package:beagle/beagle.dart';
import 'package:flutter/material.dart';

class ButtonOneStyle extends BeagleButtonStyle {
  @override
  BeagleIosButtonStyle get iosButtonStyle => BeagleIosButtonStyle(
        padding: const EdgeInsets.all(16),
        borderRadius: const BorderRadius.all(Radius.circular(16)),
        pressedOpacity: 1,
        color: Colors.indigo,
        disabledColor: Colors.lightGreenAccent,
      );

  @override
  ButtonStyle get androidButtonStyle => ButtonStyle(
        backgroundColor: MaterialStateProperty.all<Color>(Colors.black),
      );

  @override
  TextStyle get buttonTextStyle => const TextStyle(color: Colors.amber);
}
