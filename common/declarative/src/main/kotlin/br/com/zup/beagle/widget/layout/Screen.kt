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

import br.com.zup.beagle.action.Action
import br.com.zup.beagle.analytics.ScreenAnalytics
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.core.*

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
)

fun safeArea(block: SafeAreaActionBuilder.() -> Unit): SafeArea = SafeAreaActionBuilder().apply(block).build()

@CoreDeclarativeDsl
class SafeAreaActionBuilder {

    var top: Boolean? = null
    var leading: Boolean? = null
    var bottom: Boolean? = null
    var trailing: Boolean? = null

    fun build(): SafeArea = SafeArea(top, leading, bottom, trailing)

}


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
    val image: String? = null,
    val action: Action,
    val accessibility: Accessibility? = null,
    override val id: String? = null
) : IdentifierComponent

class NavigationBarItems : ArrayList<NavigationBarItem>() {

    fun navigationBarItem(block: NavigationBarItemBuilder.() -> Unit) {
        add(NavigationBarItemBuilder().apply(block).build())
    }

}

fun navigationBarItem(block: NavigationBarItemBuilder.() -> Unit): NavigationBarItem =
    NavigationBarItemBuilder().apply(block).build()

@CoreDeclarativeDsl
class NavigationBarItemBuilder {

    var text: String = ""
    var image: String? = null
    var action: Action? = null
    var accessibility: Accessibility? = null
    var id: String? = null

    fun build(): NavigationBarItem = NavigationBarItem(text = text, image = image, accessibility = accessibility,
        id = id,
        //TODO NEED TO BE IMPLEMENTS REQUIRED BY DSL
        action = action!!)

}

/**
 *  Typically displayed at the top of the window, containing buttons for navigating within a hierarchy of screens
 *
 * @see Accessibility
 * @see NavigationBarItem
 *
 * @param title define the Title on the navigation bar
 * @param showBackButton enable a back button into your action bar/navigation bar
 * @param style could define a custom layout for your action bar/navigation  bar
 * @param navigationBarItems defines a List of navigation bar items.
 * @param backButtonAccessibility define accessibility details for the item
 *
 */
data class NavigationBar(
    val title: String,
    val showBackButton: Boolean = true,
    val style: String? = null,
    val navigationBarItems: List<NavigationBarItem>? = null,
    val backButtonAccessibility: Accessibility? = null
)

fun navigationBar(block: NavigationBarBuilder.() -> Unit): NavigationBar = NavigationBarBuilder().apply(block).build()

@CoreDeclarativeDsl
class NavigationBarBuilder {

    var title: String = ""
    var showBackButton: Boolean = true
    var style: String? = null
    var backButtonAccessibility: Accessibility? = null

    private val navigationBarItems = mutableListOf<NavigationBarItem>()

    fun navigationBarItems(block: NavigationBarItems.() -> Unit) {
        navigationBarItems.addAll(NavigationBarItems().apply(block))
    }

    fun build(): NavigationBar = NavigationBar(title, showBackButton, style, navigationBarItems, backButtonAccessibility)

}


/**
 * The screen element will help you define the screen view structure.
 * By using this component you can define configurations like whether or
 * not you want to use safe areas or display a tool bar/navigation bar.
 *
 * @see NavigationBar
 * @see ServerDrivenComponent
 * @see Appearance
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
 * @param appearance enable a few visual options to be changed.
 * @param screenAnalyticsEvent send event when screen appear/disappear
 *
 */
data class Screen(
    val identifier: String? = null,
    val safeArea: SafeArea? = null,
    val navigationBar: NavigationBar? = null,
    val child: ServerDrivenComponent,
    val appearance: Appearance? = null,
    override val screenAnalyticsEvent: ScreenEvent? = null
) : ScreenAnalytics

fun screen(block: ScreenCoreBuilder.() -> Unit): Screen = ScreenCoreBuilder().apply(block).build()

@CoreDeclarativeDsl
class ScreenCoreBuilder {

    var identifier: String? = null
    var safeArea: SafeArea? = null
    var navigationBar: NavigationBar? = null
    var child: ServerDrivenComponent? = null
    var appearance: Appearance? = null
    var screenAnalyticsEvent: ScreenEvent? = null

    fun build(): Screen = Screen(identifier = identifier, safeArea = safeArea, navigationBar = navigationBar,
        //TODO NEED TO BE IMPLEMENTS REQUIRED BY DSL
        child = child!!, appearance = appearance, screenAnalyticsEvent = screenAnalyticsEvent)

}

