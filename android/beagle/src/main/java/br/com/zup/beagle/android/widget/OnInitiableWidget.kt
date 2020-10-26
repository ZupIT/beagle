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

package br.com.zup.beagle.android.widget

import android.view.View
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.OnInitiableComponent
import br.com.zup.beagle.android.context.ContextActionExecutor

/**
 * Abstract class that has onInit property
 * @property onInit list of actions performed as soon as the component is rendered
 */
class OnInitiableWidget(override val onInit: List<Action>?) : OnInitiableComponent {

    @Transient
    private var onInitCalled = false

    override fun handleOnInit(rootView: RootView, origin: View) {
        onInit?.let {
            addListenerToExecuteOnInit(rootView, origin)
        }
    }

    private fun addListenerToExecuteOnInit(rootView: RootView, origin: View) {
        origin.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
                if (!onInitCalled) {
                    ContextActionExecutor.executeActions(rootView, origin, onInit)
                    onInitCalled = true
                }
            }

            override fun onViewDetachedFromWindow(v: View?) {}
        })
    }

    /**
     * Method responsible for releasing the execution of all actions present in the onInit property
     * regardless of whether they have already been executed.
     */
    override fun markToRerunOnInit() {
        onInitCalled = false
    }
}
