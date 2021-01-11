import 'package:beagle_components/after_layout.dart';
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
