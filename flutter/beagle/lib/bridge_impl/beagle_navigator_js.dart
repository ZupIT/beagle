import 'dart:convert';

import 'package:beagle/beagle.dart';
import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/interface/beagle_navigator.dart';
import 'package:beagle/model/route.dart';
import 'package:flutter/widgets.dart' as widget;

class BeagleNavigatorJS extends BeagleNavigator {
  BeagleNavigatorJS(this.viewId);

  final String viewId;

  String _routeToJson(Route route) {
    var map = <String, dynamic>{};

    if (route is LocalView) {
      map = {'screen': route.screen.properties};
    }

    if (route is RemoteView) {
      map = {
        'url': route.url,
        'fallback': route.fallback?.properties,
        'shouldPrefetch': route.shouldPrefetch
      };
    }

    return jsonEncode(map);
  }

  @override
  Route getCurrentRoute() {
    final result = BeagleJSEngine.js
        .evaluate(
            "global.beagle.getViewById('$viewId').getNavigator().getCurrentRoute()")
        .rawResult;
    if (result == null) {
      return null;
    }
    // ignore: avoid_as
    final routeMap = result as Map<String, dynamic>;

    if (routeMap.containsKey('url')) {
      final fallback = routeMap.containsKey('fallback')
          ? BeagleUIElement(routeMap['fallback'])
          : null;
      final shouldPrefetch = routeMap.containsKey('shouldPrefetch')
          ? routeMap['shouldPrefetch']
          : false;
      return RemoteView(routeMap['url'],
          fallback: fallback, shouldPrefetch: shouldPrefetch);
    }

    if (routeMap.containsKey('screen')) {
      return LocalView(BeagleUIElement(routeMap['screen']));
    }

    return null;
  }

  @override
  bool isEmpty() {
    return BeagleJSEngine.js
        .evaluate(
            "global.beagle.getViewById('$viewId').getNavigator().isEmpty()")
        .rawResult;
  }

  @override
  Future<void> popStack() {
    // TODO: implement popStack
    throw UnimplementedError();
  }

  @override
  Future<void> popToView(String routeIdentifier) {
    // TODO: implement popToView
    throw UnimplementedError();
  }

  @override
  Future<void> popView() {
    // TODO: implement popView
    throw UnimplementedError();
  }

  @override
  Future<void> pushStack(Route route, [String controllerId]) {
    // TODO: implement pushStack
    throw UnimplementedError();
  }

  @override
  Future<void> pushView(Route route) async {
    final routeJson = _routeToJson(route);
    final result = BeagleJSEngine.js.evaluate(
        "global.beagle.getViewById('$viewId').getNavigator().pushView($routeJson)");
    await BeagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> resetApplication(Route route, [String controllerId]) {
    // TODO: implement resetApplication
    throw UnimplementedError();
  }

  @override
  Future<void> resetStack(Route route, [String controllerId]) {
    // TODO: implement resetStack
    throw UnimplementedError();
  }

  @override
  void subscribe(listener) {
    // TODO: implement subscribe
  }
}
