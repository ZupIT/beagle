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
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.core.Flex

/**
 * Add an identifier to this widget.
 * @return the current widget
 */
fun <T : Widget> T.setId(id: String) = this.apply { this.id = id }

fun <T : Widget> T.id(block: () -> String) = this.setId(block.invoke())

/**
 * Apply the layout component.
 *
 * @see Flex
 *
 * @return the current widget
 */
fun <T : Widget> T.applyFlex(flex: Flex) = this.apply { this.style = (this.style ?: Style()).copy(flex = flex) }

/**
 * Apply the appearance.
 *
 * @see Style
 *
 * @return the current widget
 */
fun <T : Widget> T.applyStyle(style: Style) = this.apply {
    this.style = if (style.flex != null) style else style.copy(flex = this.style?.flex)
}

/**
 * Apply the accessibility .
 *
 * @see Accessibility
 *
 * @return the current widget
 */
fun <T : Widget> T.applyAccessibility(accessibility: Accessibility) = this.apply { this.accessibility = accessibility }
