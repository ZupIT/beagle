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

import 'package:beagle/networking/beagle_network_strategy.dart';
import 'package:beagle/utils/enum.dart';

class NetworkStrategyUtils {
  // transforms the enum NetworkStrategy into the string expected by beagle web (js)
  static String getJsStrategyName(BeagleNetworkStrategy strategy) {
    if (strategy == null) {
      return null;
    }

    final strategyNameInCamelCase = EnumUtils.name(strategy);
    /* beagle web needs the strategy name in kebab-case, we use a regex to replace the uppercase
    letters with a hyphen and the lower case equivalent. */
    final strategyNameInKebabCase = strategyNameInCamelCase.replaceAllMapped(
        RegExp('[A-Z]'), (match) => '-${match[0].toLowerCase()}');
    return strategyNameInKebabCase;
  }
}
