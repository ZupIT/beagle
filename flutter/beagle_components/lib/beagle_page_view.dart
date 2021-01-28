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

import 'package:flutter/widgets.dart';

class BeaglePageView extends StatefulWidget {
  const BeaglePageView({
    Key key,
    this.children,
    this.onPageChange,
    this.currentPage,
  }) : super(key: key);

  final List<Widget> children;

  final Function onPageChange;
  final int currentPage;

  @override
  _BeaglePageViewState createState() => _BeaglePageViewState();
}

class _BeaglePageViewState extends State<BeaglePageView> {
  PageController _pageController;

  @override
  void initState() {
    super.initState();
    _pageController = PageController(initialPage: widget.currentPage);
    _pageController.addListener(() {
      print(_pageController.page.round());
    });
  }

  @override
  void didUpdateWidget(covariant BeaglePageView oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.currentPage != widget.currentPage) {
      _pageController.jumpToPage(widget.currentPage);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: PageView(
        controller: _pageController,
        onPageChanged: (page) {
          print('onPageChange got called!');
          widget.onPageChange({'value': page});
        },
        children: widget.children,
        // [
        //   Text('Page 1 ${widget.currentPage}'),
        //   Text('Page 2 ${widget.currentPage}'),
        // ],
      ),
    );
  }
}

// override val children: List<ServerDrivenComponent>,
// override val context: ContextData? = null,
// val onPageChange: List<Action>? = null,
