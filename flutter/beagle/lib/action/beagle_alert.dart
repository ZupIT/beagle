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

import 'package:flutter/material.dart';

class BeagleAlert extends StatefulWidget {
  const BeagleAlert({Key key, this.child}) : super(key: key);

  final Widget child;
  static _BeagleAlert dialog;

  static void showAlertDialog(
      {String title, String message, Function onPressOk}) {
    dialog.show(message: message, onPressOk: onPressOk, title: title);
  }

  @override
  _BeagleAlert createState() => _BeagleAlert();
}

class _BeagleAlert extends State<BeagleAlert> {
  @override
  void initState() {
    super.initState();
    BeagleAlert.dialog = this;
  }

  void show({String title, String message, Function onPressOk}) {
    // set up the button
    final Widget okButton = FlatButton(
      onPressed: () {
        Navigator.pop(context);
        if (onPressOk != null) {
          onPressOk();
        }
      },
      child: const Text('OK'),
    );
    // set up the AlertDialog
    final alert = AlertDialog(
      title: Text(title),
      content: Text(message),
      actions: [
        okButton,
      ],
    );
    // show the dialog
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return widget.child;
  }
}
