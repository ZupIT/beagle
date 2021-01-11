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
// 'https://gist.githubusercontent.com/Tiagoperes/89739c4c93a2f82b0ceb130921c3bf56/raw/3d1371fa96d4db0d5cc8d87c1ab409f52262d049';
    'http://10.0.2.2:8080';

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
    'custom:loading': (element, children) {
      return Center(
          key: element.getKey(), child: const Text('My custom loading.'));
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
    // BeagleInitializer.getService().globalContext.set(5, 'counter');
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
            // ? const BeagleRemoteView(route: '/global.json')
            ? const BeagleRemoteView(route: '/button')
            : const Center(child: Text('Not ready yet!')),
      ),
    );
  }
}
