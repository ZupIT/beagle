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

import 'package:flutter/widgets.dart';

extension BuildContextUtils on BuildContext {
  BuildContext findBuildContextForWidgetKey(String widgetKey) {
    if (_compareWidgetKey(this, widgetKey)) {
      return this;
    }

    BuildContext widgetContext;

    void visitor(Element element) {
      if (_compareWidgetKey(element, widgetKey)) {
        widgetContext = element;
      } else {
        element.visitChildElements(visitor);
      }
    }

    visitChildElements(visitor);

    return widgetContext;
  }

  bool _compareWidgetKey(BuildContext context, String widgetKey) {
    final ValueKey<String> key = context.widget.key;
    return key != null && key.value == widgetKey;
  }
}
