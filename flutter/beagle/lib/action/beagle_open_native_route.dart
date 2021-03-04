import 'package:beagle/logger/beagle_logger.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:beagle/service_locator.dart';

class BeagleOpenNativeRoute {
  factory BeagleOpenNativeRoute() {
    return _instance;
  }

  BeagleOpenNativeRoute._constructor();

  //Fix me - Using this context could lead to unexpected leaks, this will be swapped as soon as the context is available through the actions parameters
  BuildContext buildContext;

  static final BeagleOpenNativeRoute _instance =
      BeagleOpenNativeRoute._constructor();

  void navigate(String url) {
    try {
      Navigator.pushNamed(buildContext, url);
    } catch (err) {
      beagleServiceLocator<BeagleLogger>()
          .error('Error: $err while trying to navigate to $url');
    }
  }
}
