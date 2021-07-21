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

import 'package:flutter/material.dart';
import 'package:webview_flutter/platform_interface.dart';
import 'package:webview_flutter/webview_flutter.dart';

/// A web view widget for showing html content.
class BeagleWebView extends StatefulWidget {
  /// Creates a new web view.
  const BeagleWebView({
    Key key,
    this.url,
  }) : super(key: key);

  /// The initial URL to load.
  final String url;

  @override
  _BeagleWebView createState() => _BeagleWebView();
}

class _BeagleWebView extends State<BeagleWebView> {
  WebViewController _controller;

  @override
  void didUpdateWidget(covariant BeagleWebView oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (widget.url != oldWidget.url) {
      _controller.loadUrl(widget.url);
    }
  }

  @override
  Widget build(BuildContext context) {
    return WebView(
      initialUrl: widget.url,
      javascriptMode: JavascriptMode.unrestricted,
      onWebResourceError: _handleError,
      onPageStarted: _handleLoading,
      onPageFinished: _handleSuccess,
      onWebViewCreated: (WebViewController webViewController) =>
          _controller = webViewController,
    );
  }

  void _handleLoading(String url) {
    // TODO: loading handling is pending until definition of Beagle's screen state handling mechanism.
  }

  void _handleSuccess(String url) {
    // TODO: success handling is pending until definition of Beagle's screen state handling mechanism.
  }

  void _handleError(WebResourceError error) {
    // TODO: error handling is pending until definition of Beagle's screen state handling mechanism.
  }
}
