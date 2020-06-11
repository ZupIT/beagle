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

package br.com.zup.beagle.android.widget.form

import br.com.zup.beagle.android.widget.core.Bindable
import br.com.zup.beagle.android.widget.core.ViewConvertable
import br.com.zup.beagle.android.widget.interfaces.StateChangeable
import br.com.zup.beagle.android.widget.interfaces.WidgetState
import br.com.zup.beagle.android.widget.state.Observable
import br.com.zup.beagle.widget.form.InputWidget

abstract class InputWidget : InputWidget(), Bindable, ViewConvertable, StateChangeable, InputWidgetWatcher {

    @Transient
    private val stateObservable =
        Observable<WidgetState>()

    @Transient
    private var actionObservable = Observable<Pair<InputWidgetWatcherActionType, Any>>()

    abstract fun getValue(): Any

    abstract fun onErrorMessage(message: String)

    override fun getState(): Observable<WidgetState> = stateObservable

    override fun getAction(): Observable<Pair<InputWidgetWatcherActionType, Any>> = actionObservable

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

    private fun getActionTypeWithValue(type: InputWidgetWatcherActionType):
        Pair<InputWidgetWatcherActionType, Any> = type to getValue()
}