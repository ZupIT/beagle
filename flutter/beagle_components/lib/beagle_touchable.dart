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

import 'package:flutter/material.dart';

/// Defines a [GestureDetector] that executes an action when [child] is pressed.
class BeagleTouchable extends StatelessWidget {
  const BeagleTouchable({
    Key key,
    this.onPress,
    this.child,
  }) : super(key: key);

  /// Action that will be performed when [child] is pressed.
  final Function onPress;

  /// A [Widget] that will be rendered inside [BeagleTouchable] and listen to
  /// press events.
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onPress,
      child: child,
    );
  }
}
