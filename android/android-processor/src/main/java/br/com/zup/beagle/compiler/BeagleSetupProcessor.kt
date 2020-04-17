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

import br.com.zup.beagle.compiler.util.BEAGLE_CONFIG
import br.com.zup.beagle.compiler.util.BEAGLE_SDK
import br.com.zup.beagle.compiler.util.CUSTOM_ACTION_HANDLER
import br.com.zup.beagle.compiler.util.DEEP_LINK_HANDLER
import br.com.zup.beagle.compiler.util.HTTP_CLIENT_HANDLER
import br.com.zup.beagle.compiler.util.error
import br.com.zup.beagle.widget.Widget
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

class BeagleSetupProcessor(
    private val processingEnv: ProcessingEnvironment,
    private val beagleSetupRegisteredWidgetGenerator: BeagleSetupRegisteredWidgetGenerator =
        BeagleSetupRegisteredWidgetGenerator(),
    private val beagleSetupPropertyGenerator: BeagleSetupPropertyGenerator =
        BeagleSetupPropertyGenerator(processingEnv)
) {

    fun process(
        basePackageName: String,
        beagleConfigClassName: String,
        roundEnvironment: RoundEnvironment
    ) {
        val beagleSetupClassName = "BeagleSetup"

        val typeSpec = TypeSpec.classBuilder(beagleSetupClassName)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addSuperinterface(ClassName(BEAGLE_SDK.packageName, BEAGLE_SDK.className))
            .addFunction(beagleSetupRegisteredWidgetGenerator.generate(roundEnvironment))
            .addProperties(beagleSetupPropertyGenerator.generate(
                basePackageName,
                roundEnvironment
            ))
            .addProperty(createBeagleConfigAttribute(beagleConfigClassName))
            .build()

        val beagleSetupFile = FileSpec.builder(
            basePackageName,
            beagleSetupClassName
        ).addImport(BEAGLE_CONFIG.packageName, BEAGLE_CONFIG.className)
            .addImport(BEAGLE_SDK.packageName, BEAGLE_SDK.className)
            .addImport(CUSTOM_ACTION_HANDLER.packageName, CUSTOM_ACTION_HANDLER.className)
            .addImport(DEEP_LINK_HANDLER.packageName, DEEP_LINK_HANDLER.className)
            .addImport(HTTP_CLIENT_HANDLER.packageName, HTTP_CLIENT_HANDLER.className)
            .addImport(basePackageName, beagleConfigClassName)
            .addImport(Widget::class, "")
            .addType(typeSpec)
            .build()

        try {
            beagleSetupFile.writeTo(processingEnv.filer)
        } catch (e: IOException) {
            val errorMessage = "Error when trying to generate code.\n${e.message!!}"
            processingEnv.messager.error(errorMessage)
        }
    }

    private fun createBeagleConfigAttribute(beagleConfigClassName: String): PropertySpec {
        return PropertySpec.builder(
            "config",
            ClassName(BEAGLE_CONFIG.packageName, BEAGLE_CONFIG.className),
            KModifier.OVERRIDE
        ).initializer("$beagleConfigClassName()").build()
    }
}