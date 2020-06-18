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

import br.com.zup.beagle.core.BindAttribute
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.ScreenBuilder
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BeagleModuleTest {
    @Test
    fun beagleModule_should_be_initialized_with_BeagleSerializerModifier_and_mixins() {
        val mixins = BeagleModule::class.property("_mixins") as Map<Class<*>, Class<*>>

        assertTrue { BeagleModule::class.property("_serializerModifier") is BeagleSerializerModifier }

        assertEquals(ComposeComponentMixin::class.java, mixins[ComposeComponent::class.java])
        assertEquals(ScreenBuilderMixin::class.java, mixins[ScreenBuilder::class.java])
        assertEquals(BindMixin::class.java, mixins[BindAttribute::class.java])
    }

    private fun KClass<BeagleModule>.property(name: String) =
        this.memberProperties.find { it.name == name }?.run {
            this.isAccessible = true
            this.get(BeagleModule())
        }!!
}