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

import 'dart:developer' as developer;
import 'package:logging/logging.dart';

import 'package:beagle/logger/beagle_logger.dart';

class AppLogger implements BeagleLogger {
  static const BEAGLE_TAG = 'BeagleSDK';

  @override
  void error(String message) {
    developer.log(message, name: BEAGLE_TAG, level: Level.SEVERE.value);
  }

  @override
  void errorWithException(
    String message,
    Exception exception,
  ) {
    developer.log(
      message,
      name: BEAGLE_TAG,
      level: Level.SEVERE.value,
    );
  }

  @override
  void info(String message) {
    developer.log(message, level: Level.INFO.value);
  }

  @override
  void warning(String message) {
    developer.log(message, level: Level.WARNING.value);
  }
}
