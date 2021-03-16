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

import br.com.zup.beagle.core.StyleComponent
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.FlexWrap
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.UnitValue

fun <T : StyleComponent> T.setFlex(block: FlexBuilder.() -> Unit): T {
    style = StyleBuilder(style).apply {
        flex = FlexBuilder(style?.flex)
            .apply(block)
            .build()
    }.build()

    return this
}

class FlexBuilder(private val flexObject: Flex?) {
    var flexDirection: FlexDirection? = flexObject?.flexDirection
    var flexWrap: FlexWrap? = flexObject?.flexWrap
    var justifyContent: JustifyContent? = flexObject?.justifyContent
    var alignItems: AlignItems? = flexObject?.alignItems
    var alignSelf: AlignSelf? = flexObject?.alignSelf
    var alignContent: AlignContent? = flexObject?.alignContent
    var basis: UnitValue? = flexObject?.basis
    var flex: Double? = flexObject?.flex
    var grow: Double? = flexObject?.grow
    var shrink: Double? = flexObject?.shrink

    private fun getFlex() = flexObject ?: Flex()

    fun build(): Flex = getFlex().copy(
        flexDirection = flexDirection,
        flexWrap = flexWrap,
        justifyContent = justifyContent,
        alignItems = alignItems,
        alignSelf = alignSelf,
        alignContent = alignContent,
        basis = basis,
        flex = flex,
        grow = grow,
        shrink = shrink
    )
}