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

package br.com.zup.beagle.widget

import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.AccessibilityComponent
import br.com.zup.beagle.core.Appearance
import br.com.zup.beagle.core.AppearanceComponent
import br.com.zup.beagle.core.FlexComponent
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexBuilder

/**
 * Base of all widgets
 *
 */
abstract class Widget : FlexComponent, AppearanceComponent, AccessibilityComponent,
    IdentifierComponent {

    final override var id: String? = null
        private set
    final override var flex: Flex? = null
        private set
    final override var appearance: Appearance? = null
        private set
    final override var accessibility: Accessibility? = null
        private set

    /**
     * Add an identifier to this widget.
     * @return the current widget
     */
    open fun setId(id: String): Widget {
        this.id = id
        return this
    }

    /**
     * Apply the layout component.
     *
     * @see Flex
     *
     * @return the current widget
     */
    open fun applyFlex(flex: Flex): Widget {
        this.flex = flex
        return this
    }


    /**
     * Apply the layout component.
     *
     * @see FlexBuilder
     *
     * @return the current widget
     */
    fun buildAndApplyFlex(flexBuilder: FlexBuilder) = this.applyFlex(flexBuilder.build())

    /**
     * Apply the appearance.
     *
     * @see Appearance
     *
     * @return the current widget
     */
    open fun applyAppearance(appearance: Appearance): Widget {
        this.appearance = appearance
        return this
    }

    /**
     * Apply the accessibility .
     *
     * @see Accessibility
     *
     * @return the current widget
     */
    open fun applyAccessibility(accessibility: Accessibility): Widget {
        this.accessibility = accessibility
        return this
    }
}