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

package br.com.zup.beagle.android.components.utils

import android.view.View
import br.com.zup.beagle.R
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.putFirstCharacterOnLowerCase
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent

class ComponentPropertyAssigner<T : ServerDrivenComponent>(
    private val accessibilitySetup: AccessibilitySetup = AccessibilitySetup()
) {
    fun apply(view: View, component: T) {
        view.applyStyle(component)
        (component as? IdentifierComponent)?.id?.let {
            view.id = it.toAndroidId()
            view.setTag(R.id.beagle_component_id, it)
        }
        view.setTag(R.id.beagle_component_type, getComponentType(component))
        accessibilitySetup.applyAccessibility(view, component)
    }

    private fun getComponentType(component: ServerDrivenComponent): String {
        return createComponentType(component)
    }

    private fun createComponentType(component: ServerDrivenComponent): String =
        if (isCustomWidget(component)) "custom:" + component::class.simpleName?.putFirstCharacterOnLowerCase()
        else "beagle:" + component::class.simpleName?.putFirstCharacterOnLowerCase()

    private fun isCustomWidget(component: ServerDrivenComponent): Boolean =
        BeagleEnvironment.beagleSdk.registeredWidgets().contains(component::class.java)
}
