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

// code taken from the lib flutter_after_layout

import 'package:beagle_components/src/after_layout.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('MyWidget', (WidgetTester tester) async {
    var runCount = 0;
    await tester.pumpWidget(TestWidget(() => runCount++));
    expect(runCount, 1);
  });
}

class TestWidget extends StatefulWidget {
  // ignore: use_key_in_widget_constructors
  const TestWidget(this.callback);

  final VoidCallback callback;

  @override
  _TestWidgetState createState() => _TestWidgetState();
}

class _TestWidgetState extends State<TestWidget>
    with AfterLayoutMixin<TestWidget> {
  @override
  Widget build(BuildContext context) {
    return Container();
  }

  @override
  void afterFirstLayout(BuildContext context) {
    widget.callback();
  }
}
