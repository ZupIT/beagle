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
import br.com.zup.beagle.widget.core.ComposeComponent
import br.com.zup.beagle.widget.layout.ScreenBuilder
import com.fasterxml.jackson.databind.module.SimpleModule

class BeagleModule(
    private val classLoader: ClassLoader = BeagleModule::class.java.classLoader
) : SimpleModule() {

    init {
        this.setSerializerModifier(BeagleSerializerModifier(this.classLoader))
        this.setMixInAnnotation(
            getClass(ComposeComponent::class, this.classLoader),
            getClass(ComposeComponentMixin::class, this.classLoader)
        )
        this.setMixInAnnotation(
            getClass(ScreenBuilder::class, this.classLoader),
            getClass(ScreenBuilderMixin::class, this.classLoader)
        )
        this.setMixInAnnotation(
            getClass(BindAttribute::class, this.classLoader),
            getClass(BindMixin::class, this.classLoader)
        )
    }
}