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
import 'package:beagle/interface/beagle_service.dart';
import 'package:beagle/interface/navigation_controller.dart';
import 'package:beagle_components/beagle_components.dart';
import 'package:flutter/material.dart';

const BASE_URL =
    'https://gist.githubusercontent.com/paulomeurerzup/80e54caf96ba56ae96d07b4e671cae42/raw/02dc56a2f6f5a3ec515013de03c08f593d7260d4';

void main() {
  runApp(const BeagleSampleApp());
}

class BeagleSampleApp extends StatefulWidget {
  const BeagleSampleApp({Key key}) : super(key: key);

  @override
  _BeagleSampleApp createState() => _BeagleSampleApp();
}

class _BeagleSampleApp extends State<BeagleSampleApp> {
  bool isBeagleReady = false;
  Map<String, ComponentBuilder> myCustomComponents = {
    'custom:loading': (element, _, __) {
      return Center(
        key: element.getKey(),
        child: const Text('My custom loading.'),
      );
    }
  };
  Map<String, ActionHandler> myCustomActions = {
    'custom:log': ({action, view, element}) {
      debugPrint(action.getAttributeValue('message'));
    }
  };

  Future<void> startBeagle() async {
    await BeagleInitializer.start(
        baseUrl: BASE_URL,
        components: {...defaultComponents, ...myCustomComponents},
        actions: myCustomActions,
        navigationControllers: {
          'general': NavigationController(
              isDefault: true, loadingComponent: 'custom:loading'),
        });
    BeagleInitializer.getService().globalContext.set(5, 'counter');
    setState(() {
      isBeagleReady = true;
    });
  }

  @override
  void initState() {
    super.initState();
    startBeagle();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Beagle Sample',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      // home: JSCounter(),
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Beagle Sample'),
        ),
        body: isBeagleReady
            // ? const BeagleRemoteView(route: '/lazy.json')
            ? const BeagleRemoteView(route: '/beagle_image_remote')
            : const Center(
                child: Text('Not ready yet!'),
              ),
      ),
    );
  }
}
