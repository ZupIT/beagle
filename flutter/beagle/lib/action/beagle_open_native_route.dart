import 'package:beagle/beagle.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class BeagleNativeNavigator {
  factory BeagleNativeNavigator() {
    return _instance;
  }

  BeagleNativeNavigator._constructor();

  //Fix me - Using this context could lead to unexpected leaks, this will be swapped as soon as the context is available through the actions parameters
  BuildContext buildContext;

  static final BeagleNativeNavigator _instance =
      BeagleNativeNavigator._constructor();

  void navigate(String url) {
    try {
      Navigator.pushNamed(buildContext, url);
    } catch (err) {
      BeagleSdk.logger.error('Error: $err while trying to navigate to $url');
    }
  }
}
