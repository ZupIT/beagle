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

import br.com.zup.beagle.annotation.RegisterAction
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.ImagePath
import kotlin.reflect.KClass

internal fun getClass(
    clazz: KClass<*>,
    classLoader: ClassLoader
) = Class.forName(clazz.qualifiedName, false, classLoader)

internal fun getBeagleType(beanClass: Class<out Any>, classLoader: ClassLoader) =
    beanClass.simpleName?.decapitalize()?.let {
        when {
            isSubclass(beanClass, Action::class, classLoader) ->
                ACTION_TYPE to getBeagleTypeWithNamespace(
                    beanClass,
                    it,
                    getClass(RegisterAction::class, classLoader)
                )
            isSubclass(beanClass, Screen::class, classLoader) ->
                COMPONENT_TYPE to "$BEAGLE_NAMESPACE:$SCREEN_COMPONENT"
            isSubclass(beanClass, ServerDrivenComponent::class, classLoader) ->
                COMPONENT_TYPE to getBeagleTypeWithNamespace(
                    beanClass,
                    it,
                    getClass(RegisterWidget::class, classLoader)
                )
            isSubclass(beanClass, ImagePath::class, classLoader) ->
                IMAGE_PATH_TYPE to it
            else -> null
        }
    }

private fun isSubclass(
    beanClass: Class<out Any>,
    kClass: KClass<*>,
    classLoader: ClassLoader
) = getClass(kClass, classLoader).isAssignableFrom(beanClass)

private fun getBeagleTypeWithNamespace(beanClass: Class<out Any>, name: String, annotation: Class<*>) =
    if ((annotation as? Class<out Annotation>)?.let { beanClass.isAnnotationPresent(it) } == true)
        "$CUSTOM_BEAGLE_NAMESPACE:$name"
    else "$BEAGLE_NAMESPACE:$name"

