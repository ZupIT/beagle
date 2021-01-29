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
import 'package:beagle_components/beagle_image.dart';
import 'package:flutter/material.dart';

class BeagleTabBar extends StatefulWidget {
  const BeagleTabBar({
    Key key,
    this.items,
    this.currentTab,
    this.onTabSelection,
  }) : super(key: key);

  final List<TabBarItem> items;
  final int currentTab;
  final Function onTabSelection;

  @override
  _BeagleTabBarState createState() => _BeagleTabBarState();
}

class _BeagleTabBarState extends State<BeagleTabBar>
    with TickerProviderStateMixin {
  TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(
        initialIndex: widget.currentTab,
        length: widget.items.length,
        vsync: this);
  }

  @override
  void didUpdateWidget(covariant BeagleTabBar oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (widget.currentTab != oldWidget.currentTab) {
      _tabController.animateTo(widget.currentTab);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      // TODO: check if its viable to maintain this
      color: Theme.of(context).primaryColor,
      child: TabBar(
        controller: _tabController,
        onTap: (tabIndex) {
          //TODO: check evaluation of implicit contexts.. (see sample json)
          widget.onTabSelection({'value': tabIndex});
        },
        tabs: createTabs(),
      ),
    );
  }

  List<Widget> createTabs() {
    return widget.items
        .map(
          (tabBarItem) => Tab(
            text: tabBarItem.title,
            // TODO: place dynamic icon
            icon: SizedBox(
              width: 24,
              height: 24,
              child: Image.asset(
                BeagleInitializer.designSystem.image(
                  tabBarItem.path.mobileId,
                ),
                fit: BoxFit.fill,
              ),
            ),
          ),
        )
        .toList();
  }
}

// TODO: check ImagePath
class TabBarItem {
  TabBarItem(this.title, this.path);

  TabBarItem.fromJson(Map<String, dynamic> json)
      : title = json['title'],
        path = ImagePath.fromJson(json['icon']);

  final String title;
  final LocalImagePath path;
}
// val icon: ImagePath.Local? = null,
