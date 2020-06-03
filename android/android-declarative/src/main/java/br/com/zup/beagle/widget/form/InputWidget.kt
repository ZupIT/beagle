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
import br.com.zup.beagle.widget.core.Action
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.WidgetView
import br.com.zup.beagle.widget.interfaces.StateChangeable
import br.com.zup.beagle.widget.interfaces.WidgetState
import br.com.zup.beagle.widget.state.Observable

@Suppress("TooManyFunctions")
abstract class InputWidget : WidgetView(), StateChangeable, InputWidgetWatcher {

    var onChange: List<Action>? = null
        private set
    var onFocus: List<Action>? = null
        private set
    var onBlur: List<Action>? = null
        private set

    @Transient
    private val stateObservable = Observable<WidgetState>()

    @Transient
    private var actionObservable = Observable<Pair<InputWidgetWatcherActionType, Any>>()

    abstract fun getValue(): Any

    abstract fun onErrorMessage(message: String)

    override fun getState(): Observable<WidgetState> = stateObservable

    override fun getAction(): Observable<Pair<InputWidgetWatcherActionType, Any>> = actionObservable

    override fun setId(id: String): InputWidget {
        return super.setId(id) as InputWidget
    }

    override fun applyAppearance(appearance: Appearance): InputWidget {
        return super.applyAppearance(appearance) as InputWidget
    }

    override fun applyFlex(flex: Flex): InputWidget {
        return super.applyFlex(flex) as InputWidget
    }

    override fun applyAccessibility(accessibility: Accessibility): InputWidget {
        return super.applyAccessibility(accessibility) as InputWidget
    }

    /**
     * call this action when the field lost focus.
     */
    override fun onBlur() {
        actionObservable.notifyObservers(getActionTypeWithValue(InputWidgetWatcherActionType.ON_BLUR))
    }

    /**
     * call this action when the field change value.
     */
    override fun onChange() {
        actionObservable.notifyObservers(getActionTypeWithValue(InputWidgetWatcherActionType.ON_CHANGE))
    }

    /**
     * call this action when the field focused.
     */
    override fun onFocus() {
        actionObservable.notifyObservers(getActionTypeWithValue(InputWidgetWatcherActionType.ON_FOCUS))
    }

    /**
     * Notify the view the value updated
     *
     */
    fun notifyChanges() {
        onChange()
        stateObservable.notifyObservers(WidgetState(getValue()))
    }

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

    private fun getActionTypeWithValue(type: InputWidgetWatcherActionType):
        Pair<InputWidgetWatcherActionType, Any> = type to getValue()
}