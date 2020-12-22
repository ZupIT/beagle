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

import 'package:beagle_components/beagle_text_input.dart';
import 'package:beagle/interface/component_builder.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:flutter/material.dart';

typedef ComponentBuildFunction = Widget Function(
    BeagleUIElement element, List<Widget> children);

class DefaultComponentBuilder implements ComponentBuilder {
  final Map<String, ComponentBuildFunction> _builders = {
    'custom:loading': (BeagleUIElement element, List<Widget> children) =>
        Text('Loading...', key: element.getKey()),
    'custom:error': (BeagleUIElement element, List<Widget> children) =>
        Text('Error!', key: element.getKey()),
    'beagle:text': (BeagleUIElement element, List<Widget> children) =>
        Text(element.getAttributeValue('text'), key: element.getKey()),
    'beagle:container': (BeagleUIElement element, List<Widget> children) =>
        Container(key: element.getKey(), child: Column(children: children)),
    'beagle:textInput': (BeagleUIElement element, List<Widget> children) =>
        BeagleTextInput(
          key: element.getKey(),
          onChange: element.getAttributeValue('onChange'),
          onFocus: element.getAttributeValue('onFocus'),
          onBlur: element.getAttributeValue('onBlur'),
          placeholder: element.getAttributeValue('placeholder'),
          value: element.getAttributeValue('value'),
        ),
    'beagle:button': (BeagleUIElement element, List<Widget> children) =>
        FlatButton(
          key: element.getKey(),
          onPressed: element.getAttributeValue('onPress'),
          child: Text(
            element.getAttributeValue('text'),
          ),
        ),
  };

  @override
  List<String> getComponentKeys() {
    return _builders.keys.toList();
  }

  @override
  Widget buildComponent(BeagleUIElement element, List<Widget> children) {
    if (!_builders.containsKey(element.getType())) {
      // ignore: only_throw_errors
      throw ErrorDescription(
          'No builder found for component of type ${element.getType()}');
    }

    return _builders[element.getType()](element, children);
  }
}
