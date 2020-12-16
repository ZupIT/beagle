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

package br.com.zup.beagle.android.data.serializer.adapter

import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.data.serializer.PolymorphicJsonAdapterFactory
import br.com.zup.beagle.android.setup.BeagleEnvironment
import java.util.Locale

private const val BEAGLE_WIDGET_TYPE = "_beagleAction_"
private const val NAMESPACE = "custom"

internal object AndroidActionJsonAdapterFactory {

    fun make(): PolymorphicJsonAdapterFactory<Action> {
        var factory = PolymorphicJsonAdapterFactory.of(Action::class.java, BEAGLE_WIDGET_TYPE)
        factory = ActionJsonAdapterFactory.make(factory)
        factory = registerUserActions(factory)
        return factory
    }

    private fun registerUserActions(factory: PolymorphicJsonAdapterFactory<Action>):
        PolymorphicJsonAdapterFactory<Action> {
        var newFactory = factory
        val customActions = BeagleEnvironment.beagleSdk.registeredActions()
        customActions.forEach {
            newFactory = newFactory.withSubtype(it, createNamespace(NAMESPACE, it))
        }
        return newFactory
    }

    private fun createNamespace(appNamespace: String, clazz: Class<*>): String {
        val typeName = clazz.simpleName.toLowerCase(Locale.getDefault())
        return "$appNamespace:$typeName"
    }
}
