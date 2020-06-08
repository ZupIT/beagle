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
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ViewConvertable
import br.com.zup.beagle.widget.interfaces.StateChangeable
import br.com.zup.beagle.widget.interfaces.WidgetState
import br.com.zup.beagle.widget.state.Observable

abstract class InputWidgetView : InputWidget(), ViewConvertable, StateChangeable {

    @Transient
    private val stateObservable = Observable<WidgetState>()

    abstract fun getValue(): Any

    abstract fun onErrorMessage(message: String)

    override fun getState(): Observable<WidgetState> = stateObservable

    fun notifyChanges() {
        stateObservable.notifyObservers(WidgetState(getValue()))
    }
}