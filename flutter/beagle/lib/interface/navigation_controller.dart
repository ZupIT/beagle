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

class NavigationController {
  NavigationController({
    this.errorComponent,
    this.isDefault,
    this.loadingComponent,
    this.shouldShowError,
    this.shouldShowLoading,
  });

  /// If true, uses this as the default navigation controller.
  bool isDefault = false;

  /// Wether to show a loading component or not. True by default.
  bool shouldShowLoading = true;

  /// Wether to show an error component or not. True by default.
  bool shouldShowError = true;

  /// A custom loading component to use. The default value is "beagle:loading"
  String loadingComponent = 'beagle:loading';

  /// A custom error component to use. The default value is "beagle:error"
  String errorComponent = 'beagle:error';

  Map<String, dynamic> toMap() {
    return {
      'errorComponent': errorComponent,
      'isDefault': isDefault,
      'loadingComponent': loadingComponent,
      'shouldShowError': shouldShowError,
      'shouldShowLoading': shouldShowLoading,
    };
  }
}
