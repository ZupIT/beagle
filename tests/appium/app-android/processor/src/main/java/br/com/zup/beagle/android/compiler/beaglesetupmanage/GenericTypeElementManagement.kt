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

package br.com.zup.beagle.android.compiler.beaglesetupmanage

import br.com.zup.beagle.compiler.shared.error
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

internal class GenericTypeElementManagement(
    private val processingEnv: ProcessingEnvironment,
    private val typeElement: TypeElement) {

    internal fun manageTypeElement(propertySpecificationsElement: TypeElement?, element: String) : TypeElement? {
        if (propertySpecificationsElement == null) {
           return typeElement
        } else {
            logImplementationErrorMessage(typeElement, element)
        }

        return null
    }

    private fun logImplementationErrorMessage(typeElement: TypeElement, element: String) {
        processingEnv.messager?.error(typeElement, "$element already " +
            "defined, remove one implementation from the application.")
    }
}