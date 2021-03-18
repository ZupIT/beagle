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

import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.core.Display
import br.com.zup.beagle.core.PositionType
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size

/**
 *  The Styled is a helper to apply style in your component
 *
 * @param self the component will apply style
 *
 */
@Suppress("FunctionNaming")
inline fun <T : StyleComponent> Styled(self: T, block: StyleBuilder.() -> Unit): T {
    self.style = StyleBuilder(self.style).apply(block).build()
    return self
}

/**
 *  The StyleBuilder it is a helper to set style options in your component.
 *  with this method you don't need instance any object, just set fields
 *
 */
inline fun <T : StyleComponent> T.setStyle(block: StyleBuilder.() -> Unit): T {
    this.style = StyleBuilder(this.style).apply(block).build()
    return this
}

/**
 * The style class will enable a few visual options to be changed.
 *
 * @property backgroundColor
 *                          Using a String parameter it sets the background color on this visual component.
 *                          It must be listed as an Hexadecimal color format without the "#".
 *                          For example, for a WHITE background type in "FFFFFF".
 * @property cornerRadius Using a Double parameters it sets the corner of your view to make it round.
 * @property borderColor Sets the color of your view border. Supported formats:#RRGGBB[AA] and #RGB[A].
 * @property borderWidth Sets the width of your view border.
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
 * @property display enables a flex context for all its direct children.
 */
data class StyleBuilder(private val style: Style?) {
    var backgroundColor: String? = style?.backgroundColor
    var cornerRadius: CornerRadius? = style?.cornerRadius
    var size: Size? = style?.size ?: Size()
    var margin: EdgeValue? = style?.margin
    var padding: EdgeValue? = style?.padding
    var position: EdgeValue? = style?.position
    var flex: Flex = style?.flex ?: Flex()
    var positionType: PositionType? = style?.positionType
    var display: Bind<Display>? = style?.display
    var borderColor: String? = style?.borderColor
    var borderWidth: Double? = style?.borderWidth

    private fun getStyle() = style ?: Style()

    fun build() = getStyle().copy(
        backgroundColor = backgroundColor,
        cornerRadius = cornerRadius,
        borderColor = borderColor,
        borderWidth = borderWidth,
        size = size,
        margin = margin,
        padding = padding,
        position = position,
        flex = flex,
        positionType = positionType,
        display = display,
    )

}
