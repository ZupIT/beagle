import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class BeagleNativeNavigator {
  factory BeagleNativeNavigator() {
    return _instance;
  }

  BeagleNativeNavigator._constructor();

  BuildContext buildContext;

  static final BeagleNativeNavigator _instance =
      BeagleNativeNavigator._constructor();

  void navigate(String url) {
    try {
      Navigator.pushNamed(buildContext, url);
    } catch (err) {
      debugPrint('Error: $err while trying to navigate to $url');
    }
  }
}
