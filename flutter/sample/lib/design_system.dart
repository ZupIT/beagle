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

class AppDesignSystem extends DesignSystem {
  @override
  String image(String id) {
    if (id == 'bus') {
      return 'images/bus.png';
    } else if (id == 'car') {
      return 'images/car.png';
    } else if (id == 'person') {
      return 'images/person.png';
    } else if (id == 'beagle') {
      return 'images/beagle.png';
    }

    return null;
  }

  @override
  BeagleButtonStyle buttonStyle(String id) {
    return null;
  }
}
