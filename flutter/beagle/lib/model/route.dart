import 'package:beagle/model/beagle_ui_element.dart';

abstract class Route {}

class RemoteView extends Route {
  RemoteView(this.url, {this.fallback, this.shouldPrefetch});

  final String url;
  final BeagleUIElement fallback;
  final bool shouldPrefetch;
}

class LocalView extends Route {
  LocalView(this.screen);

  final BeagleUIElement screen;
}
