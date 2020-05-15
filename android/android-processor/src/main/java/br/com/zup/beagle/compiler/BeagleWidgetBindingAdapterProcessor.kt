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

import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.compiler.util.BINDING_ADAPTER
import br.com.zup.beagle.compiler.util.GET_VALUE_NOT_NULL
import br.com.zup.beagle.compiler.util.GET_VALUE_NULL
import br.com.zup.beagle.compiler.util.error
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

class BeagleWidgetBindingAdapterProcessor(
    private val processingEnv: ProcessingEnvironment,
    private val beagleWidgetBindingAdapterGenerator: BeagleWidgetBindingAdapterGenerator =
        BeagleWidgetBindingAdapterGenerator(
            processingEnv
        )
) {

    fun process(
        roundEnvironment: RoundEnvironment
    ) {
        val registerWidgetAnnotatedClasses = roundEnvironment
            .getElementsAnnotatedWith(RegisterWidget::class.java)
        registerWidgetAnnotatedClasses.forEachIndexed { _, element ->

            val bindingAdapterClassName = beagleWidgetBindingAdapterGenerator
                .getBindingAdapterClassNameForWidget(element)

            val typeSpec = TypeSpec.classBuilder(bindingAdapterClassName)
                .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
                .primaryConstructor(beagleWidgetBindingAdapterGenerator.getConstructorParametersSpec(element)
                    .build())
                .addProperty(beagleWidgetBindingAdapterGenerator.getPropertyWidget(element))
                .addProperty(beagleWidgetBindingAdapterGenerator.getPropertyBinding(element))
                .addSuperinterface(ClassName(
                    BINDING_ADAPTER.packageName,
                    BINDING_ADAPTER.className
                ))
                .addFunction(
                    beagleWidgetBindingAdapterGenerator.getFunctionBindModel(element)
                )
                .addFunction(
                    beagleWidgetBindingAdapterGenerator.getFunctionGetBindAttributes(element)
                )
                .build()
            val packageElement = beagleWidgetBindingAdapterGenerator.getPackageOf(element)

            val beagleSetupFile = FileSpec.builder(
                packageElement,
                bindingAdapterClassName
            ).addImport(GET_VALUE_NULL.packageName, GET_VALUE_NULL.className)
                .addImport(GET_VALUE_NOT_NULL.packageName, GET_VALUE_NOT_NULL.className)
                .addType(typeSpec)
                .build()

            try {
                beagleSetupFile.writeTo(processingEnv.filer)
            } catch (e: IOException) {
                val errorMessage = "Error when trying to generate code.\n${e.message!!}"
                processingEnv.messager.error(errorMessage)
            }
        }

    }
}