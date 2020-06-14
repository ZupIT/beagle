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

package br.com.zup.beagle.android.components.form

import android.view.View
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.ActionExecutor
import br.com.zup.beagle.android.components.form.observer.Observable
import br.com.zup.beagle.android.components.form.observer.Observer
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.widget.Widget

data class FormInput(
    val name: String,
    val required: Boolean? = null,
    val validator: String? = null,
    val errorMessage: String? = null,
    val child: InputWidget
) : WidgetView() {

    @Transient
    private val viewRendererFactory: ViewRendererFactory = ViewRendererFactory()

    @Transient
    private val actionExecutor: ActionExecutor = ActionExecutor()

    override fun buildView(rootView: RootView): View {
        return viewRendererFactory.make(child).build(rootView).apply {
            tag = this@FormInput
            val inputWidget: InputWidget = child
            inputWidget.getAction().addObserver(object : Observer<Pair<InputWidgetWatcherActionType, Any>> {
                override fun update(o: Observable<Pair<InputWidgetWatcherActionType, Any>>,
                                    arg: Pair<InputWidgetWatcherActionType, Any>) {
                    actionExecutor.doAction(rootView, getActions(inputWidget, arg.first))
                }
            })
        }
    }

    private fun getActions(inputWidget: InputWidget, type: InputWidgetWatcherActionType): List<Action>? {
        return when (type) {
            InputWidgetWatcherActionType.ON_CHANGE -> inputWidget.onChange
            InputWidgetWatcherActionType.ON_FOCUS -> inputWidget.onFocus
            InputWidgetWatcherActionType.ON_BLUR -> inputWidget.onBlur
        }
    }


    override fun onBind(widget: Widget, view: View) {}
}
