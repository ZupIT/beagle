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

import br.com.zup.beagle.core.CoreDeclarativeDsl
import br.com.zup.beagle.core.LayoutComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.ServerDrivenComponentList

/**
 * component is a specialized container that will display its children vertically.
 *
 * @param children define a list of components to be displayed on this view.
 * @param reversed will change the children display direction.
 *
 */
data class Vertical(
    val children: List<ServerDrivenComponent>,
    val reversed: Boolean? = null
) : ServerDrivenComponent, LayoutComponent

fun vertical(block: VerticalBuilder.() -> Unit): Vertical = VerticalBuilder().apply(block).build()

@CoreDeclarativeDsl
class VerticalBuilder {

    var reversed: Boolean? = null
    private val children = mutableListOf<ServerDrivenComponent>()

    fun children(block: ServerDrivenComponentList.() -> Unit) {
        children.addAll(ServerDrivenComponentList().apply(block))
    }

    fun build(): Vertical = Vertical(children, reversed)

}

fun ServerDrivenComponentList.horizontal(block: VerticalBuilder.() -> Unit) {
    add(VerticalBuilder().apply(block).build())
}
