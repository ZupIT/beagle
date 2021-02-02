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

import 'package:beagle/beagle_remote_view.dart';
import 'package:flutter/material.dart';

class TabBarScreen extends StatefulWidget {
  const TabBarScreen({Key key}) : super(key: key);

  @override
  _TabBarScreenState createState() => _TabBarScreenState();
}

class _TabBarScreenState extends State<TabBarScreen> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Tab Bar',
      theme: Theme.of(context).copyWith(
        indicatorColor: Colors.white,
        appBarTheme: const AppBarTheme(
          elevation: 0,
        ),
      ),
      home: Scaffold(
        appBar: AppBar(
          title: const Text('TabBar Sample'),
        ),
        body: const BeagleRemoteView(route: '/beagle_tab_bar'),
      ),
    );
  }
}
