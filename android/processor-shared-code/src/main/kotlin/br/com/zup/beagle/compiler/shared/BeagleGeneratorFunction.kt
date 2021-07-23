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

package br.com.zup.beagle.compiler.shared

import com.squareup.kotlinpoet.FunSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element

abstract class BeagleGeneratorFunction<T : Annotation>(
    protected val processingEnv: ProcessingEnvironment,
    private val functionName: String,
    private val annotation: Class<T>,
    private val registrarComponentsProvider: RegistrarComponentsProvider? = null,
) {

    abstract fun buildCodeByElement(element: Element, annotation: Annotation): String

    abstract fun buildCodeByDependency(
        registeredDependency: Pair<RegisteredComponentId, RegisteredComponentFullName>
    ): String

    abstract fun validationElement(element: Element, annotation: Annotation)

    abstract fun getCodeFormatted(allCodeMappedWithAnnotation: String): String

    abstract fun returnStatementInGenerate(): String

    abstract fun createFuncSpec(name: String): FunSpec.Builder

    open fun generate(roundEnvironment: RoundEnvironment, className: String): FunSpec {
        val registeredComponentsInDependencies = getRegisteredComponentsInDependencies(className)
        val classesWithAnnotation = getAllClassWithAnnotation(roundEnvironment) + registeredComponentsInDependencies
        return createFuncSpec(functionName)
            .addCode(getCodeFormatted(classesWithAnnotation))
            .addStatement(returnStatementInGenerate())
            .build()
    }

    private fun getRegisteredComponentsInDependencies(className: String): StringBuilder {
        val registeredWidgets = StringBuilder()
        registrarComponentsProvider
            ?.getRegisteredComponentsInDependencies(
                processingEnv,
                className,
                functionName,
            )
            ?.forEach { registeredDependency ->
                registeredWidgets.append(buildCodeByDependency(registeredDependency))
            }

        return registeredWidgets
    }

    fun getFunctionName(): String = functionName

    private fun getAllClassWithAnnotation(roundEnvironment: RoundEnvironment): String {
        val stringBuilder = StringBuilder()
        val elements = roundEnvironment.getElementsAnnotatedWith(annotation)

        elements.forEach { element ->
            val annotation = element.getAnnotation(annotation)

            validationElement(element, annotation)

            stringBuilder.append(buildCodeByElement(element, annotation))
        }

        return stringBuilder.toString()
    }
}