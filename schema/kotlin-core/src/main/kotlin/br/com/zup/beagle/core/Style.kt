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

package br.com.zup.beagle.core

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.core.Display.FLEX
import br.com.zup.beagle.core.Display.NONE
import br.com.zup.beagle.core.PositionType.ABSOLUTE
import br.com.zup.beagle.core.PositionType.RELATIVE
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size

/**
 * The style class will enable a few visual options to be changed.
 *
 * @param backgroundColor
 *                          Using a String parameter it sets the background color on this visual component.
 *                          It must be listed as an Hexadecimal color format without the "#".
 *                          For example, for a WHITE background type in "FFFFFF".
 * @param cornerRadius Using a Double parameters it sets the corner of your view to make it round.
 * @param size add size to current view applying the flex.
 * @param margin
 *                  effects the spacing around the outside of a node.
 *                  A node with margin will offset itself from the bounds of its parent
 *                  but also offset the location of any siblings.
 *                  The margin of a node contributes to the total size of its parent if the parent is auto sized.
 * @param padding
 *                  affects the size of the node it is applied to.
 *                  Padding in Yoga acts as if box-sizing: border-box; was set.
 *                  That is padding will not add to the total size of an element if it has an explicit size set.
 *                  For auto sized nodes padding will increase the size of the
 *                  node as well as offset the location of any children..
 * @param position add padding to position.
 * @param flex
 * @see Flex
 * @param positionType The position type of an element defines how it is positioned within its parent.
 * @param display enables a flex context for all its direct children.
 */
data class Style (
    val backgroundColor: String? = null,
    val cornerRadius: CornerRadius? = null,
    val size: Size? = null,
    val margin: EdgeValue? = null,
    val padding: EdgeValue? = null,
    val position: EdgeValue? = null,
    val flex: Flex? = null,
    val positionType: PositionType? = null,
    val display: Display? = null
) {
    companion object{
        @JvmStatic
        fun builder() = Builder()
    }
    @Suppress("TooManyFunctions")
    class Builder : BeagleBuilder<Style> {
        var backgroundColor: String? = null
        var cornerRadius: CornerRadius? = null
        var size: Size? = null
        var margin: EdgeValue? = null
        var padding: EdgeValue? = null
        var position: EdgeValue? = null
        var flex: Flex? = null
        var positionType: PositionType? = null
        var display: Display? = null

        fun backgroundColor(backgroundColor: String?) = this.apply { this.backgroundColor = backgroundColor }
        fun cornerRadius(cornerRadius: CornerRadius?) = this.apply { this.cornerRadius = cornerRadius }
        fun size(size: Size?) = this.apply { this.size = size }
        fun margin(margin: EdgeValue?) = this.apply { this.margin = margin }
        fun padding(padding: EdgeValue?) = this.apply { this.padding = padding }
        fun position(position: EdgeValue?) = this.apply { this.position = position }
        fun flex(flex: Flex?) = this.apply { this.flex = flex }
        fun positionType(positionType: PositionType?) = this.apply { this.positionType = positionType }
        fun display(display: Display?) = this.apply { this.display = display }

        fun backgroundColor(block: () -> String?) {
            backgroundColor(block.invoke())
        }

        fun cornerRadius(block: CornerRadius.Builder.() -> Unit) {
            cornerRadius(CornerRadius.Builder().apply(block).build())
        }

        fun size(block: Size.Builder.() -> Unit) {
            size(Size.Builder().apply(block).build())
        }

        fun margin(block: EdgeValue.Builder.() -> Unit) {
            margin(EdgeValue.Builder().apply(block).build())
        }

        fun padding(block: EdgeValue.Builder.() -> Unit) {
            padding(EdgeValue.Builder().apply(block).build())
        }

        fun position(block: EdgeValue.Builder.() -> Unit) {
            position(EdgeValue.Builder().apply(block).build())
        }

        fun flex(block: Flex.Builder.() -> Unit) {
            flex(Flex.Builder().apply(block).build())
        }

        fun positionType(block: () -> PositionType?) {
            positionType(block.invoke())
        }

        fun display(block: () -> Display?) {
            display(block.invoke())
        }

        override fun build() = Style(
            backgroundColor = backgroundColor,
            cornerRadius = cornerRadius,
            size = size,
            margin = margin,
            padding = padding,
            position = position,
            flex = flex,
            positionType = positionType,
            display = display
        )
    }
}

/**
 * The corner radius change the appearance of view
 *
 * @param radius define size of radius
 */
data class CornerRadius(
    val radius: Double = 0.0
){
    companion object{
        @JvmStatic
        fun builder() = Builder()
    }
    class Builder: BeagleBuilder<CornerRadius> {
        var radius: Double = 0.0

        fun radius(radius: Double) = this.apply { this.radius = radius }

        fun radius(block: () -> Double) {
            radius(block.invoke())
        }

        override fun build() = CornerRadius(radius)
    }
}

/**
 * This defines a flex container;
 * inline or block depending on the given value. It enables a flex context for all its direct children.
 *
 * @property FLEX
 * @property NONE
 */
enum class Display {
    /**
     * Apply the flex properties.
     */
    FLEX,

    /**
     * No flex properties will be applied to the element.
     */
    NONE
}

/**
 * The position type of an element defines how it is positioned within its parent.
 *
 * @property ABSOLUTE
 * @property RELATIVE
 */
enum class PositionType {

    /**
     * This means an element is positioned according to the normal flow of the layout,
     * and then offset relative to that position based on the values of top, right, bottom, and left.
     * The offset does not affect the position of any sibling or parent elements.
     */
    ABSOLUTE,

    /**
     * When positioned absolutely an element doesn't take part in the normal layout flow.
     * It is instead laid out independent of its siblings.
     * The position is determined based on the top, right, bottom, and left values.
     */
    RELATIVE
}
