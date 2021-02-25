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

import 'package:beagle/beagle.dart';
import 'package:flutter/material.dart';

class BeagleSampleScreen extends StatefulWidget {
  const BeagleSampleScreen({
    Key key,
    this.title,
    this.route,
  }) : super(key: key);

  final String title;
  final String route;

  @override
  _BeagleSampleScreenState createState() => _BeagleSampleScreenState();
}

class _BeagleSampleScreenState extends State<BeagleSampleScreen> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: widget.title,
      theme: Theme.of(context).copyWith(
        indicatorColor: Colors.white,
        appBarTheme: const AppBarTheme(
          elevation: 0,
        ),
      ),
      home: Scaffold(
        appBar: AppBar(
          title: Text(widget.title),
        ),
        body: BeagleWidget(
          screenRequest: BeagleScreenRequest(widget.route)
        ),
      ),
    );
  }
}
