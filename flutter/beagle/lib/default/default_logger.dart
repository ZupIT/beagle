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

import 'package:beagle/logger/beagle_logger.dart';

class DefaultLogger implements BeagleLogger {
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

/// [Level]s to control logging output.
class Level {
  const Level(this.name, this.value);

  final String name;
  final int value;

  /// Key for informational messages ([value] = 800).
  static const Level INFO = Level('INFO', 800);

  /// Key for potential problems ([value] = 900).
  static const Level WARNING = Level('WARNING', 900);

  /// Key for serious failures ([value] = 1000).
  static const Level SEVERE = Level('SEVERE', 1000);
}
