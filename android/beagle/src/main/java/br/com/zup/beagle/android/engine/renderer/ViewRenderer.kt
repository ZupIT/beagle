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

package br.com.zup.beagle.android.engine.renderer

import android.view.View
import br.com.zup.beagle.android.components.utils.ComponentStylization
import br.com.zup.beagle.android.context.ContextComponentHandler
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.ViewConvertable
import br.com.zup.beagle.core.ServerDrivenComponent

internal abstract class ViewRenderer<T : ServerDrivenComponent>(
    private val componentStylization: ComponentStylization<T> = ComponentStylization(),
    private val contextComponentHandler: ContextComponentHandler = ContextComponentHandler()
) {
    abstract val component: T

    fun build(rootView: RootView): View {
        val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
        val builtView = buildView(rootView)
        componentStylization.apply(builtView, component)
        if (builtView.id == View.NO_ID) {
            val generateIdViewModel = rootView.generateViewModelInstance<GenerateIdViewModel>()
            builtView.id = try {
                generateIdViewModel.getViewId(rootView.getParentId())
            } catch (exception: Exception) {
                View.generateViewId()
            }
        }
        contextComponentHandler.addContext(viewModel, builtView, component)
        contextComponentHandler.addListenerToHandleContext(viewModel, builtView)
        return builtView
    }

    abstract fun buildView(rootView: RootView): View
}

internal class ViewRendererFactory {

    fun make(component: ServerDrivenComponent): ViewRenderer<*> {
        return ViewConvertableRenderer(component as ViewConvertable)
    }
}
