# Beagle Components
This library provides the Beagle's base components. It is currently in alpha stage, and some components are yet to be 
implemented. We welcome any help from the community in making improvements to the components with features that are 
missing. At the end of this document you can find the session "State of development" where we show everything we have 
yet to do before releasing a stable version.

## Versioning
Every alpha and beta version of Beagle Flutter will follow the pattern `0.x.y`, where `x` is the version of [Beagle 
Web](https://github.com/ZupIT/beagle-web-core) it's based on and `y` is every subsequent version where `x` 
would be the same.

In the version number, `x` refers to a version of the Beagle Web, because Beagle Flutter uses this lib under the hood.

## Null safety compatibility
For now, we'll not support null safety for the alpha versions, however this is a high priority implementation and 
should be available in future versions.

## Installation
1. Open the file `pubspec.yaml` in the root of your project.
2. Under `dependencies`, add `beagle_components: ^0.9.0-alpha`, or whatever the most recent version is.
4. In your IDE (Android Studio or Visual Studio Code), click `pub get`. Or, from the terminal, type `flutter pub get`.

## Usage
It's quite simple to use the Beagle Components library. Follow the steps bellow to be able to use it.

### 1. The configuration
There is no configuration required to use the Beagle Components library, but it is expected to use it with the 
Beagle library. To learn how to configure Beagle, please check the 
[documentation for the Beagle Initialization](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/configuration/).

### 2. Using Beagle Components
To use this library with Beagle, open the file `lib/main.dart`, import `package:beagle/beagle.dart` and 
`package:beagle_components/beagle_components.dart` as well. After that, inside the main function, before rendering 
anything, call `BeagleSdk.init` passing the `baseUrl` param with the `components: defaultComponents`. 
See the example below:

```dart
import 'package:beagle/beagle.dart';
import 'package:beagle_components/beagle_components.dart';

void main() {
  BeagleSdk.init(
    baseUrl: 'http://yourBffBaseUrl.io',
    components: defaultComponents,
  );
  // runApp();
}
```

### 3. Rendering a remote widget
To render a Beagle Widget, you must use the component `BeagleWidget` which is provided by the Beagle Library. This widget
requires a single parameter, the `screenRequest`, which specifies the request to fetch the first server-driven view of
the flow. See the example below:

```dart
import 'package:beagle/beagle.dart';
import 'package:beagle_components/beagle_components.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

void main() {
  BeagleSdk.init(
    baseUrl: 'http://yourBffBaseUrl.io',
    components: defaultComponents,
  );
  runApp(const BeagleSampleApp());
}

class BeagleSampleApp extends StatelessWidget {
  const BeagleSampleApp({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Beagle Sample',
      home: Scaffold(
        body: BeagleWidget(
          screenRequest: BeagleScreenRequest('welcome'),
        ),
      ),
    );
  }
}
```

## Current state of development
Currently in alpha. It'll be moved to beta as soon as we have every layout tool (Yoga) working as expected. Here's
a list of every feature we need to release a stable version of Beagle Flutter and its status.

It's important to reiterate that Beagle is an open source project and every help is welcomed!

### Default components
todo: check https://docs.google.com/spreadsheets/d/1aSo7eZsj2lEnTG0kKzta-U4G9qkgA8v1w9kpyrPlVPA/edit?usp=sharing
