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
import 'package:flutter/rendering.dart';

class AppDesignSystem extends BeagleDesignSystem {
  Map<String, String> imageMap = {
    'bus': 'images/bus.png',
    'car': 'images/car.png',
    'person': 'images/person.png',
    'beagle': 'images/beagle.png',
    'delete': 'images/delete.png',
  };

  @override
  String image(String id) {
    return imageMap[id];
  }

  @override
  BeagleButtonStyle buttonStyle(String id) {
    return null;
  }

  @override
  TextStyle textStyle(String id) {
    return null;
  }
}
