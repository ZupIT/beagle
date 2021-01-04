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

class BeagleTextInput extends StatefulWidget {
  const BeagleTextInput(
      {Key key,
      this.value,
      this.onChange,
      this.onBlur,
      this.onFocus,
      this.placeholder})
      : super(key: key);

  final String value;
  final Function onChange;
  final Function onBlur;
  final Function onFocus;
  final String placeholder;

  @override
  _BeagleTextInput createState() => _BeagleTextInput();
}

class _BeagleTextInput extends State<BeagleTextInput> {
  TextEditingController _controller;
  FocusNode _focus;

  @override
  void initState() {
    super.initState();

    _focus = FocusNode();
    _focus.addListener(() {
      if (_focus.hasFocus && widget.onFocus != null) {
        widget.onFocus({'value': _controller.text});
      }

      if (!_focus.hasFocus && widget.onBlur != null) {
        widget.onBlur({'value': _controller.text});
      }
    });

    _controller = TextEditingController();
    _controller.addListener(() {
      widget.onChange({'value': _controller.text});
    });
  }

  @override
  Widget build(BuildContext context) {
    if (_controller != null && widget.value != _controller.text) {
      _controller.text = widget.value;
    }

    return TextField(
        controller: _controller,
        focusNode: _focus,
        decoration: InputDecoration(
          border: const OutlineInputBorder(),
          labelText: widget.placeholder,
        ));
  }
}
