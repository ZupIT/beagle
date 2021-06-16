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

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.ContextComponent
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.pager.PageIndicatorComponent
import br.com.zup.beagle.widget.utils.BeagleConstants.DEPRECATED_PAGE_VIEW

/**
 *  The PageView component is a specialized container to hold pages (views) that will be displayed horizontally.
 *
 * @param children define a List of components (views) that are contained on this PageView. Consider the
 * @param pageIndicator defines in what page the PageView is currently on.
 * @param context define the contextData that be set to pageView.
 * @param onPageChange List of actions that are performed when you are on the selected page.
 * @param currentPage Integer number that identifies that selected.
 * @param showArrow This attribute is specific to the web platform, with which it allows you to place the arrows
 * to change pages.
 */

data class PageView(
    val children: List<ServerDrivenComponent>? = null,
    @Deprecated(DEPRECATED_PAGE_VIEW)
    val pageIndicator: PageIndicatorComponent? = null,
    override val context: ContextData? = null,
    val onPageChange: List<Action>? = null,
    val currentPage: Bind<Int>? = null,
    val showArrow: Boolean? = null
) : ServerDrivenComponent, ContextComponent {

    @Deprecated(DEPRECATED_PAGE_VIEW)
    constructor(
        children: List<ServerDrivenComponent>? = null,
        pageIndicator: PageIndicatorComponent? = null,
        context: ContextData? = null,
        showArrow: Boolean? = null
    ) : this(
        children,
        pageIndicator,
        context,
        null,
        null,
        showArrow
    )

    constructor(
        children: List<ServerDrivenComponent>? = null,
        context: ContextData? = null,
        onPageChange: List<Action>? = null,
        currentPage: Bind<Int>? = null,
        showArrow: Boolean? = null
    ) : this(
        children,
        null,
        context,
        onPageChange,
        currentPage,
        showArrow
    )
}
