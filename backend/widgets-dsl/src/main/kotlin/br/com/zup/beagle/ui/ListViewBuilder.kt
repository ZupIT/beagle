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

package br.com.zup.beagle.ui

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.ui.ListView
import kotlin.properties.Delegates

fun listView(block: ListViewBuilder.() -> Unit) = ListViewBuilder().apply(block).build()

class ListViewBuilder : BeagleBuilder<ListView> {
    var children: MutableList<ServerDrivenComponent> by Delegates.notNull()
    var direction: ListDirection = ListDirection.VERTICAL

    fun children(children: List<ServerDrivenComponent>)
        = this.apply { this.children = children.toMutableList() }
    fun direction(direction: ListDirection) = this.apply { this.direction = direction }

    fun children(block: BeagleListBuilder<ServerDrivenComponent>.() -> Unit) {
        children(BeagleListBuilder<ServerDrivenComponent>().apply(block).build())
    }

    fun direction(block: () -> ListDirection) {
        direction(block.invoke())
    }

    override fun build() = ListView(children, direction)
}