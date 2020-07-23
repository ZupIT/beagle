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
import br.com.zup.beagle.widget.builder.BeagleBuilder
import br.com.zup.beagle.widget.builder.BeagleListBuilder
import br.com.zup.beagle.widget.core.ListDirection
import kotlin.properties.Delegates

/**
 * ListView is a Layout component that will define a list of views natively.
 * These views could be any Server Driven Component.
 *
 * @param children define the items on the list view.
 * @param direction define the list direction.
 *
 */
data class ListView(
    val children: List<ServerDrivenComponent>,
    val direction: ListDirection = ListDirection.VERTICAL
) : ServerDrivenComponent {
    companion object

    class Builder : BeagleBuilder<ListView> {
        var children: List<ServerDrivenComponent> by Delegates.notNull()
        var direction: ListDirection = ListDirection.VERTICAL

        fun children(children: List<ServerDrivenComponent>) = this.apply { this.children = children }
        fun direction(direction: ListDirection) = this.apply { this.direction = direction }

        fun children(block: BeagleListBuilder<ServerDrivenComponent>.() -> Unit) {
            children(BeagleListBuilder<ServerDrivenComponent>().apply(block).build())
        }

        fun direction(block: () -> ListDirection) {
            direction(block.invoke())
        }

        override fun build() = ListView(children, direction)
    }
}

fun listView(block: ListView.Builder.() -> Unit) = ListView.Builder().apply(block).build()