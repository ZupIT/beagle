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

import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

import 'package:beagle_components/text_input_type.dart';

/// Defines a text field that lets the user enter text.
class BeagleTextInput extends StatefulWidget {
  const BeagleTextInput({
    Key key,
    this.value,
    this.placeholder,
    this.enabled,
    this.readOnly,
    this.type,
    this.error,
    this.showError,
    this.onChange,
    this.onBlur,
    this.onFocus,
  }) : super(key: key);

  /// Initial text displayed.
  final String value;

  /// A label text that is shown when the text is empty.
  final String placeholder;

  /// tells whether this field is enabled. Default is true.
  final bool enabled;

  /// tells whether this field is readOnly. Default is false.
  final bool readOnly;

  /// Type of data represented by the text input. This sets both the keyboard type and whether or not the content will
  /// be obscured. The content is obscured when the type is "PASSWORD". Note that Flutter can't change the keyboard
  /// type after the component is rendered, which means that, when this property is changed, only the effect to obscure
  /// the text content is updated.
  final BeagleTextInputType type;

  /// An error string for validation.
  final String error;

  /// Whether or not to show the error string. Default is false.
  final bool showError;

  /// Action that will be performed when text change.
  final Function onChange;

  /// Action that will be performed when the widget looses its focus.
  final Function onBlur;

  /// Action that will be performed when the widget acquire focus.
  final Function onFocus;

  @override
  _BeagleTextInput createState() => _BeagleTextInput();
}

class _BeagleTextInput extends State<BeagleTextInput> {
  TextEditingController _controller;
  FocusNode _focus;

  @override
  void initState() {
    log('Initializing TextField');
    super.initState();

    if (widget.onBlur != null || widget.onFocus != null) {
      _focus = FocusNode();
      _focus.addListener(() {
        if (_focus.hasFocus && widget.onFocus != null) {
          widget.onFocus({'value': _controller.text});
        }

        if (!_focus.hasFocus && widget.onBlur != null) {
          widget.onBlur({'value': _controller.text});
        }
      });
    }

    _controller = TextEditingController();
    if (widget.onChange != null) {
      _controller.addListener(() {
        if ((widget.value ?? '') != _controller.text) {
          widget.onChange({'value': _controller.text});
        }
      });
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    if (_focus != null) _focus.dispose();
    super.dispose();
  }

  Widget _buildMaterialWidget() {
    return TextField(
        controller: _controller,
        focusNode: _focus,
        enabled: widget.enabled != false,
        keyboardType: getMaterialInputType(widget.type),
        obscureText: widget.type == BeagleTextInputType.PASSWORD,
        readOnly: widget.readOnly == true,
        decoration: InputDecoration(
          border: const OutlineInputBorder(),
          errorText: widget.showError == true ? widget.error : null,
          labelText: widget.placeholder,
        )
    );
  }

  Widget _buildCupertinoWidget() {
    final hasError = widget.showError == true && widget.error != null && widget.error.isNotEmpty;
    final textField = CupertinoTextField(
        controller: _controller,
        focusNode: _focus,
        enabled: widget.enabled != false,
        keyboardType: getMaterialInputType(widget.type),
        obscureText: widget.type == BeagleTextInputType.PASSWORD,
        readOnly: widget.readOnly == true,
        placeholder: widget.placeholder,
        decoration: BoxDecoration(border: hasError? Border.all(color: Colors.red) : null)
    );
    return hasError
      ? Column(children: [textField, Text(widget.error, style: TextStyle(color: Colors.red))])
      : textField;
  }

  @override
  Widget build(BuildContext context) {
    if (_controller != null && widget.value != null && widget.value != _controller.text) {
      _controller.text = widget.value;
    }
    final platform = Theme.of(context).platform;
    return platform == TargetPlatform.iOS ? _buildCupertinoWidget() : _buildMaterialWidget();
  }
}
