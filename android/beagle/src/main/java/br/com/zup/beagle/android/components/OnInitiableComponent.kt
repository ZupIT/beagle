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

package br.com.zup.beagle.android.components

import android.view.View
import androidx.lifecycle.Observer
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.AsyncAction
import br.com.zup.beagle.android.action.AsyncActionStatus
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.viewmodel.OnInitViewModel
import br.com.zup.beagle.android.widget.RootView

/**
 * Class that has onInit property
 * @property onInit list of actions performed as soon as the component is rendered
 */
interface OnInitiableComponent {

    val onInit: List<Action>?

    /**
     * Method responsible for executing all actions present in the onInit property once the component is rendered.
     * It is recommended to call this method within the buildView.
     * @property rootView from buildView
     * @property origin view that triggered the action
     */
    fun handleOnInit(rootView: RootView, origin: View)

    /**
     * Method responsible for releasing the execution of all actions present in the onInit property
     * regardless of whether they have already been executed.
     * It is rarely appropriate to use this method.
     */
    fun markToRerunOnInit()
}

/**
 * Class that implements onInitiableComponent behavior
 * @property onInitViewModel manages the onInit called status
 * @property origin represents the view that triggered the action
 * @property observer listens to the FINISHED status of the async actions present on onInit informing the ViewModel
 */
class OnInitiableComponentImpl(override val onInit: List<Action>?) : OnInitiableComponent {

    @Transient
    private lateinit var onInitViewModel: OnInitViewModel

    @Transient
    private lateinit var origin: View

    @Transient
    private val observer = Observer<AsyncActionStatus> { actionStatus ->
        if (actionStatus == AsyncActionStatus.FINISHED) {
            onInitViewModel.setOnInitFinished(origin.id, true)
        }
    }

    /**
     * Execute the actions present in the onInit property as soon as the component is attached to window.
     * Call this method preferably from the component's buildView method.
     */
    override fun handleOnInit(rootView: RootView, origin: View) {
        onInitViewModel = rootView.generateViewModelInstance()
        this.origin = origin
        onInit?.let {
            addListenerToExecuteOnInit(rootView)
        }
    }

    private fun addListenerToExecuteOnInit(rootView: RootView) {
        origin.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
                if (!onInitViewModel.isOnInitCalled(origin.id)) {
                    onInit?.forEach { action ->
                        (action as? AsyncAction)?.status?.observe(rootView.getLifecycleOwner(), observer)
                        action.handleEvent(rootView, origin, action)
                    }
                    onInitViewModel.setOnInitCalled(origin.id, true)
                }
            }

            override fun onViewDetachedFromWindow(v: View?) {}
        })
    }

    /**
     * Method responsible for marking the onInitCalled status of all actions in a view as false.
     * It is rarely appropriate to use this method.
     */
    override fun markToRerunOnInit() {
        onInitViewModel.setOnInitCalled(origin.id, false)
    }
}
