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

import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle_components/beagle_lazy_component.dart';
import 'package:beagle_components/beagle_text_input.dart';
import 'package:beagle/interface/beagle_service.dart';
import 'package:flutter/material.dart';

final Map<String, ComponentBuilder> defaultComponents = {
  'custom:loading': (element, _, __) =>
      Text('Loading...', key: element.getKey()),
  'custom:error': (element, _, __) => Text('Error!', key: element.getKey()),
  'beagle:text': (element, _, __) =>
      Text(element.getAttributeValue('text'), key: element.getKey()),
  'beagle:container': (element, children, _) =>
      Container(key: element.getKey(), child: Column(children: children)),
  'beagle:textInput': (element, _, __) => BeagleTextInput(
        key: element.getKey(),
        onChange: element.getAttributeValue('onChange'),
        onFocus: element.getAttributeValue('onFocus'),
        onBlur: element.getAttributeValue('onBlur'),
        placeholder: element.getAttributeValue('placeholder'),
        value: element.getAttributeValue('value'),
      ),
  'beagle:button': (element, _, __) => FlatButton(
        key: element.getKey(),
        onPressed: element.getAttributeValue('onPress'),
        child: Text(
          element.getAttributeValue('text'),
        ),
      ),
  'beagle:lazycomponent': (element, children, view) {
    final initialState = element.getAttributeValue('initialState');
    return BeagleLazyComponent(
        key: element.getKey(),
        path: element.getAttributeValue('path'),
        initialState:
            initialState == null ? null : BeagleUIElement(initialState),
        beagleId: element.getId(),
        view: view,
        child: children.isEmpty ? null : children[0]);
  }
};
