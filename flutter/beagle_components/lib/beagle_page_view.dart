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
import 'package:flutter/widgets.dart';

/// Defines a specialized container to hold pages [children] that will be
/// displayed horizontally.
class BeaglePageView extends StatefulWidget {
  const BeaglePageView({
    Key key,
    this.children,
    this.onPageChange,
    this.currentPage,
  }) : super(key: key);

  /// A List of widgets that will be showed as pages in this widget.
  final List<Widget> children;

  /// Action that will be performed when a page is changed.
  final void Function(int) onPageChange;

  /// Current selected page.
  final int currentPage;

  @override
  _BeaglePageViewState createState() => _BeaglePageViewState();
}

class _BeaglePageViewState extends State<BeaglePageView> {
  PageController _pageController;

  @override
  void initState() {
    super.initState();
    _pageController = PageController(initialPage: widget.currentPage ?? 0);
  }

  @override
  void didUpdateWidget(covariant BeaglePageView oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.currentPage != widget.currentPage) {
      _pageController.jumpToPage(widget.currentPage);
    }
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return PageView(
      controller: _pageController,
      onPageChanged: (page) {
        if (widget.onPageChange != null) widget.onPageChange(page);
      },
      children: widget.children,
    );
  }
}
