/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import 'dart:async';
import 'dart:convert';

import 'package:beagle/beagle_initializer.dart';
import 'package:beagle/interface/beagle_view.dart';
import 'package:beagle/model/beagle_ui_element.dart';
import 'package:beagle/model/route.dart';
import 'package:flutter/widgets.dart';

class BeagleServerDrivenState {
  BeagleServerDrivenState._();

  factory BeagleServerDrivenState.Started() = BeagleServerDrivenStateStarted;

  factory BeagleServerDrivenState.Success() = BeagleServerDrivenStateSuccess;

  factory BeagleServerDrivenState.Finished() = BeagleServerDrivenStateFinished;

  //NOT IMPLEMENTED
  factory BeagleServerDrivenState.Canceled() = BeagleServerDrivenStateCanceled;

  factory BeagleServerDrivenState.Error(Exception exception) =
      BeagleServerDrivenStateError;
}

class BeagleServerDrivenStateSuccess extends BeagleServerDrivenState {
  BeagleServerDrivenStateSuccess() : super._();
}

class BeagleServerDrivenStateStarted extends BeagleServerDrivenState {
  BeagleServerDrivenStateStarted() : super._();
}

class BeagleServerDrivenStateFinished extends BeagleServerDrivenState {
  BeagleServerDrivenStateFinished() : super._();
}

class BeagleServerDrivenStateCanceled extends BeagleServerDrivenState {
  BeagleServerDrivenStateCanceled() : super._();
}

class BeagleServerDrivenStateError extends BeagleServerDrivenState {
  BeagleServerDrivenStateError(this.exception) : super._();

  final Exception exception;
}

class BeagleStreamBuilder extends StatefulWidget {
  const BeagleStreamBuilder({
    Key key,
    this.url,
    this.builder,
    this.json,
  }) : super(key: key);

  final String url;

  final String json;

  final Widget Function(BuildContext context, BeagleServerDrivenState state)
      builder;

  @override
  _BeagleStreamBuilder createState() => _BeagleStreamBuilder();
}

class _BeagleStreamBuilder extends State<BeagleStreamBuilder> {
  BeagleView _view;
  Widget widgetState;

  @override
  void initState() {
    super.initState();
    widgetState = widget.builder(context, BeagleServerDrivenState.Started());

    startBeagleView();
  }

  Future<void> startBeagleView() async {
    await BeagleInitializer.getService().start();

    _view = BeagleInitializer.getService().createView()
      ..subscribe((tree) {
        final widgetLoaded = buildViewFromTree(tree);
        setState(() {
          widgetState = widgetLoaded;
        });
        setNewWidget(
            widget.builder(context, BeagleServerDrivenState.Success()));
        setNewWidget(
            widget.builder(context, BeagleServerDrivenState.Finished()));
      })
      ..addErrorListener((errors) {
        setNewWidget(widget.builder(
            context, BeagleServerDrivenState.Error(Exception(errors))));
        setNewWidget(
            widget.builder(context, BeagleServerDrivenState.Finished()));
      });

    if (widget.url != null) {
      await _view.getNavigator().pushView(RemoteView(widget.url));
    } else {
      await _view.getNavigator().pushView(LocalView(BeagleUIElement(jsonDecode(widget.json))));
    }
  }

  Widget buildViewFromTree(BeagleUIElement tree) {
    final widgetChildren = tree.getChildren().map(buildViewFromTree).toList();
    final builder = BeagleInitializer.getService().components[tree.getType()];
    if (builder == null) {
      BeagleInitializer.logger
          .error("Can't find builder for component ${tree.getType()}");
      //TODO SHOULD CREATE A COMPONENT INFORM NOT REGISTERED AND IF PRODUCTION NOT CREATE ANY COMPONENT
      return Container();
    }
    return builder(tree, widgetChildren, _view);
  }

  @override
  Widget build(BuildContext context) {
    return widgetState;
  }

  void setNewWidget(Widget widget) {
    if (widget != null) {
      setState(() {
        widgetState = widget;
      });
    }
  }
}
