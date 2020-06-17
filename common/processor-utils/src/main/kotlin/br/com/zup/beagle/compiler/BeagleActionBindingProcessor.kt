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

package br.com.zup.beagle.compiler

import br.com.zup.beagle.annotation.RegisterAction
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@Suppress("UNCHECKED_CAST")
class BeagleActionBindingProcessor(
    private val processingEnv: ProcessingEnvironment,
    private val beagleActionBindingGenerator: BeagleActionBindingGenerator = BeagleActionBindingGenerator(
        processingEnv
    )
) {

    private val outputDirectory: Filer = processingEnv.filer

    fun process(
        roundEnvironment: RoundEnvironment
    ) {
        val registerWidgetAnnotatedClasses = roundEnvironment
            .getElementsAnnotatedWith(RegisterAction::class.java)
        registerWidgetAnnotatedClasses.forEach { element ->
            handle(element)
        }
    }

    private fun handle(element: Element) {
        if (element is TypeElement && element.kind.isClass) {
            try {
                val typeSpecBuilder =
                    beagleActionBindingGenerator.buildActionClassSpec(
                        element,
                        BINDING_SUFFIX)

                typeSpecBuilder.addFunction(beagleActionBindingGenerator.getFunctionHandleSpec(element))
                typeSpecBuilder.addFunction(
                    beagleActionBindingGenerator.getFunctionGetBindAttributes(element)
                ).addSuperinterface(ClassName(
                    BINDING_ADAPTER.packageName,
                    BINDING_ADAPTER.className
                ))

                val typeSpec = typeSpecBuilder.build()
                val fileSpec = FileSpec.builder(
                    processingEnv.elementUtils.getPackageAsString(element),
                    "${element.simpleName}$BINDING_SUFFIX"
                ).addType(typeSpec)
                    .addImport(GET_VALUE_NOT_NULL.packageName, GET_VALUE_NOT_NULL.className)
                    .build()

                fileSpec.writeTo(outputDirectory)
            } catch (e: IOException) {
                val errorMessage = "Error when trying to generate code.\n${e.message!!}"
                processingEnv.messager.error(errorMessage)
            }
        } else {
            processingEnv.messager.warning(element, "Skipped element $element.simpleName")
        }
    }

    companion object {
        private const val BINDING_SUFFIX = "Binding"
    }
}