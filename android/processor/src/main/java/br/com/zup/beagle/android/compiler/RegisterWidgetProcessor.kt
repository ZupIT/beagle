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

import br.com.zup.beagle.compiler.BeagleSetupRegisteredWidgetGenerator
import br.com.zup.beagle.compiler.REGISTERED_WIDGETS
import br.com.zup.beagle.compiler.error
import br.com.zup.beagle.widget.Widget
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

const val REGISTERED_WIDGETS_GENERATED = "RegisteredWidgets"

class RegisterWidgetProcessorProcessor(
    private val processingEnv: ProcessingEnvironment,
    private val beagleSetupRegisteredWidgetGenerator: BeagleSetupRegisteredWidgetGenerator =
        BeagleSetupRegisteredWidgetGenerator()
) {

    fun process(packageName: String, roundEnvironment: RoundEnvironment) {
        val typeSpec = TypeSpec.classBuilder(REGISTERED_WIDGETS_GENERATED)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addFunction(createRegisteredWidgetsFunctionInternal(roundEnvironment))
            .build()

        try {
            FileSpec.builder(packageName, REGISTERED_WIDGETS_GENERATED)
                .addImport(Widget::class, "")
                .addAnnotation(
                    AnnotationSpec.builder(Suppress::class.java)
                        .addMember("%S", "UNCHECKED_CAST")
                        .build()
                )
                .addType(typeSpec)
                .build()
                .writeTo(processingEnv.filer)
        } catch (e: IOException) {
            val errorMessage = "Error when trying to generate code.\n${e.message!!}"
            processingEnv.messager.error(errorMessage)
        }
    }

    private fun createRegisteredWidgetsFunctionInternal(roundEnvironment: RoundEnvironment): FunSpec {
        return beagleSetupRegisteredWidgetGenerator.generate(roundEnvironment)
    }

    fun createRegisteredWidgetsFunction(): FunSpec {
        return beagleSetupRegisteredWidgetGenerator.createFuncSpec()
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("return $REGISTERED_WIDGETS_GENERATED().$REGISTERED_WIDGETS()")
            .build()
    }
}