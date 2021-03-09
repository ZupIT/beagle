import 'package:beagle/logger/beagle_logger.dart';
import 'package:beagle/service_locator.dart';
import 'package:flutter/material.dart';

class BeagleOpenNativeRoute {
  factory BeagleOpenNativeRoute() {
    return _instance;
  }

  BeagleOpenNativeRoute._constructor();

  static final BeagleOpenNativeRoute _instance =
      BeagleOpenNativeRoute._constructor();

  void navigate(BuildContext buildContext, String routeName) {
    try {
      Navigator.pushNamed(buildContext, routeName);
    } catch (err) {
      beagleServiceLocator<BeagleLogger>()
          .error('Error: $err while trying to navigate to $routeName');
    }
  }
}
