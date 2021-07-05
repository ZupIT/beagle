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
import 'package:flutter/material.dart';
import 'package:flutter/src/painting/text_style.dart';

import 'button_one_style.dart';

class FakeDesignSystem extends BeagleDesignSystem {
  @override
  BeagleButtonStyle buttonStyle(String id) {
    BeagleButtonStyle style;
    switch (id) {
      case 'button-one':
        style = ButtonOneStyle();
        break;
    }

    return style;
  }

  @override
  String image(String id) {
    return null;
  }

  @override
  TextStyle textStyle(String id) {
    TextStyle style;
    switch (id) {
      case 'text-one':
        style = const TextStyle(
          color: Colors.black,
          backgroundColor: Colors.indigo,
        );
        break;
    }

    return style;
  }
}
