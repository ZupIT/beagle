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

package br.com.zup.beagle.widget.layout

import br.com.zup.beagle.analytics.ScreenAnalytics
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.builder.BeagleBuilder
import br.com.zup.beagle.widget.builder.BeagleListBuilder
import br.com.zup.beagle.widget.context.ContextComponent
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.ui.ImagePath
import kotlin.properties.Delegates

/**
 * The SafeArea will enable Safe areas to help you place your views within the visible portion of the overall interface.
 * <p>
 * Note: This class is only used to iOS SafeArea
 * </p>
 *
 * @param top enable the safeArea constraint only on the TOP of the screen view.
 * @param leading enable the safeArea constraint only on the LEFT side of the screen view.
 * @param bottom enable the safeArea constraint only on the BOTTOM of the screen view.
 * @param trailing enable the safeArea constraint only on the RIGHT of the screen view.
 *
 */
data class SafeArea(
    val top: Boolean? = null,
    val leading: Boolean? = null,
    val bottom: Boolean? = null,
    val trailing: Boolean? = null
) {
    class Builder : BeagleBuilder<SafeArea> {
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
}

fun safeArea(block: SafeArea.Builder.() -> Unit) = SafeArea.Builder().apply(block).build()

/**
 *  The SafeArea will enable Safe areas to help you place your views
 *  within the visible portion of the overall interface.
 * @see Accessibility
 *
 * @param text define the Title on the navigation bar
 * @param image defines an image for your navigation bar
 * @param action defines an action to be called when the item is clicked on.
 * @param accessibility define Accessibility details for the item
 *
 */

data class NavigationBarItem(
    val text: String,
    val image: ImagePath.Local? = null,
    val action: Action,
    val accessibility: Accessibility? = null
) : IdentifierComponent {
    override var id: String? = null

    class Builder : BeagleBuilder<NavigationBarItem> {
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

        fun image(block: ImagePath.Local.Builder.() -> Unit) {
            image(ImagePath.Local.Builder().apply(block).build())
        }

        fun action(block: () -> Action) {
            action(block.invoke())
        }

        fun accessibility(block: () -> Accessibility?) {
            accessibility(block.invoke())
        }

        override fun build() = NavigationBarItem(
            text = text,
            image = image,
            action = action,
            accessibility = accessibility
        )
    }
}

fun navigationBarItem(block: NavigationBarItem.Builder.() -> Unit)
    = NavigationBarItem.Builder().apply(block).build()

/**
 *  Typically displayed at the top of the window, containing buttons for navigating within a hierarchy of screens
 *
 * @see Accessibility
 * @see NavigationBarItem
 *
 * @param title define the Title on the navigation bar
 * @param showBackButton enable a back button into your action bar/navigation bar
 * @param styleId could define a custom layout for your action bar/navigation  bar
 * @param navigationBarItems defines a List of navigation bar items.
 * @param backButtonAccessibility define accessibility details for the item
 *
 */
data class NavigationBar(
    val title: String,
    val showBackButton: Boolean = true,
    val styleId: String? = null,
    val navigationBarItems: List<NavigationBarItem>? = null,
    val backButtonAccessibility: Accessibility? = null
) {

    @Suppress("TooManyFunctions")
    class Builder : BeagleBuilder<NavigationBar> {
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

        fun backButtonAccessibility(block: () -> Accessibility?) {
            backButtonAccessibility(block.invoke())
        }

        override fun build() = NavigationBar(
            title = title,
            showBackButton = showBackButton,
            styleId = styleId,
            navigationBarItems = navigationBarItems,
            backButtonAccessibility = backButtonAccessibility
        )
    }
}

fun navigationBar(block: NavigationBar.Builder.() -> Unit) = NavigationBar.Builder().apply(block).build()


/**
 * The screen element will help you define the screen view structure.
 * By using this component you can define configurations like whether or
 * not you want to use safe areas or display a tool bar/navigation bar.
 *
 * @see NavigationBar
 * @see ServerDrivenComponent
 * @see Style
 * @see ScreenEvent
 *
 * @param identifier
 *                      identifies your screen globally inside your
 *                      application so that it could have actions set on itself.
 * @param safeArea
 *                      enable Safe areas to help you place your views within the visible
 *                      portion of the overall interface.
 *                      By default it is not enabled and it wont constrain considering any safe area.
 * @param navigationBar enable a action bar/navigation bar into your view. By default it is set as null.
 * @param child
 *                  define the child elements on this screen.
 *                  It could be any visual component that extends the ServerDrivenComponent.1
 * @param style enable a few visual options to be changed.
 * @param screenAnalyticsEvent send event when screen appear/disappear
 *
 */
data class Screen(
    val identifier: String? = null,
    val safeArea: SafeArea? = null,
    val navigationBar: NavigationBar? = null,
    val child: ServerDrivenComponent,
    val style: Style? = null,
    override val screenAnalyticsEvent: ScreenEvent? = null,
    override val context: ContextData? = null
) : ScreenAnalytics, ContextComponent {

    @Suppress("TooManyFunctions")
    class Builder : BeagleBuilder<Screen> {
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

        fun safeArea(block: SafeArea.Builder.() -> Unit) {
            safeArea(SafeArea.Builder().apply(block).build())
        }

        fun navigationBar(block: NavigationBar.Builder.() -> Unit) {
            navigationBar(NavigationBar.Builder().apply(block).build())
        }

        fun child(block: () -> ServerDrivenComponent) {
            child(block.invoke())
        }

        fun style(block: () -> Style?) {
            style(block.invoke())
        }

        fun screenAnalyticsEvent(block: () -> ScreenEvent?) {
            screenAnalyticsEvent(block.invoke())
        }

        fun context(block: ContextData.Builder.() -> Unit) {
            context(ContextData.Builder().apply(block).build())
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
}

fun screen(block: Screen.Builder.() -> Unit) = Screen.Builder().apply(block).build()