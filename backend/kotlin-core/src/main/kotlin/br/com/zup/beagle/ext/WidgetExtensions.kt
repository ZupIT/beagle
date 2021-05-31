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

import br.com.zup.beagle.builder.core.AccessibilityBuilder
import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.builder.core.StyleBuilder as OldStyle

/**
 * Add an identifier to this widget.
 * @return the current widget
 */
fun <T : Widget> T.setId(id: String) = this.apply { this.id = id }
fun <T : Widget> T.id(block: () -> String) = this.setId(block.invoke())

/**
 * Apply the layout component.
 *
 * @see Flexible
 *
 * @return the current widget
 */

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version.",
    ReplaceWith("setFlex { }"))
fun <T : Widget> T.applyFlex(flex: Flex) = this.apply { this.style = (this.style ?: Style()).copy(flex = flex) }

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version.",
    ReplaceWith("setFlex { }"))
fun <T : StyleComponent> T.flex(block: Flex.() -> Unit): T = setFlex(block)

/**
 * Apply the appearance.
 *
 * @see Style
 *
 * @return the current widget
 */
@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version.",
    ReplaceWith("Styled(component, { })"))
fun <T : Widget> T.applyStyle(style: Style) = this.apply {
    this.style = style.copy(
        flex = Flex(
            flexDirection = style.flex.flexDirection ?: this.style?.flex?.flexDirection,
            flexWrap = style.flex.flexWrap ?: this.style?.flex?.flexWrap ,
            justifyContent = style.flex.justifyContent ?: this.style?.flex?.justifyContent,
            alignItems = style.flex.alignItems ?: this.style?.flex?.alignItems,
            alignSelf = style.flex.alignSelf ?: this.style?.flex?.alignSelf,
            alignContent = style.flex.alignContent ?: this.style?.flex?.alignContent,
            basis = style.flex.basis ?: this.style?.flex?.basis,
            flex = style.flex.flex ?: this.style?.flex?.flex,
            grow = style.flex.grow ?: this.style?.flex?.grow,
            shrink = style.flex.shrink ?: this.style?.flex?.shrink
        )
    )
}

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version.",
    ReplaceWith("setStyle { }"))
fun <T : Widget> T.style(block: OldStyle.() -> Unit) = this.applyStyle(OldStyle().apply(block).build())

/**
 * Apply the accessibility .
 *
 * @see Accessibility
 *
 * @return the current widget
 */

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version.",
    ReplaceWith("setAccessibility { }"))
fun <T : Widget> T.applyAccessibility(accessibility: Accessibility) = this.apply { this.accessibility = accessibility }

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version.",
    ReplaceWith("setAccessibility { }"))
fun <T : Widget> T.accessibility(block: AccessibilityBuilder.() -> Unit) =
    this.applyAccessibility(AccessibilityBuilder().apply(block).build())
