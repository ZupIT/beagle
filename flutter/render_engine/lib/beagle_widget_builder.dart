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

import 'dart:convert';

import 'package:flutter/widgets.dart';
import 'package:render_engine/beagle_widget.dart';

// ignore: avoid_classes_with_only_static_members
class BeagleWidgetBuilder {
  static final _parsers = [];

  static final _widgetNameParserMap = <String, BeagleWidget>{};

  static bool _defaultParserInitialized = false;

  static void addBeagleWidget(BeagleWidget widget) {
    _parsers.add(widget);
    _widgetNameParserMap[widget.widgetName] = widget;
  }

  static void addBeagleWidgets(List<BeagleWidget> widget) {
    widget.forEach(addBeagleWidget);
  }

  static void registerWidgetsIfNeed() {
    if (!_defaultParserInitialized) {
      for (final parser in _parsers) {
        _widgetNameParserMap[parser.widgetName] = parser;
      }
      _defaultParserInitialized = true;
    }
  }

  static Widget build(String json, BuildContext buildContext) {
    registerWidgetsIfNeed();
    final map = jsonDecode(json);
    final widget = buildFromMap(map, buildContext);
    return widget;
  }

  static Widget buildFromMap(
      Map<String, dynamic> map, BuildContext buildContext) {
    final String widgetName = map['_beagleComponent_'];
    final parser = _widgetNameParserMap[widgetName];
    if (parser != null) {
      return parser.parse(map, buildContext);
    }
    return null;
  }

  static List<Widget> buildWidgets(
      List<dynamic> values, BuildContext buildContext) {
    final List<Widget> widgets = [];
    for (final value in values) {
      widgets.add(buildFromMap(value, buildContext));
    }
    return widgets;
  }
}
