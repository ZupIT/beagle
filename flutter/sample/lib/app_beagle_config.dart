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
import 'package:flutter/foundation.dart';

class AppBeagleConfig implements BeagleConfig {
  static const BASE_URL =
      'https://gist.githubusercontent.com/paulomeurerzup/80e54caf96ba56ae96d07b4e671cae42/raw/ef803a5a3f32aff8c2b7b2d65e0633d485c658a2';

  @override
  String get baseUrl => BASE_URL;

  @override
  BeagleEnvironment get environment =>
      kDebugMode ? BeagleEnvironment.debug : BeagleEnvironment.production;
}
