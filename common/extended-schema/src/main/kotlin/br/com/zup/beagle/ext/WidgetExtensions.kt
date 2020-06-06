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

import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.AccessibilityAutoDslBuilder
import br.com.zup.beagle.core.Appearance
import br.com.zup.beagle.core.AppearanceAutoDslBuilder
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexAutoDslBuilder

/**
 * Add an identifier to this widget.
 * @return the current widget
 */
fun <T : Widget> T.setId(id: String) = this.apply { this.id = id }

/**
 * Apply the layout component.
 *
 * @see Flex
 *
 * @return the current widget
 */
fun <T : Widget> T.applyFlex(flex: Flex) = this.apply { this.flex = flex }

/**
 * Apply the layout component.
 *
 * @see FlexAutoDslBuilder
 *
 * @return the current widget
 */
fun <T : Widget> T.flex(block: FlexAutoDslBuilder.() -> Unit) =
    this.applyFlex(FlexAutoDslBuilder().apply(block).build())


/**
 * Apply the appearance.
 *
 * @see Appearance
 *
 * @return the current widget
 */
fun <T : Widget> T.applyAppearance(appearance: Appearance) = this.apply { this.appearance = appearance }

fun <T : Widget> T.appearance(block: AppearanceAutoDslBuilder.() -> Unit) = this.apply {
    this.appearance = AppearanceAutoDslBuilder().apply(block).build()
}

/**
 * Apply the accessibility .
 *
 * @see Accessibility
 *
 * @return the current widget
 */
fun <T : Widget> T.applyAccessibility(accessibility: Accessibility) = this.apply { this.accessibility = accessibility }

fun <T : Widget> T.accessibility(block: AccessibilityAutoDslBuilder.() -> Unit) = this.apply {
    accessibility = AccessibilityAutoDslBuilder().apply(block).build()
}