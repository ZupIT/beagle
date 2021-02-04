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

import 'package:beagle/logger/beagle_logger.dart';
import 'package:beagle/logger/beagle_logger_proxy.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';

void main() {
  final logger = BeagleLoggerMock();
  const logMessage = 'log message';
  final logException = Exception('log exception');

  group('Given a BeagleLoggerProxy with a non null logger', () {
    final beagleLoggerProxy = BeagleLoggerProxy(logger: logger);

    group('When I call error method', () {
      test('Then it should call logger error method', () {
        beagleLoggerProxy.error(logMessage);
        verify(logger.error(logMessage)).called(1);
      });
    });

    group('When I call errorWithException method', () {
      test('Then it should call logger errorWithException method', () {
        beagleLoggerProxy.errorWithException(logMessage, logException);
        verify(logger.errorWithException(logMessage, logException)).called(1);
      });
    });

    group('When I call info method', () {
      test('Then it should call logger info method', () {
        beagleLoggerProxy.info(logMessage);
        verify(logger.info(logMessage)).called(1);
      });
    });

    group('When I call warning method', () {
      test('Then it should call logger warning method', () {
        beagleLoggerProxy.warning(logMessage);
        verify(logger.warning(logMessage)).called(1);
      });
    });
  });

  group('Given a BeagleLoggerProxy with a null logger', () {
    final beagleLoggerProxy = BeagleLoggerProxy();

    group('When I call error method', () {
      test('Then it should not throw any exception', () {
        beagleLoggerProxy.error(logMessage);
      });
    });

    group('When I call errorWithException method', () {
      test('Then it should not throw any exception', () {
        beagleLoggerProxy.errorWithException(logMessage, logException);
      });
    });

    group('When I call info method', () {
      test('Then it should not throw any exception', () {
        beagleLoggerProxy.info(logMessage);
      });
    });

    group('When I call warning method', () {
      test('Then it should not throw any exception', () {
        beagleLoggerProxy.warning(logMessage);
      });
    });
  });
}

class BeagleLoggerMock extends Mock implements BeagleLogger {}
