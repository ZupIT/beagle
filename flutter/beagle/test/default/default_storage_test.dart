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
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('Given a DefaultStorage', () {
    final defaultStorage = DefaultStorage();
    group('When setItem is called', () {
      test('Then it should store the value for the key', () async {
        const key = 'key1';
        const value = 'value for key1';
        await defaultStorage.clear();

        await defaultStorage.setItem(key, value);

        final retrievedValue = await defaultStorage.getItem(key);
        expect(retrievedValue, value);
      });
    });
    group('When setItem is called by passing a key that already exists', () {
      test('Then it should override the value stored for the key', () async {
        const key = 'key1';
        const firstValue = 'first value for key1';
        const secondValue = 'second value for key1';
        await defaultStorage.clear();

        await defaultStorage.setItem(key, firstValue);
        await defaultStorage.setItem(key, secondValue);

        final retrievedValue = await defaultStorage.getItem(key);
        expect(retrievedValue, secondValue);
      });
    });

    group('When getItem is called by passing a key that exists', () {
      test('Then it should return the value stored for the key', () async {
        const key = 'key1';
        const value = 'value for key1';
        await defaultStorage.clear();

        await defaultStorage.setItem(key, value);

        final retrievedValue = await defaultStorage.getItem(key);

        expect(retrievedValue, value);
      });
    });

    group('When getItem is called by passing a key that does not exists', () {
      test('Then it should return null', () async {
        const key = 'key1';
        await defaultStorage.clear();

        final retrievedValue = await defaultStorage.getItem(key);

        expect(retrievedValue, null);
      });
    });

    group('When removeItem is called by passing an existent key', () {
      test(
          'Then it should remove the value assigned for the key from the storage',
          () async {
        const key = 'key1';
        const value = 'value for key1';
        await defaultStorage.clear();
        await defaultStorage.setItem(key, value);

        var retrievedValue = await defaultStorage.getItem(key);
        expect(retrievedValue, value);

        await defaultStorage.removeItem(key);
        retrievedValue = await defaultStorage.getItem(key);

        expect(retrievedValue, null);
      });
    });

    group('When removeItem is called by passing a nonexistent key', () {
      test('Then it should not throw any exception', () async {
        const key = 'key1';
        await defaultStorage.clear();

        await defaultStorage.removeItem(key);
      });
    });

    group('When clear is called', () {
      test('Then it should clear all values stored', () async {
        const key = 'key1';
        const value = 'value for key1';
        await defaultStorage.clear();
        await defaultStorage.setItem(key, value);

        await defaultStorage.clear();

        final retrievedValue = await defaultStorage.getItem(key);

        expect(retrievedValue, null);
      });
    });
  });
}
