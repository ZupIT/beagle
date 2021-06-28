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

import 'package:beagle/beagle.dart';
import 'package:beagle/interface/beagle_service.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle/utils/enum.dart';
import 'package:beagle_components/beagle_button.dart';
import 'package:beagle_components/beagle_image.dart';
import 'package:beagle_components/beagle_lazy_component.dart';
import 'package:beagle_components/beagle_page_indicator.dart';
import 'package:beagle_components/beagle_page_view.dart';
import 'package:beagle_components/beagle_tab_bar.dart';
import 'package:beagle_components/beagle_text.dart';
import 'package:beagle_components/beagle_text_input.dart';
import 'package:beagle_components/beagle_touchable.dart';
import 'package:beagle_components/beagle_webview.dart';
import 'package:beagle_components/beagle_container.dart';
import 'package:beagle_components/beagle_scroll_view.dart';
import 'package:flutter/material.dart';

final Map<String, ComponentBuilder> defaultComponents = {
  'custom:loading': beagleLoadingBuilder(),
  'custom:error': beagleErrorBuilder(),
  'beagle:text': beagleTextBuilder(),
  'beagle:container': beagleContainerBuilder(),
  'beagle:textInput': beagleTextInputBuilder(),
  'beagle:button': beagleButtonBuilder(),
  'beagle:lazycomponent': beagleLazyComponentBuilder(),
  'beagle:tabbar': beagleTabBarBuilder(),
  'beagle:pageview': beaglePageViewBuilder(),
  'beagle:image': beagleImageBuilder(),
  'beagle:pageIndicator': beaglePageIndicatorBuilder(),
  'beagle:touchable': beagleTouchableBuilder(),
  'beagle:webView': beagleWebViewBuilder(),
  'beagle:screenComponent': beagleContainerBuilder(),
  'beagle:scrollView': beagleScrollViewBuilder(),
};

ComponentBuilder beagleLoadingBuilder() {
  return (element, _, __) => Text(
        'Loading...',
        key: element.getKey(),
      );
}

ComponentBuilder beagleErrorBuilder() {
  return (element, _, __) => Text(
        'Error!',
        key: element.getKey(),
      );
}

ComponentBuilder beagleTextBuilder() {
  return (element, _, __) => BeagleText(
        key: element.getKey(),
        text: element.getAttributeValue('text'),
        textColor: element.getAttributeValue('textColor'),
        styleId: element.getAttributeValue('styleId'),
        alignment: EnumUtils.fromString(
          TextAlignment.values,
          element.getAttributeValue('alignment') ?? '',
        ),
      );
}

ComponentBuilder beagleContainerBuilder() {
  return (element, children, _) => BeagleContainer(
    key: element.getKey(),
    onInit: element.getAttributeValue('onInit'),
    children: children,
  );
}

ComponentBuilder beagleScrollViewBuilder() {
  return (element, children, _) => BeagleScrollView(
    key: element.getKey(),
    scrollDirection: EnumUtils.fromString(
      ScrollAxis.values,
      element.getAttributeValue('scrollDirection'),
    ),
    scrollBarEnabled: element.getAttributeValue('scrollBarEnabled'),
    children: children,
  );
}

ComponentBuilder beagleTextInputBuilder() {
  return (element, _, __) => BeagleTextInput(
        key: element.getKey(),
        onChange: element.getAttributeValue('onChange'),
        onFocus: element.getAttributeValue('onFocus'),
        onBlur: element.getAttributeValue('onBlur'),
        placeholder: element.getAttributeValue('placeholder'),
        value: element.getAttributeValue('value'),
      );
}

ComponentBuilder beagleButtonBuilder() {
  return (element, _, __) => BeagleButton(
        key: element.getKey(),
        onPress: element.getAttributeValue('onPress'),
        text: element.getAttributeValue('text'),
        enabled: element.getAttributeValue('enabled'),
        styleId: element.getAttributeValue('styleId'),
      );
}

ComponentBuilder beagleLazyComponentBuilder() {
  return (element, children, view) {
    final initialState = element.getAttributeValue('initialState');
    return BeagleLazyComponent(
        key: element.getKey(),
        path: element.getAttributeValue('path'),
        initialState:
            initialState == null ? null : BeagleUIElement(initialState),
        beagleId: element.getId(),
        view: view,
        child: children.isEmpty ? null : children[0]);
  };
}

ComponentBuilder beagleTabBarBuilder() {
  return (element, _, __) => BeagleTabBar(
        key: element.getKey(),
        items:
            element.getAttributeValue('items').map<TabBarItem>((dynamic item) {
          return TabBarItem.fromJson(item);
        }).toList(),
        currentTab: element.getAttributeValue('currentTab'),
        onTabSelection: element.getAttributeValue('onTabSelection'),
      );
}

ComponentBuilder beaglePageViewBuilder() {
  return (element, children, __) => BeaglePageView(
        key: element.getKey(),
        currentPage: element.getAttributeValue('currentPage'),
        onPageChange: element.getAttributeValue('onPageChange'),
        children: children,
      );
}

ComponentBuilder beagleImageBuilder() {
  return (element, _, __) => BeagleImage(
        key: element.getKey(),
        path: ImagePath.fromJson(element.getAttributeValue('path')),
        mode: EnumUtils.fromString(
          ImageContentMode.values,
          element.getAttributeValue('mode') ?? '',
        ),
      );
}

ComponentBuilder beaglePageIndicatorBuilder() {
  return (element, _, __) => BeaglePageIndicator(
        key: element.getKey(),
        selectedColor: element.getAttributeValue('selectedColor'),
        unselectedColor: element.getAttributeValue('unselectedColor'),
        numberOfPages: element.getAttributeValue('numberOfPages'),
        currentPage: element.getAttributeValue('currentPage'),
      );
}

ComponentBuilder beagleTouchableBuilder() {
  return (element, children, __) => BeagleTouchable(
        key: element.getKey(),
        onPress: element.getAttributeValue('onPress'),
        child: children[0],
      );
}

ComponentBuilder beagleWebViewBuilder() {
  return (element, children, __) => BeagleWebView(
        key: element.getKey(),
        url: element.getAttributeValue('url'),
      );
}
