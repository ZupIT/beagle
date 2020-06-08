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

package br.com.zup.beagle.engine.renderer.layout

import android.view.View
import br.com.zup.beagle.action.ActionExecutor
import br.com.zup.beagle.engine.renderer.LayoutViewRenderer
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.Action
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.InputWidget
import br.com.zup.beagle.widget.form.InputWidgetWatcherActionType
import br.com.zup.beagle.widget.interfaces.Observer
import br.com.zup.beagle.widget.state.Observable


internal class FormInputViewRenderer(
    override val component: FormInput,
    viewRendererFactory: ViewRendererFactory = ViewRendererFactory(),
    viewFactory: ViewFactory = ViewFactory(),
    private val actionExecutor: ActionExecutor = ActionExecutor()
) : LayoutViewRenderer<FormInput>(viewRendererFactory, viewFactory) {

    override fun buildView(rootView: RootView): View {
        return viewRendererFactory.make(component.child).build(rootView).apply {
            tag = component
            val inputWidget: InputWidget = component.child
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
}