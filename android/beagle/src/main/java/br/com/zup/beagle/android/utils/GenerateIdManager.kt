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

package br.com.zup.beagle.android.utils

import android.view.View
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ListViewIdViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.MultiChildComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.SingleChildComponent
import br.com.zup.beagle.ext.setId
import br.com.zup.beagle.widget.Widget

internal const val COMPONENT_NO_ID = "-1"

internal class GenerateIdManager(
    private val rootView: RootView,
    private val generateIdViewModel: GenerateIdViewModel = rootView.generateViewModelInstance(),
    private val listViewIdViewModel: ListViewIdViewModel = rootView.generateViewModelInstance()
) {

    fun createSingleManagerByRootViewId() {
        generateIdViewModel.createIfNotExisting(rootView.getParentId())
    }

    fun onViewDetachedFromWindow(view: View) {
        generateIdViewModel.setViewCreated(rootView.getParentId())
        listViewIdViewModel.prepareToReuseIds(view)
    }

    fun manageId(component: ServerDrivenComponent, view: BeagleFlexView) {
        (component as? IdentifierComponent)?.let { identifierComponent ->
            if (identifierComponent.id.isNullOrEmpty()) {
                if (view.isAutoGenerateIdEnabled()) {
                    setComponentId(component)
                } else {
                    markEachNestedComponentAsNoIdIfNeeded(component)
                }
            }
        }
    }

    private fun setComponentId(component: ServerDrivenComponent) {
        val id = try {
            generateIdViewModel.getViewId(rootView.getParentId())
        } catch (exception: Exception) {
            View.generateViewId()
        }
        (component as? Widget)?.setId(id.toString())
    }

    private fun markEachNestedComponentAsNoIdIfNeeded(serverDrivenComponent: ServerDrivenComponent) {
        if (serverDrivenComponent is WidgetView) {
            setNoIdToComponentWithoutId(serverDrivenComponent)
        }
        if (serverDrivenComponent is SingleChildComponent) {
            markEachNestedComponentAsNoIdIfNeeded(serverDrivenComponent.child)
        } else if (serverDrivenComponent is MultiChildComponent) {
            serverDrivenComponent.children.forEach { childComponent ->
                markEachNestedComponentAsNoIdIfNeeded(childComponent)
            }
        }
    }

    private fun setNoIdToComponentWithoutId(component: WidgetView) {
        if (component.id.isNullOrEmpty()) {
            component.setId(COMPONENT_NO_ID)
        }
    }
}
