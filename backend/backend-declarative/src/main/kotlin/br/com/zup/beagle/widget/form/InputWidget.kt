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

package br.com.zup.beagle.widget.form

import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.Appearance
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.core.Action
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexBuilder

/**
 * <p>It could be an EditText view in Android, a Radio button in HTML,
 * an UITextField in iOS or any other type of view that can receive and store input from users. </p>
 *
 */
abstract class InputWidget : Widget() {

    var onChange: List<Action>? = null
        private set
    var onFocus: List<Action>? = null
        private set
    var onBlur: List<Action>? = null
        private set

    /**
     * Update list of actions to on change to this widget.
     * @return the current widget
     */
    fun setOnChange(actions: List<Action>): InputWidget {
        this.onChange = actions
        return this
    }

    /**
     * Update list of actions to on focus to this widget.
     * @return the current widget
     */
    fun setOnFocus(actions: List<Action>): InputWidget {
        this.onFocus = actions
        return this
    }

    /**
     * Update list of actions to on focus to this widget.
     * @return the current widget
     */
    fun setOnBlur(actions: List<Action>): InputWidget {
        this.onBlur = actions
        return this
    }

    /**
     * Add an identifier to this widget.
     * @return the current widget
     */
    override fun setId(id: String): InputWidget {
        return super.setId(id) as InputWidget
    }

    /**
     * Apply the appearance.
     * @see Appearance
     * @return the current widget
     */
    override fun applyAppearance(appearance: Appearance): InputWidget {
        return super.applyAppearance(appearance) as InputWidget
    }

    /**
     * Apply the layout component.
     * @see Flex
     * @return the current widget
     */
    override fun applyFlex(flex: Flex): InputWidget {
        return super.applyFlex(flex) as InputWidget
    }

    fun flex(block: FlexBuilder.() -> Unit) = applyFlex(FlexBuilder().apply(block).build())

    /**
     * Apply the accessibility .
     * @see Accessibility
     * @return the current widget
     */
    override fun applyAccessibility(accessibility: Accessibility): InputWidget {
        return super.applyAccessibility(accessibility) as InputWidget
    }
}