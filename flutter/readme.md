# Beagle Flutter
This is the library responsible for rendering a Beagle JSON (payload) in Flutter. It is currently in alpha stage, and
most features are yet to be implemented. We welcome any help from the community in making improvements to the API -
which is not yet final - and implementing functionalities that are missing. By the end of this document you can find the
session "State of development" where we show everything we have yet to do before releasing a stable version.

## Versioning
Every alpha and beta version of Beagle Flutter will follow the pattern `0.x.y`, where `x` is the version of Beagle Web
it's based on and `y` is every subsequent version where `x` would be the same.

In the version number, `x` refers to a version of the Beagle Web, because Beagle Flutter uses this lib under the hood.

## Compatibility
For now, we're focusing in the most recent version of Flutter (2.2), but before releasing, we'll test it with older
versions to guarantee it also works.

## Installation
1. Open the file `pubspec.yaml` in the root of your project.
2. Under `dependencies`, add `beagle: ^0.0.7`, or whatever the most recent version is.
3. Also add `beagle_components: ^0.0.7`. You can omit this dependency if you're familiar with Beagle and won't use any
of the default components.
4. From your IDE, click `pub get`. Or, from the terminal, type `flutter pub get`.

## Usage
We currently don't have a script to automatically setup your environment, but don't be discouraged, it's quite simple.

### 1. The main configuration file
1. Under `lib`, create a new directory called `beagle`.
2. Under `lib/beagle`, create a new file called `config.dart`.
3. Write the following code to `config.dart`:

```dart
import 'package:beagle/beagle.dart';
import 'package:flutter/foundation.dart';
import 'package:beagle_components/beagle_components.dart';

class AppBeagleConfig implements BeagleConfig {
  static const BASE_URL = 'https://usebeagle.io/start';

  @override
  String get baseUrl => BASE_URL;

  @override
  BeagleEnvironment get environment =>
      kDebugMode ? BeagleEnvironment.debug : BeagleEnvironment.production;
}

void startBeagle() {
  BeagleSdk.init(
    beagleConfig: AppBeagleConfig(),
    components: defaultComponents,
  );
}
```

This file tells everything Beagle needs to know to render your views. Here we show only the basic options, for a list
of available options please check the [documentation for the BeagleConfig](todo).

### 2. Starting Beagle
You can start Beagle wherever it feels more appropriate for your use. For this guide, we're going to start Beagle as
soon as the app starts, for this, open the file `lib/main.dart`, import `lib/beagle/config.dart` and, inside the main
function, before rendering  anything, call `startBeagle()`. See the example below:

```dart
import 'package:sample/app_beagle_config.dart';
// ...
void main() {
  startBeagle();
  // ...
}
```

Above, we imported `package:sample/app_beagle_config.dart` because `sample` is the name of our package. You'll need to
change this to the name of your own package. You can find this name in your `pubscpec.yaml` under the property `name`.

### 3. Rendering a remote view
To render a Beagle View, you must use the component `BeagleWidget` which is provided by the Beagle Library. This widget
requires a single parameter, the `screenRequest`, which specifies the request to fetch the first server driven view of
the flow. See the example below:

```dart
import 'package:beagle/beagle.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:sample/config.dart';

void main() {
  startBeagle();
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

Above, we used a simple Material App to render our first server driven view! You can use whatever you need, the
important part here is the component `BeagleWidget`, this is what includes a remote view in the layout.

The `BeagleWidget` accepts other optional parameters, and so does the `BeagleScreenRequest`. To check them, please visit
the [BeagleWidget documentation page](todo).

### 4. Making it your own

This example showed how to render a simple example available in the web. For real use case scenarios, you'll have your
own backend server providing the views - check the
[Beagle Backend Framework](https://docs.usebeagle.io/v1.8/get-started/installing-beagle/backend/) for more details. To
make Beagle work with this backend, you only need to change the address in the field `baseUrl` of your configuration at
`lib/beagle/config.dart`.

If you're testing it in the Android emulator and using a backend served in the localhost, don't forget to use the ip
10.0.2.2. To build a `baseUrl` that works for both the Android and iOS emulator, you can use `Platform` from `dart:io`:

```dart
import 'dart:io' show Platform;
// ...

class AppBeagleConfig implements BeagleConfig {
  static final localhost = Platform.isAndroid ? '10.0.2.2' : 'localhost';

  @override
  String get baseUrl => 'http://$localhost:8080';

  // ...
}
```

## Configuration options
Beagle lets you configure a lot of its behavior. Please, check [this article](todo) for a list of every option
available.

## The Beagle Widget
The entrypoint for a Beagle remote view is the `BeagleWidget`, as said before, it only requires a single parameter, the
url of the view to render, but much more can be customized, e.g. observe errors and updates, make complex requests, etc.
To check a list of every option accepted by the `BeagleWidget`, please, check [this article](todo).

## Beagle services
These are services that must be provided to Beagle for it work properly. For now, we're embedding default
implementations within the library. This is probably not be the case in the future, since it may represent potential
security issues. The Beagle services are:

- [BeagleLogger](todo): provide a logger for Beagle to use.
- [HttpClient](todo): tell exactly how the network requests should be made, add your own headers and much more.
- [BeagleImageDownloader](todo): use your own logic for downloading network images.
- [BeagleStorage](todo): persist data across multiple executions by stating exactly how Beagle should store data.

You can set your own implementation of each of these services via `BeagleSdk.init`, in your configuration file.

## Customization
Beagle is highly customizable, you can create your own components, actions and even operations. All of these must be
provided when calling `BeagleSdk.init`.

- [Custom components](todo): create your own components.
- [Custom actions](todo): make the events in your components do exactly what you need.
- [Custom operations](todo): if the operations shipped with Beagle are not enough for your expressions, create your own.

## Other APIs
- [Global context](todo): learn how to manipulate the global context in Beagle Flutter.
- [Analytics](todo): observe every action executed and navigation performed.
- [Renderer](todo): sometimes it might be necessary to interact with Beagle while rendering a component. This article
  shows how to use the Renderer API to achieve complex behaviors.
  
## Current state of development
We're currently in alpha. We'll move to beta as soon as we have every layout tool (Yoga) working as expected. Here's
a list of every feature we need to release a stable version of Beagle Flutter and its status.

It's important to reiterate that Beagle is an open source project and every help is welcomed!

### Major features

todo: check https://docs.google.com/spreadsheets/d/1aSo7eZsj2lEnTG0kKzta-U4G9qkgA8v1w9kpyrPlVPA/edit?usp=sharing

### Default components

todo: check https://docs.google.com/spreadsheets/d/1aSo7eZsj2lEnTG0kKzta-U4G9qkgA8v1w9kpyrPlVPA/edit?usp=sharing

### Default actions

todo: check https://docs.google.com/spreadsheets/d/1aSo7eZsj2lEnTG0kKzta-U4G9qkgA8v1w9kpyrPlVPA/edit?usp=sharing
