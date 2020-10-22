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

package br.com.zup.beagle.builder.core

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.widget.EdgeValueBuilder
import br.com.zup.beagle.builder.widget.FlexBuilder
import br.com.zup.beagle.builder.widget.SizeBuilder
import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.core.Display
import br.com.zup.beagle.core.PositionType
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size

fun style(block: StyleBuilder.() -> Unit) = StyleBuilder()
    .apply(block).build()

class StyleBuilder: BeagleBuilder<Style> {
    var backgroundColor: String? = null
    var cornerRadius: CornerRadius? = null
    var size: Size? = null
    var margin: EdgeValue? = null
    var padding: EdgeValue? = null
    var position: EdgeValue? = null
    var flex: Flex? = null
    var positionType: PositionType? = null
    var display: Display? = null
    var borderColor: String? = null
    var borderWidth: Double? = null

    fun backgroundColor(backgroundColor: String?) = this.apply { this.backgroundColor = backgroundColor }
    fun cornerRadius(cornerRadius: CornerRadius?) = this.apply { this.cornerRadius = cornerRadius }
    fun size(size: Size?) = this.apply { this.size = size }
    fun margin(margin: EdgeValue?) = this.apply { this.margin = margin }
    fun padding(padding: EdgeValue?) = this.apply { this.padding = padding }
    fun position(position: EdgeValue?) = this.apply { this.position = position }
    fun flex(flex: Flex?) = this.apply { this.flex = flex }
    fun positionType(positionType: PositionType?) = this.apply { this.positionType = positionType }
    fun display(display: Display?) = this.apply { this.display = display }
    fun borderColor(borderColor: String?) = this.apply { this.borderColor = borderColor }
    fun borderWidth(borderWidth: Double?) = this.apply { this.borderWidth = borderWidth }

    fun backgroundColor(block: () -> String?) {
        backgroundColor(block.invoke())
    }

    fun cornerRadius(block: CornerRadiusBuilder.() -> Unit) {
        cornerRadius(CornerRadiusBuilder().apply(block).build())
    }

    fun size(block: SizeBuilder.() -> Unit) {
        size(SizeBuilder().apply(block).build())
    }

    fun margin(block: EdgeValueBuilder.() -> Unit) {
        margin(EdgeValueBuilder().apply(block).build())
    }

    fun padding(block: EdgeValueBuilder.() -> Unit) {
        padding(EdgeValueBuilder().apply(block).build())
    }

    fun position(block: EdgeValueBuilder.() -> Unit) {
        position(EdgeValueBuilder().apply(block).build())
    }

    fun flex(block: FlexBuilder.() -> Unit) {
        flex(FlexBuilder().apply(block).build())
    }

    fun positionType(block: () -> PositionType?) {
        positionType(block.invoke())
    }

    fun display(block: () -> Display?) {
        display(block.invoke())
    }

    fun borderColor(block: () -> String?){
        borderColor(block.invoke())
    }

    fun borderWidth(block: () -> Double?){
        borderWidth(block.invoke())
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
        display = display,
        borderColor = borderColor,
        borderWidth = borderWidth
    )
}

fun cornerRadius(block: CornerRadiusBuilder.() -> Unit) = CornerRadiusBuilder().apply(block).build()

class CornerRadiusBuilder: BeagleBuilder<CornerRadius> {
    var radius: Double = 0.0

    fun radius(radius: Double) = this.apply { this.radius = radius }

    fun radius(block: () -> Double) {
        radius(block.invoke())
    }

    override fun build() = CornerRadius(radius)
}