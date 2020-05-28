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

package br.com.zup.beagle.serialization.jackson

import br.com.zup.beagle.action.Action
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.ImagePath

internal fun getBeagleType(beanClass: Class<out Any>) = beanClass.simpleName?.decapitalize()?.let {
    when {
        Action::class.java.isAssignableFrom(beanClass) -> ACTION_TYPE to "$BEAGLE_NAMESPACE:$it"
        Screen::class.java.isAssignableFrom(beanClass) -> COMPONENT_TYPE to "$BEAGLE_NAMESPACE:$SCREEN_COMPONENT"
        ServerDrivenComponent::class.java.isAssignableFrom(beanClass) ->
            COMPONENT_TYPE to getBeagleTypeWithNamespace(beanClass, it)
        ImagePath::class.java.isAssignableFrom(beanClass) -> IMAGE_PATH_TYPE to it
        else -> null
    }
}

private fun getBeagleTypeWithNamespace(beanClass: Class<out Any>, name: String) =
    if (beanClass.getAnnotation(RegisterWidget::class.java) != null) "$CUSTOM_WIDGET_BEAGLE_NAMESPACE:$name"
    else "$BEAGLE_NAMESPACE:$name"