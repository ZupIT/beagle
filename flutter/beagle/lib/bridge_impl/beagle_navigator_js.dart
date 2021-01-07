import 'dart:convert';

import 'package:beagle/beagle.dart';
import 'package:beagle/bridge_impl/beagle_js_engine.dart';
import 'package:beagle/interface/beagle_navigator.dart';
import 'package:beagle/interface/types.dart';
import 'package:beagle/model/route.dart';

class BeagleNavigatorJS extends BeagleNavigator {
  BeagleNavigatorJS(this._viewId);

  final String _viewId;

  static String routeToJson(Route route) {
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

  static Route mapToRoute(Map<String, dynamic> routeMap) {
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
  T getCurrentRoute<T extends Route>() {
    final result = BeagleJSEngine.js
        .evaluate(
            "global.beagle.getViewById('$_viewId').getNavigator().getCurrentRoute()")
        .rawResult;

    if (result == null) {
      return null;
    }

    return mapToRoute(result);
  }

  @override
  bool isEmpty() {
    return BeagleJSEngine.js
        .evaluate(
            "global.beagle.getViewById('$_viewId').getNavigator().isEmpty()")
        .rawResult;
  }

  @override
  Future<void> popStack() {
    final result = BeagleJSEngine.js.evaluate(
        "global.beagle.getViewById('$_viewId').getNavigator().popStack()");
    return BeagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> popToView(String routeIdentifier) {
    final result = BeagleJSEngine.js.evaluate(
        "global.beagle.getViewById('$_viewId').getNavigator().popToView('$routeIdentifier')");
    return BeagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> popView() {
    final result = BeagleJSEngine.js.evaluate(
        "global.beagle.getViewById('$_viewId').getNavigator().popView()");
    return BeagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> pushStack(Route route, [String controllerId]) {
    final routeJson = routeToJson(route);
    final args =
        controllerId == null ? routeJson : "$routeJson, '$controllerId'";
    final result = BeagleJSEngine.js.evaluate(
        "global.beagle.getViewById('$_viewId').getNavigator().pushStack($args)");
    return BeagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> pushView(Route route) {
    final routeJson = routeToJson(route);
    final result = BeagleJSEngine.js.evaluate(
        "global.beagle.getViewById('$_viewId').getNavigator().pushView($routeJson)");
    return BeagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> resetApplication(Route route, [String controllerId]) {
    final routeJson = routeToJson(route);
    final args =
        controllerId == null ? routeJson : "$routeJson, '$controllerId'";
    final result = BeagleJSEngine.js.evaluate(
        "global.beagle.getViewById('$_viewId').getNavigator().resetApplication($args)");
    return BeagleJSEngine.promiseToFuture(result);
  }

  @override
  Future<void> resetStack(Route route, [String controllerId]) {
    final routeJson = routeToJson(route);
    final args =
        controllerId == null ? routeJson : "$routeJson, '$controllerId'";
    final result = BeagleJSEngine.js.evaluate(
        "global.beagle.getViewById('$_viewId').getNavigator().resetStack($args)");
    return BeagleJSEngine.promiseToFuture(result);
  }

  @override
  RemoveListener subscribe(NavigationListener listener) {
    return BeagleJSEngine.onNavigate(_viewId, listener);
  }
}
