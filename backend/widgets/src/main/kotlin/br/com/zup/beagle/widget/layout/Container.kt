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

import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.builder.BeagleWidgetBuilder
import br.com.zup.beagle.widget.context.ContextComponent
import br.com.zup.beagle.widget.context.ContextData
import kotlin.properties.Delegates

/**
 *  The container component is a general container that can hold other components inside.
 *
 * @param children define a list of components that are part of the container.
 *
 */
data class Container(
    val children: List<ServerDrivenComponent>,
    override val context: ContextData? = null,
    val onInit: List<Action>? = null
) : Widget(), ContextComponent {
    companion object{
        @JvmStatic
        fun builder() = Builder()
    }
    class Builder : BeagleWidgetBuilder<Container> {
        var children: MutableList<ServerDrivenComponent> by Delegates.notNull()
        var context: ContextData? = null
        var onInit: MutableList<Action>? = null

        fun children(children: List<ServerDrivenComponent>)
            = this.apply { this.children = children.toMutableList() }
        fun context(context: ContextData?) = this.apply { this.context = context }
        fun onInit(onInit: List<Action>?) = this.apply { this.onInit = onInit?.toMutableList() }

        fun children(block: BeagleListBuilder<ServerDrivenComponent>.() -> Unit) {
            children(BeagleListBuilder<ServerDrivenComponent>().apply(block).build())
        }

        fun context(block: ContextData.Builder.() -> Unit) {
            context(ContextData.Builder().apply(block).build())
        }

        fun onInit(block: BeagleListBuilder<Action>.() -> Unit) {
            onInit(BeagleListBuilder<Action>().apply(block).buildNullable())
        }

        override fun build() = Container(
            children = children,
            context = context,
            onInit = onInit
        )

    }
}

fun container(block: Container.Builder.() -> Unit) = Container.Builder().apply(block).build()
