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

import 'dart:io' show Platform;

import 'package:beagle/beagle.dart';
import 'package:beagle/interface/beagle_service.dart';
import 'package:beagle/interface/navigation_controller.dart';
import 'package:beagle_components/beagle_components.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:sample/app_design_system.dart';
import 'package:sample/beagle_sample_screen.dart';

Map<String, ComponentBuilder> myCustomComponents = {
  'custom:loading': (element, _, __) {
    return Center(
      key: element.getKey(),
      child: const Text('My custom loading.'),
    );
  }
};
Map<String, ActionHandler> myCustomActions = {
  'custom:log': ({action, view, element, context}) {
    debugPrint(action.getAttributeValue('message'));
  }
};

void main() {
  final localhost = Platform.isAndroid ? '10.0.2.2' : 'localhost';

  BeagleSdk.init(
    baseUrl: 'http://$localhost:8080',
    environment: kDebugMode ? BeagleEnvironment.debug : BeagleEnvironment.production,
    components: {...defaultComponents, ...myCustomComponents},
    actions: myCustomActions,
    navigationControllers: {
      'general': NavigationController(
        isDefault: true,
        loadingComponent: 'custom:loading',
      ),
    },
    designSystem: AppDesignSystem(),
  );

  runApp(const MaterialApp(home: BeagleSampleApp()));
}

class BeagleSampleApp extends StatelessWidget {
  const BeagleSampleApp({Key key}) : super(key: key);

  static final _appBarMenuOptions = [
    MenuOption(title: 'Tab Bar', route: 'tab-bar'),
    MenuOption(title: 'Page View', route: 'page-view-screen'),
    MenuOption(title: 'Touchable', route: 'touchable'),
    MenuOption(title: 'Web View', route: 'web-view'),
  ];

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Beagle Sample',
      theme: ThemeData(
        visualDensity: VisualDensity.adaptivePlatformDensity,
        indicatorColor: Colors.white,
        appBarTheme: const AppBarTheme(
          elevation: 0,
        ),
      ),
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Beagle Sample'),
          actions: [
            PopupMenuButton(
              onSelected: (MenuOption result) {
                _handleAppBarMenuOption(result, context);
              },
              itemBuilder: (BuildContext context) {
                return _appBarMenuOptions.map((menuOption) {
                  return PopupMenuItem<MenuOption>(
                    value: menuOption,
                    child: Text(menuOption.title),
                  );
                }).toList();
              },
            ),
          ],
        ),
        body: BeagleWidget(
          screenRequest: BeagleScreenRequest('components'),
          onCreateView: (view) => {
            view.addErrorListener((errors) {
              //TODO
            })
          },
        ),
      ),
    );
  }

  void _handleAppBarMenuOption(MenuOption menuOption, BuildContext context) {
    Navigator.push(
      context,
      MaterialPageRoute<BeagleSampleScreen>(
        builder: (buildContext) => BeagleSampleScreen(
          title: menuOption.title,
          route: menuOption.route,
        ),
      ),
    );
  }
}

class MenuOption {
  MenuOption({this.title, this.route});

  final String title;
  final String route;
}
