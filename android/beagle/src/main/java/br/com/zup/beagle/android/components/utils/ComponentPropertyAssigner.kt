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
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.BeagleJson
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

    private fun getComponentType(component: ServerDrivenComponent) = createComponentType(component)

    private fun createComponentType(component: ServerDrivenComponent) =
        if (isCustomWidget(component)) "custom:" + getCustomWidgetName(component::class.java)
        else "beagle:" + getBeagleWidgetName(component::class.java)

    private fun isCustomWidget(component: ServerDrivenComponent) =
        BeagleEnvironment.beagleSdk.registeredWidgets().contains(component::class.java)

    //When use proguard, the widget name on Beagle is caught by the BeagleJson annotation
    private fun getBeagleWidgetName(clazz: Class<*>): String? {
        var name = clazz.getAnnotation(BeagleJson::class.java)?.name
        if (name.isNullOrEmpty()) {
            name = clazz.simpleName
        }
        return name?.putFirstCharacterOnLowerCase()
    }

    //When use proguard, the widget name on Beagle is caught by the RegisterWidget annotation
    private fun getCustomWidgetName(clazz: Class<*>): String? {
        var name = clazz.getAnnotation(RegisterWidget::class.java)?.name
        if (name.isNullOrEmpty()) {
            name = clazz.simpleName
        }
        return name?.putFirstCharacterOnLowerCase()
    }
}
