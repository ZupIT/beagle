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

import 'package:beagle/logger/beagle_logger.dart';
import 'package:beagle/model/beagle_style.dart';
import 'package:beagle/service_locator.dart';
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

  /// List of tabs that will be displayed. Note that the number of tabs of a TabView is final and cannot be changed
  /// after the component has initialized.
  final List<TabBarItem> items;

  /// Currently selected Tab.
  final int currentTab;

  /// Action that will be performed when a tab is pressed.
  final void Function(int) onTabSelection;

  @override
  _BeagleTabBarState createState() => _BeagleTabBarState();
}

class _BeagleTabBarState extends State<BeagleTabBar>
    with TickerProviderStateMixin {
  TabController _tabController;
  final BeagleLogger _logger = beagleServiceLocator<BeagleLogger>();
  static final imageStyle = BeagleStyle(size: Size(height: UnitValue(value: 32, type: UnitType.REAL)));

  @override
  void initState() {
    super.initState();
    _tabController = TabController(
      initialIndex: widget.currentTab ?? 0,
      length: widget.items == null ? 0 : widget.items.length,
      vsync: this,
    );
  }

  @override
  void didUpdateWidget(covariant BeagleTabBar oldWidget) {
    super.didUpdateWidget(oldWidget);
    final currentSelectedTab = widget.currentTab ?? 0;
    final previousSelectedTab = oldWidget.currentTab ?? 0;

    if (previousSelectedTab != currentSelectedTab) {
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
    if (widget.items == null || widget.items.isEmpty) {
      _logger.error('A tab bar should have at least one item. The tab bar with id ${widget.key} has no items.');
      return Container();
    }

    final _platform = Theme.of(context).platform;
    return _platform == TargetPlatform.iOS
        ? buildCupertinoWidget()
        : buildMaterialWidget();
  }

  Widget buildCupertinoWidget() {
    return CupertinoTabBar(
      currentIndex: widget.currentTab,
      onTap: widget.onTabSelection,
      items: buildCupertinoTabs(),
    );
  }

  List<BottomNavigationBarItem> buildCupertinoTabs() {
    return widget.items.map((tabBarItem) {
      return BottomNavigationBarItem(
        label: tabBarItem.title ?? '',
        icon: tabBarItem.icon == null
          ? null
          : BeagleImage(path: tabBarItem.icon, style: imageStyle),
      );
    }).toList();
  }

  Widget buildMaterialWidget() {
    return Container(
      // TODO: check if its viable to maintain this
      color: Theme.of(context).primaryColor,
      child: TabBar(
        controller: _tabController,
        onTap: widget.onTabSelection,
        tabs: buildMaterialTabs(),
      ),
    );
  }

  List<Widget> buildMaterialTabs() {
    return widget.items
        .map(
          (tabBarItem) => Tab(
            text: tabBarItem.title,
            icon: tabBarItem.icon == null
              ? null
              : BeagleImage(path: tabBarItem.icon, style: imageStyle),
          ),
        )
        .toList();
  }
}

class TabBarItem {
  TabBarItem(this.title, this.icon);

  TabBarItem.fromJson(Map<String, dynamic> json)
      : title = json['title'] ?? '',
        icon = json['icon'] == null ? null : LocalImagePath.fromJson(json['icon']);

  final String title;
  final LocalImagePath icon;
}
