# Beagle Flutter
This is the library responsible for rendering a Beagle JSON in Flutter. It is currently in alpha stage, and
most features are yet to be implemented. We welcome any help from the community in making improvements to the API -
which isn't finished - and implementing features that are missing. At the end of this document you can find the
session "State of development" where we show everything we have yet to do before releasing a stable version.

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
2. Under `dependencies`, add `beagle: ^0.9.0`, or whatever the most recent version is.
3. Also add `beagle_components: ^0.9.0`. You can omit this dependency if you're familiar with Beagle and won't use any
of the default components.
4. In your IDE (Android Studio or Visual Studio Code), click `pub get`. Or, from the terminal, type `flutter pub get`.

## Usage
It's quite simple to configure and use the Beagle Flutter library. Follow the steps bellow to be able to use it.

### 1. The configuration
All the configuration necessary for Beagle to work is centered on the parameters of the `BeagleSdk.init` startup 
method. This params tells everything Beagle needs to know to render your widgets. Here we show only the basic options 
`baseUrl` and `components`. For a list of all the available options, please check the 
[documentation for the Beagle Initialization](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/configuration/).

### 2. Starting Beagle
You can start Beagle at any point of the application. For this guide, we're going to start Beagle as soon as the app 
starts. For this, open the file `lib/main.dart`, import `package:beagle/beagle.dart` and, if you're using the 
`beagle_components` package, import `package:beagle_components/beagle_components.dart` as well. After that, 
inside the main function, before rendering anything, call `BeagleSdk.init` passing the parameter previously informed. 
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

Above, we used a simple Material App to render our first server-driven widget! The important part here is the component 
`BeagleWidget`, because it includes a remote view in the layout.

### 4. Implementing your own Beagle application

This example showed how to render a simple example available in the web. For real use case scenarios, you'll have your
own backend server providing the views - check the
[Beagle Backend Framework](https://docs.usebeagle.io/v1.9/get-started/installing-beagle/backend/) for more details. To
make Beagle work with this backend, you only need to change the address in the field `baseUrl` of your configuration at
`BeagleSdk.init` method.

If you're testing it in the Android emulator and using a local backend server, don't forget to use the ip 10.0.2.2. 
To build a `baseUrl` that works for both the Android and iOS emulator, you can use `Platform` from `dart:io`:

```dart
import 'dart:io' show Platform;

final localhost = Platform.isAndroid ? '10.0.2.2' : 'localhost';
final baseUrl = 'http://$localhost:8080';
```

## Configuration options
Beagle lets you configure a lot of its behavior. Please, check 
[this article](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/customization/overview/) 
for a list of every option available.

## The Beagle Widget
The entry point for a Beagle remote view is the `BeagleWidget`. As mentioned before, it only requires a single 
parameter, the url of the view to render, but much more can be customized, e.g. observe requests and parsing errors 
rendering updates, make parametrized requests, etc. 
[Check this doc](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/beagle-widget) for a list of 
every option accepted by the `BeagleWidget`.

## Beagle services
These are services that must be provided to Beagle for it to work properly. For now, we're embedding default
implementations within the library. This is probably not going to be the case in the future, since it may represent
potential security issues. The Beagle services are:

- [BeagleLogger](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/services/logger/): 
provides a logger for Beagle to use.
- [HttpClient](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/services/http-client/): 
tells exactly how the network requests should be made, allowing custom headers and much more.
- [BeagleImageDownloader](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/services/image-downloader/): 
allows a custom logic for downloading network images.
- [BeagleStorage](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/services/storage/): 
persists data across multiple executions by stating exactly how Beagle should store data.

You can set your own implementation of each of these services via `BeagleSdk.init`.

## Customization
Beagle is highly customizable because you can create your own components, actions and even operations. All of these 
must be provided when calling `BeagleSdk.init`.

- [Custom components](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/customization/components/): 
create your own components.
- [Custom actions](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/customization/actions/): 
make the events in your components do exactly what you need.
- [Custom operations](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/customization/operations/): 
if the operations shipped with Beagle are not enough for your expressions, create your own.
- [Design System](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-flutter/design-system/): 
here you define all styles and local images that can be used by Beagle.

## Other APIs
- [Global context](https://docs.usebeagle.io/v1.9/api/context/global-context/): allows manipulation of the global 
context in Beagle Flutter.
- [Analytics](https://docs.usebeagle.io/v1.9/api/analytics/): gives information of every action executed, such as 
navigation data.
- [Renderer](https://docs.usebeagle.io/v1.9/resources/customization/beagle-for-web/advanced-topics/renderer-api/): sometimes it might be necessary to interact with Beagle while rendering a component or executing an 
action. This article shows how to use the Renderer API to achieve complex behaviors.
  
## Current state of development
Currently in alpha. It'll be moved to beta as soon as we have every layout tool (Yoga) working as expected. Here's
a list of every feature we need to release a stable version of Beagle Flutter and its status.

It's important to reiterate that Beagle is an open source project and every help is welcomed!

### Major features

todo: check https://docs.google.com/spreadsheets/d/1aSo7eZsj2lEnTG0kKzta-U4G9qkgA8v1w9kpyrPlVPA/edit?usp=sharing

### Default components

todo: check https://docs.google.com/spreadsheets/d/1aSo7eZsj2lEnTG0kKzta-U4G9qkgA8v1w9kpyrPlVPA/edit?usp=sharing

### Default actions

todo: check https://docs.google.com/spreadsheets/d/1aSo7eZsj2lEnTG0kKzta-U4G9qkgA8v1w9kpyrPlVPA/edit?usp=sharing
