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

package br.com.zup.beagle.android.components

import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.core.Display
import br.com.zup.beagle.core.PositionType
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.Size

fun column(children: List<ServerDrivenComponent>): Container {
    return styled(Container(children = children), {
        flex = Flex(
            grow = 1.0,
            alignItems = AlignItems.FLEX_START
        )
    })
}

fun center(child: ServerDrivenComponent): Container {
    return center(listOf(child))
}

fun center(children: List<ServerDrivenComponent>): Container {
    return styled(Container(children = children), {
        flex = Flex(
            justifyContent = JustifyContent.CENTER,
            alignContent = AlignContent.CENTER,
            alignSelf = AlignSelf.CENTER,
            grow = 1.0
        )
    })
}


fun <T : StyleComponent> styled(component: T, block: StyleBuilder.() -> Unit): T {
    component.style = StyleBuilder(component.style).apply(block).build()
    return component
}

data class StyleBuilder(private val style: Style?) {

    var backgroundColor: String? = style?.backgroundColor
    var cornerRadius: CornerRadius? = style?.cornerRadius
    var size: Size? = style?.size
    var margin: EdgeValue? = style?.margin
    var padding: EdgeValue? = style?.padding
    var position: EdgeValue? = style?.position
    var flex: Flex? = style?.flex
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