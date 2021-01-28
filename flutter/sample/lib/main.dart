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
// 'https://gist.githubusercontent.com/Tiagoperes/89739c4c93a2f82b0ceb130921c3bf56/raw/041461163dbad2ec7c234cfd28325483f3750d0b';
    'https://gist.githubusercontent.com/paulomeurerzup/8f10b7c33a1e9418d0a769941e5232d8/raw/dde7defb986f0182e5e1b9b4cb2ad6eeff9693b9';

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
      return Text('My custom loading.', key: element.getKey());
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

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Beagle Sample',
      theme: ThemeData(
          // This is the theme of your application.
          //
          // Try running your application with "flutter run". You'll see the
          // application has a blue toolbar. Then, without quitting the app, try
          // changing the primarySwatch below to Colors.green and then invoke
          // "hot reload" (press "r" in the console where you ran "flutter run",
          // or simply save your changes to "hot reload" in a Flutter IDE).
          // Notice that the counter didn't reset back to zero; the application
          // is not restarted.
          primarySwatch: Colors.red,
          // This makes the visual density adapt to the platform that you run
          // the app on. For desktop platforms, the controls will be smaller and
          // closer together (more dense) than on mobile platforms.
          visualDensity: VisualDensity.adaptivePlatformDensity,
          tabBarTheme: const TabBarTheme(
            labelColor: Colors.black,
            unselectedLabelColor: Colors.grey,
          ),
          appBarTheme: const AppBarTheme(
            elevation: 0,
          )),
      // home: JSCounter(),
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Beagle Sample'),
        ),
        body:
            /*SingleChildScrollView(
          // padding: const EdgeInsets.all(16),
          child:*/
            Center(
          child: isBeagleReady
              ? const BeagleRemoteView(route: '/beagle_tab_bar')
              : const Text('Not ready yet!'),
        ),
        // ),
      ),
    );
  }
}
