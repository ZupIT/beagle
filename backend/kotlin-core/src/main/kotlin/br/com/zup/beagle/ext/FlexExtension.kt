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

/**
 *  The Flex is a helper to apply Flex in your component
 *
 * @param self the component will apply flex
 *
 */
@Suppress("FunctionNaming")
fun <T : StyleComponent> Flex(self: T, block: FlexBuilder.() -> Unit): T {
    return self.setFlex(block)
}

/**
 *  The FlexBuilder it is a helper to set flex options in your component.
 *  with this method you don't need instance any object, just set fields
 *
 */
fun <T : StyleComponent> T.setFlex(block: FlexBuilder.() -> Unit): T {
    style = StyleBuilder(style).apply {
        flex = FlexBuilder(style?.flex)
            .apply(block)
            .build()
    }.build()

    return this
}

/**
 *
 * The flex is a Layout component that will handle your visual component positioning at the screen.
 * Internally Beagle uses a Layout engine called Yoga Layout to position elements on screen.
 * In fact it will use the HTML Flexbox properties applied on the visual components and its children.
 *
 * @property flexDirection
 *                          controls the direction in which the children of a node are laid out.
 *                          This is also referred to as the main axis
 * @property flexWrap
 *                  set on containers and controls what happens when children
 *                  overflow the size of the container along the main axis.
 * @property justifyContent align children within the main axis of their container.
 * @property alignItems Align items describes how to align children along the cross axis of their container.
 * @property alignSelf
 *                      Align self has the same options and effect as align items
 *                      but instead of affecting the children within a container.
 * @property alignContent Align content defines the distribution of lines along the cross-axis..
 * @property basis is an axis-independent way of providing the default size of an item along the main axis.
 * @property grow describes how any space within a container should be distributed among
 *                  its children along the main axis.
 * @property shrink
 *              describes how to shrink children along the main axis in the case that
 *              the total size of the children overflow the size of the container on the main axis.
 *
 */
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
