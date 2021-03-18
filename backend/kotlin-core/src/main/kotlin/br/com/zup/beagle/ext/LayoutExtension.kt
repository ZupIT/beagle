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

package br.com.zup.beagle.ext

import br.com.zup.beagle.core.Layout
import br.com.zup.beagle.core.LayoutComponent
import br.com.zup.beagle.core.PositionType
import br.com.zup.beagle.core.Visibility
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size

/**
 *  The Layouted is a helper to apply layout in your component
 *
 * @param self the component will apply layout
 *
 */
@Suppress("FunctionNaming")
inline fun <T : LayoutComponent> Layouted(self: T, block: LayoutBuilder.() -> Unit): T {
    self.layout = LayoutBuilder(self.layout).apply(block).build()
    return self
}

/**
 *  The LayoutBuilder is a helper to set style options in your component.
 *  with this method you don't need instance any object, just set fields
 *
 */
inline fun <T : LayoutComponent> T.setLayout(block: LayoutBuilder.() -> Unit): T {
    this.layout = LayoutBuilder(this.layout).apply(block).build()
    return this
}

/**
 * The style class will enable a few visual options to be changed.
 *
 * @property size add size to current view applying the flex.
 * @property margin
 *                  effects the spacing around the outside of a node.
 *                  A node with margin will offset itself from the bounds of its parent
 *                  but also offset the location of any siblings.
 *                  The margin of a node contributes to the total size of its parent if the parent is auto sized.
 * @property padding
 *                  affects the size of the node it is applied to.
 *                  Padding in Yoga acts as if box-sizing: border-box; was set.
 *                  That is padding will not add to the total size of an element if it has an explicit size set.
 *                  For auto sized nodes padding will increase the size of the
 *                  node as well as offset the location of any children..
 * @property position add padding to position.
 * @property flex
 * @see Flex
 * @property positionType The position type of an element defines how it is positioned within its parent.
 * @property visibility show or hide the layout.
 */
data class LayoutBuilder(private val layout: Layout?) {
    var size: Size = layout?.size ?: Size()
    var margin: EdgeValue? = layout?.margin
    var padding: EdgeValue? = layout?.padding
    var position: EdgeValue? = layout?.position
    var flex: Flex = layout?.flex ?: Flex()
    var positionType: PositionType? = layout?.positionType
    var visibility: Bind<Visibility>? = layout?.visibility

    private fun getLayout() = layout ?: Layout()

    fun build() = getLayout().copy(
        size = size,
        margin = margin,
        padding = padding,
        position = position,
        flex = flex,
        positionType = positionType,
        visibility = visibility,
    )

}
