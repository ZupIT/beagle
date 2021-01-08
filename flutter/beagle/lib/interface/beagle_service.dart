/*
 *
 *  Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import 'package:beagle/beagle.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/interface/global_context.dart';
import 'package:beagle/interface/http_client.dart';
import 'package:beagle/interface/navigation_controller.dart';
import 'package:beagle/interface/storage.dart';
import 'package:beagle/model/beagle_action.dart';
import 'package:beagle/model/network_options.dart';
import 'package:beagle/model/network_strategy.dart';
import 'package:flutter/widgets.dart';

typedef ComponentBuilder = Widget Function(
    BeagleUIElement element, List<Widget> children);
typedef ActionHandler = void Function(
    {BeagleAction action, BeagleView view, BeagleUIElement element});

abstract class BeagleService {
  /// URL to the backend providing the views (JSON) for Beagle.
  String baseUrl;

  /// Custom client to make HTTP requests. You can use this to implement your own HTTP client,
  /// calculating your own headers, cookies, response transformation, etc. The client provided
  /// here must implement the HttpClient interface. By default, the DefaultHttpClient will be
  /// used.
  HttpClient httpClient;

  /// The map of components to be used when rendering a view. The key must be the
  /// `_beagleComponent_` identifier and the value must be a ComponentBuilder, which is a function
  /// that transforms a BeagleUIElement into a Widget. The key must always start with `beagle:` or
  /// `custom:`.
  Map<String, ComponentBuilder> components;

  /// TODO: The custom storage. By default, uses the browser's `localStorage`.
  Storage storage;

  /// Wether or not to send specific beagle headers in the requests to fetch a view. Default is
  /// true.
  bool useBeagleHeaders;

  /// The map of custom actions. The key must be the `_beagleAction_` identifier and the value
  /// must be the action handler. The key must always start with `beagle:` or `custom:`.
  Map<String, ActionHandler> actions;

  /// The default cache strategy for fetching views from the backend. By default uses
  /// `beagle-with-fallback-to-cache`.
  NetworkStrategy strategy;

  /// Options for the visual feedback when navigating from a view to another. To set the default
  /// options, use `default: true` in the navigation controller.
  Map<String, NavigationController> navigationControllers;

  /// Access to the Global Context API. Use it to set persistent values that can be retrieved and
  /// manipulated by the view rendered by Beagle.
  GlobalContext globalContext;

  // todo:
  /*Analytics analytics;
  LifecycleHandler lifecycles,
  AnalyticsProvider analyticsProvider,
  Map<String, Operation> customOperations,*/

  // todo: add support for the ViewContentManager

  /// Starts the Beagle Service.
  Future<void> start();

  /// Creates a new Beagle View. There are two optional parameters: the [networkOptions] and the
  /// [initialControllerId]. The first one sets network options for every view requested by this
  /// Beagle View (headers, http method and cache strategy). If nothing is specied, the default
  /// network options are used (beagle headers, get and beagle-with-fallback-to-cache). The
  /// [initialControllerId] is the id of the navigation controller for the first navigation stack.
  /// If not specified, the default navigation controller is used.
  BeagleView createView(
      {NetworkOptions networkOptions, String initialControllerId});
}
