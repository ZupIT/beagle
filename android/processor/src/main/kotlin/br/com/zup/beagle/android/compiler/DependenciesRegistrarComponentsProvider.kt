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

package br.com.zup.beagle.android.compiler

import br.com.zup.beagle.compiler.shared.RegisteredComponentFullName
import br.com.zup.beagle.compiler.shared.RegisteredComponentId
import br.com.zup.beagle.compiler.shared.RegistrarComponentsProvider
import javax.annotation.processing.ProcessingEnvironment

object DependenciesRegistrarComponentsProvider : RegistrarComponentsProvider {
    @Suppress("UNCHECKED_CAST")
    override fun getRegisteredComponentsInDependencies(
        processingEnv: ProcessingEnvironment,
        className: String,
        methodName: String,
    ): List<Pair<RegisteredComponentId, RegisteredComponentFullName>> {

        val registeredComponentsInDependencies =
            mutableListOf<Pair<RegisteredComponentId, RegisteredComponentFullName>>()

        processingEnv.elementUtils.getPackageElement(REGISTRAR_COMPONENTS_PACKAGE)?.enclosedElements?.forEach {
            val fullRegistrarClassName = it.toString()
            val registrarClassName = fullRegistrarClassName.substringAfterLast(".")
            if (!registrarClassName.startsWith(className)) {
                return@forEach
            }

            val cls = Class.forName(fullRegistrarClassName)
            val kotlinClass = cls.kotlin
            try {
                registeredComponentsInDependencies
                    .addAll(cls.getMethod(methodName).invoke(kotlinClass.objectInstance) as List<Pair<String, String>>)
            } catch (e: NoSuchMethodException) {
                // intentionally left blank
            }
        }

        return registeredComponentsInDependencies
    }
}