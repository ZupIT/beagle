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

import 'package:beagle/action/beagle_alert.dart';
import 'package:beagle/interface/action_handler.dart';
import 'package:beagle/model/beagle_action.dart';
import 'package:flutter/foundation.dart';

typedef ActionHandlerFunction = void Function(BeagleAction action);

class DefaultActionHandler implements ActionHandler {
  final Map<String, ActionHandlerFunction> _handlers = {
    'beagle:alert': (action) {
      BeagleAlert.showAlertDialog(
        message: action.getAttributeValue('message'),
        onPressOk: action.getAttributeValue('onPressOk', () {}),
        title: action.getAttributeValue('title', 'Alert'),
      );
    },
  };

  @override
  List<String> getActionKeys() {
    return _handlers.keys.toList();
  }

  @override
  void handleAction(BeagleAction action) {
    if (!_handlers.containsKey(action.getType())) {
      // ignore: only_throw_errors
      throw ErrorDescription(
          'No handler found for action of type ${action.getType()}');
    }
    _handlers[action.getType()](action);
  }
}
