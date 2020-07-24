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

package br.com.zup.beagle.android.handler

import android.view.View
import br.com.zup.beagle.android.utils.AccessibilitySetup
import br.com.zup.beagle.android.utils.applyStyle
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent

class ComponentStylization<T : ServerDrivenComponent>(
    private val accessibilitySetup: AccessibilitySetup = AccessibilitySetup()
) {
    fun apply(view: View, component: T) {
        view.applyStyle(component)
        (component as? IdentifierComponent)?.id?.let {
            view.id = it.toAndroidId()
        }
        accessibilitySetup.applyAccessibility(view, component)
    }
}
