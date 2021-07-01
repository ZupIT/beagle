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

import 'package:beagle/model/beagle_style.dart';
import 'package:beagle/style/style_mapper.dart';
import 'package:flutter/widgets.dart';
import 'package:yoga_engine/yoga_engine.dart';

mixin YogaWidget {}

YogaLayout buildYogaLayout({
  Key key,
  BeagleStyle style,
  List<Widget> children,
}) {
  final nodeProperties = mapToNodeProperties(style);
  final mappedChildren = children
      .map(
        (child) => child is YogaWidget
            ? child
            : YogaNode(
                nodeProperties: mapToNodeProperties(BeagleStyle()),
                child: child,
              ),
      )
      .toList();
  return YogaLayout(
    key: key,
    nodeProperties: nodeProperties,
    children: mappedChildren,
  );
}

YogaNode buildYogaNode({
  Key key,
  BeagleStyle style,
  Widget child,
}) {
  final nodeProperties = mapToNodeProperties(style);
  return YogaNode(
    nodeProperties: nodeProperties,
    child: child,
  );
}
