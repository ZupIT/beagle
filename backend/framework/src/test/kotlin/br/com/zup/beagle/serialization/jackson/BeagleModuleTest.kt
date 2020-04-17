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

import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertTrue

internal class BeagleModuleTest {
    @Test
    fun beagleModule_should_be_initialized_with_BeagleSerializerModifier() {
        assertTrue {
            BeagleModule::class.memberProperties
                .find { it.name == "_serializerModifier" }
                ?.run {
                    this.isAccessible = true
                    this.get(BeagleModule) is BeagleSerializerModifier
                }!!
        }
    }
}