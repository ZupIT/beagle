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

package br.com.zup.beagle.view

import android.content.Context
import androidx.appcompat.widget.AppCompatButton
import br.com.zup.beagle.engine.renderer.ui.setData
import br.com.zup.beagle.interfaces.OnStateUpdatable
import br.com.zup.beagle.interfaces.StateChangeable
import br.com.zup.beagle.interfaces.WidgetState
import br.com.zup.beagle.state.Observable
import br.com.zup.beagle.widget.ui.Button as ButtonWidget

internal class BeagleButtonView(context: Context) : AppCompatButton(context),
    OnStateUpdatable<ButtonWidget>, StateChangeable {

    init {
        setOnClickListener {
            stateObservable.notifyObservers(WidgetState(Any()))
        }
    }

    private val stateObservable = Observable<WidgetState>()

    override fun getState(): Observable<WidgetState> = stateObservable

    override fun onUpdateState(widget: ButtonWidget) {
        this.setData(widget)
    }
}