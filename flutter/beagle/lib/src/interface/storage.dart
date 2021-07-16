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

abstract class Storage {
  /// Adds the tuple ([key], [value]) to this persistent storage. The Future (promise) returned
  /// resolves as soon as the operation completes.
  Future<void> setItem(String key, String value);

  /// Retrieves the value identified by the [key] passed as parameter. If the key doesn't exist,
  /// null is returned. The return value is wrapped in a Future (promise).
  Future<String> getItem(String key);

  /// Removes a [key] from the storage. The Future (promise) returned resolves as soon as the
  /// operation completes.
  Future<void> removeItem(String key);

  /// removes everything from the storage. The Future (promise) returned resolves as soon as the
  /// operation completes.
  Future<void> clear();
}
