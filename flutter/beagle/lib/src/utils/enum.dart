/*
 *
 *  Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import 'package:beagle/src/utils/string_utils.dart';

class EnumUtils {
  static T fromString<T>(List<T> values, String str) {
    return str == null ? null : values.firstWhere(
        (item) => getEnumValueName(item).toUpperCase() == str.toUpperCase(),
        orElse: () => null
    );
  }

  static String getEnumValueName<T>(T enumValue) {
    return enumValue.toString().split('.')[1];
  }

  static String getEnumValueNameInKebabCase<T>(T enumValue) {
    return getEnumValueName(enumValue).toKebabCase();
  }
}
