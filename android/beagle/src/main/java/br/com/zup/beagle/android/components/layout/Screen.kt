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

package br.com.zup.beagle.android.components.layout

import br.com.zup.beagle.analytics.ScreenAnalytics
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.BeagleJson
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.SingleChildComponent
import br.com.zup.beagle.core.Style

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

    @BeagleJson(name = "top")
    val top: Boolean? = null,

    @BeagleJson(name = "leading")
    val leading: Boolean? = null,

    @BeagleJson(name = "bottom")
    val bottom: Boolean? = null,

    @BeagleJson(name = "trailing")
    val trailing: Boolean? = null,
)

/**
 *  Defines a List of navigation bar items.
 *
 * @see Accessibility
 *
 * @param text define the Title on the navigation bar
 * @param image defines an image for your navigation bar
 * @param action defines an action to be called when the item is clicked on.
 * @param accessibility define Accessibility details for the item
 *
 */
data class NavigationBarItem(

    @BeagleJson(name = "text")
    val text: String,

    @BeagleJson(name = "image")
    val image: ImagePath.Local? = null,

    @BeagleJson(name = "action")
    val action: Action,

    @BeagleJson(name = "accessibility")
    val accessibility: Accessibility? = null,
) : IdentifierComponent {
    override var id: String? = null
}

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

    @BeagleJson(name = "title")
    val title: String,

    @BeagleJson(name = "showBackButton")
    val showBackButton: Boolean = true,

    @BeagleJson(name = "styleId")
    val styleId: String? = null,

    @BeagleJson(name = "navigationBarItems")
    val navigationBarItems: List<NavigationBarItem>? = null,

    @BeagleJson(name = "backButtonAccessibility")
    val backButtonAccessibility: Accessibility? = null,
)

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
 * @param context define the contextData that be set to screen.
 *
 */
@Suppress("DataClassPrivateConstructor")
data class Screen private constructor(

    @BeagleJson(name = "identifier")
    val identifier: String? = null,

    @BeagleJson(name = "safeArea")
    val safeArea: SafeArea? = null,

    @BeagleJson(name = "navigationBar")
    val navigationBar: NavigationBar? = null,

    @BeagleJson(name = "child")
    override val child: ServerDrivenComponent,

    @BeagleJson(name = "style")
    val style: Style? = null,

    @BeagleJson(name = "screenAnalyticsEvent")
    override val screenAnalyticsEvent: ScreenEvent? = null,

    @BeagleJson(name = "context")
    override val context: ContextData? = null,

    @BeagleJson(name = "id")
    override val id: String? = null,
) : ScreenAnalytics, ContextComponent, SingleChildComponent, IdentifierComponent {

    @Deprecated(
        "It was deprecated in version 1.5.0 and will be removed in a future version. Use field id instead.",
        replaceWith = ReplaceWith("Screen(id = )")
    )
    constructor(
        identifier: String?,
        safeArea: SafeArea? = null,
        navigationBar: NavigationBar? = null,
        child: ServerDrivenComponent,
        style: Style? = null,
        screenAnalyticsEvent: ScreenEvent? = null,
        context: ContextData? = null,
    ) : this(identifier, safeArea, navigationBar, child, style, screenAnalyticsEvent, context, null)

    constructor(
        safeArea: SafeArea? = null,
        navigationBar: NavigationBar? = null,
        child: ServerDrivenComponent,
        style: Style? = null,
        screenAnalyticsEvent: ScreenEvent? = null,
        context: ContextData? = null,
        id: String? = null,
    ) : this(null, safeArea, navigationBar, child, style, screenAnalyticsEvent, context, id)
}