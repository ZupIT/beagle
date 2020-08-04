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

package br.com.zup.beagle.widget.ui

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.ContextComponent
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.core.ListDirection
import java.awt.Container

/**
 * ListView is a Layout component that will define a list of views natively.
 * These views could be any Server Driven Component.
 *
 * @param children define the items on the list view.
 * @param direction define the list direction.
 *
 */
data class ListView(
    val children: List<ServerDrivenComponent>? = null,
    override val context: ContextData? = null,
    val onInit: List<Action>? = null,
    val dataSource: Bind<List<Any>>? = null,
    val direction: ListDirection,
    val template: ServerDrivenComponent? = null,
    val onScrollEnd: List<Action>? = null,
    val scrollThreshold: Int? = null,
    val useParentScroll: Boolean = false
) : Widget(), ContextComponent {

    companion object {}

    @Deprecated(message = "", replaceWith = ReplaceWith("")) //TODO(put message here, implement replaceWith)
    constructor(
        children: List<ServerDrivenComponent>,
        direction: ListDirection
    ) : this(
        context = null,
        children = children,
        direction = direction
    )

    constructor(
        context: ContextData? = null,
        onInit: List<Action>? = null,
        dataSource: Bind<List<Any>>,
        direction: ListDirection,
        template: ServerDrivenComponent,
        onScrollEnd: List<Action>? = null,
        scrollThreshold: Int? = null,
        useParentScroll: Boolean = false
    ) : this(
        children = null,
        context = context,
        onInit = onInit,
        dataSource = dataSource,
        direction = direction,
        template = template,
        onScrollEnd = onScrollEnd,
        scrollThreshold = scrollThreshold,
        useParentScroll = useParentScroll
    )
}