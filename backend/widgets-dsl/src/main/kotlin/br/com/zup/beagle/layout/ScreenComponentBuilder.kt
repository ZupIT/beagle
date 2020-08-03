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

package br.com.zup.beagle.layout

import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.builder.analytics.ScreenEventBuilder
import br.com.zup.beagle.builder.core.AccessibilityBuilder
import br.com.zup.beagle.builder.core.StyleBuilder
import br.com.zup.beagle.context.ContextDataBuilder
import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ui.ImagePathLocalBuilder
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.SafeArea
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.ImagePath
import kotlin.properties.Delegates

fun safeArea(block: SafeAreaBuilder.() -> Unit) = SafeAreaBuilder().apply(block).build()

class SafeAreaBuilder : BeagleBuilder<SafeArea> {
    var top: Boolean? = null
    var leading: Boolean? = null
    var bottom: Boolean? = null
    var trailing: Boolean? = null

    fun top(top: Boolean?) = this.apply { this.top = top }
    fun leading(leading: Boolean?) = this.apply { this.leading = leading }
    fun bottom(bottom: Boolean?) = this.apply { this.bottom = bottom }
    fun trailing(trailing: Boolean?) = this.apply { this.trailing = trailing }

    fun top(block: () -> Boolean?) {
        top(block.invoke())
    }

    fun leading(block: () -> Boolean?) {
        leading(block.invoke())
    }

    fun bottom(block: () -> Boolean?) {
        bottom(block.invoke())
    }

    fun trailing(block: () -> Boolean?) {
        trailing(block.invoke())
    }

    override fun build() = SafeArea(
        top = top,
        leading = leading,
        bottom = bottom,
        trailing = trailing
    )
}

fun navigationBarItem(block: NavigationBarItemBuilder.() -> Unit)
    = NavigationBarItemBuilder().apply(block).build()

class NavigationBarItemBuilder : BeagleBuilder<NavigationBarItem> {
    var text: String by Delegates.notNull()
    var image: ImagePath.Local? = null
    var action: Action by Delegates.notNull()
    var accessibility: Accessibility? = null

    fun text(text: String) = this.apply { this.text = text }
    fun image(image: ImagePath.Local?) = this.apply { this.image = image }
    fun action(action: Action) = this.apply { this.action = action }
    fun accessibility(accessibility: Accessibility?) = this.apply { this.accessibility = accessibility }

    fun text(block: () -> String) {
        text(block.invoke())
    }

    fun image(block: ImagePathLocalBuilder.() -> Unit) {
        image(ImagePathLocalBuilder().apply(block).build())
    }

    fun action(block: () -> Action) {
        action(block.invoke())
    }

    fun accessibility(block: AccessibilityBuilder.() -> Unit) {
        accessibility(AccessibilityBuilder().apply(block).build())
    }

    override fun build() = NavigationBarItem(
        text = text,
        image = image,
        action = action,
        accessibility = accessibility
    )
}

fun navigationBar(block: NavigationBarBuilder.() -> Unit) = NavigationBarBuilder().apply(block).build()

class NavigationBarBuilder : BeagleBuilder<NavigationBar> {
    var title: String by Delegates.notNull()
    var showBackButton: Boolean = true
    var styleId: String? = null
    var navigationBarItems: MutableList<NavigationBarItem>? = null
    var backButtonAccessibility: Accessibility? = null

    fun title(title: String) = this.apply { this.title = title }
    fun showBackButton(showBackButton: Boolean) = this.apply { this.showBackButton = showBackButton }
    fun styleId(styleId: String?) = this.apply { this.styleId = styleId }
    fun navigationBarItems(navigationBarItems: List<NavigationBarItem>?)
        = this.apply { this.navigationBarItems = navigationBarItems?.toMutableList() }
    fun backButtonAccessibility(backButtonAccessibility: Accessibility?)
        = this.apply { this.backButtonAccessibility = backButtonAccessibility }

    fun title(block: () -> String) {
        title(block.invoke())
    }

    fun showBackButton(block: () -> Boolean) {
        showBackButton(block.invoke())
    }

    fun styleId(block: () -> String?) {
        styleId(block.invoke())
    }

    fun navigationBarItems(block: BeagleListBuilder<NavigationBarItem>.() -> Unit) {
        navigationBarItems(BeagleListBuilder<NavigationBarItem>().apply(block).buildNullable())
    }

    fun backButtonAccessibility(block: AccessibilityBuilder.() -> Unit) {
        backButtonAccessibility(AccessibilityBuilder().apply(block).build())
    }

    override fun build() = NavigationBar(
        title = title,
        showBackButton = showBackButton,
        styleId = styleId,
        navigationBarItems = navigationBarItems,
        backButtonAccessibility = backButtonAccessibility
    )
}

fun screen(block: ScreenComponentBuilder.() -> Unit) = ScreenComponentBuilder().apply(block).build()

class ScreenComponentBuilder: BeagleBuilder<Screen> {
    var identifier: String? = null
    var safeArea: SafeArea? = null
    var navigationBar: NavigationBar? = null
    var child: ServerDrivenComponent by Delegates.notNull()
    var style: Style? = null
    var screenAnalyticsEvent: ScreenEvent? = null
    var context: ContextData? = null

    fun identifier(identifier: String?) = this.apply { this.identifier = identifier }
    fun safeArea(safeArea: SafeArea?) = this.apply { this.safeArea = safeArea }
    fun navigationBar(navigationBar: NavigationBar?) = this.apply { this.navigationBar = navigationBar }
    fun child(child: ServerDrivenComponent) = this.apply { this.child = child }
    fun style(style: Style?) = this.apply { this.style = style }
    fun screenAnalyticsEvent(screenAnalyticsEvent: ScreenEvent?)
        = this.apply { this.screenAnalyticsEvent = screenAnalyticsEvent }
    fun context(context: ContextData?) = this.apply { this.context = context }

    fun identifier(block: () -> String?) {
        identifier(block.invoke())
    }

    fun safeArea(block: SafeAreaBuilder.() -> Unit) {
        safeArea(SafeAreaBuilder().apply(block).build())
    }

    fun navigationBar(block: NavigationBarBuilder.() -> Unit) {
        navigationBar(NavigationBarBuilder().apply(block).build())
    }

    fun child(block: () -> ServerDrivenComponent) {
        child(block.invoke())
    }

    fun style(block: StyleBuilder.() -> Unit) {
        style(StyleBuilder().apply(block).build())
    }

    fun screenAnalyticsEvent(block: ScreenEventBuilder.() -> Unit) {
        screenAnalyticsEvent(ScreenEventBuilder().apply(block).build())
    }

    fun context(block: ContextDataBuilder.() -> Unit) {
        context(ContextDataBuilder().apply(block).build())
    }

    override fun build() = Screen(
        identifier = identifier,
        safeArea = safeArea,
        navigationBar = navigationBar,
        child = child,
        style = style,
        screenAnalyticsEvent = screenAnalyticsEvent,
        context = context
    )
}