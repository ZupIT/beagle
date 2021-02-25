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

import 'package:beagle/service_locator.dart';
import 'package:beagle/setup/beagle_design_system.dart';
import 'package:beagle_components/beagle_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

/// Defines a widget that displays a horizontal row of tabs, that will be
/// rendered according to the style of the running platform.
class BeagleTabBar extends StatefulWidget {
  const BeagleTabBar({
    Key key,
    this.items,
    this.currentTab,
    this.onTabSelection,
  }) : super(key: key);

  /// List of tabs that will be displayed.
  final List<TabBarItem> items;

  /// Currently selected Tab.
  final int currentTab;

  /// Action that will be performed when a tab is pressed.
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
      vsync: this,
    );
  }

  @override
  void didUpdateWidget(covariant BeagleTabBar oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (widget.currentTab != oldWidget.currentTab) {
      _tabController.animateTo(widget.currentTab);
    }
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final _platform = Theme.of(context).platform;
    return _platform == TargetPlatform.iOS
        ? buildCupertinoWidget()
        : buildMaterialWidget();
  }

  Widget buildCupertinoWidget() {
    return CupertinoTabBar(
      currentIndex: widget.currentTab,
      onTap: (tabIndex) {
        widget.onTabSelection({'value': tabIndex});
      },
      items: buildCupertinoTabs(),
    );
  }

  List<BottomNavigationBarItem> buildCupertinoTabs() {
    return widget.items.map((tabBarItem) {
      return BottomNavigationBarItem(
        label: tabBarItem.title,
        icon: _getImageFromAsset(
          tabBarItem.path.mobileId,
        ),
      );
    }).toList();
  }

  Widget buildMaterialWidget() {
    return Container(
      // TODO: check if its viable to maintain this
      color: Theme.of(context).primaryColor,
      child: TabBar(
        controller: _tabController,
        onTap: (tabIndex) {
          widget.onTabSelection({'value': tabIndex});
        },
        tabs: buildMaterialTabs(),
      ),
    );
  }

  List<Widget> buildMaterialTabs() {
    return widget.items
        .map(
          (tabBarItem) => Tab(
            text: tabBarItem.title,
            icon: _getImageFromAsset(
              tabBarItem.path.mobileId,
            ),
          ),
        )
        .toList();
  }

  Image _getImageFromAsset(String mobileId) {
    final designSystem = beagleServiceLocator<BeagleDesignSystem>();

    final assetName = designSystem.image(mobileId);

    return assetName != null ? Image.asset(assetName, fit: BoxFit.fill) : null;
  }
}

class TabBarItem {
  TabBarItem(this.title, this.path);

  TabBarItem.fromJson(Map<String, dynamic> json)
      : title = json['title'],
        path = ImagePath.fromJson(json['icon']);

  final String title;
  final LocalImagePath path;
}
