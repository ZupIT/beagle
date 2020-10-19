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

import br.com.zup.beagle.compiler.ANDROID_ACTION
import br.com.zup.beagle.compiler.BEAGLE_CONFIG
import br.com.zup.beagle.compiler.BEAGLE_CUSTOM_ADAPTER
import br.com.zup.beagle.compiler.BEAGLE_CUSTOM_ADAPTER_IMPL
import br.com.zup.beagle.compiler.BEAGLE_IMAGE_DOWNLOADER
import br.com.zup.beagle.compiler.BEAGLE_LOGGER
import br.com.zup.beagle.compiler.BEAGLE_SDK
import br.com.zup.beagle.compiler.CONTROLLER_REFERENCE
import br.com.zup.beagle.compiler.DEEP_LINK_HANDLER
import br.com.zup.beagle.compiler.FORM_LOCAL_ACTION_HANDLER
import br.com.zup.beagle.compiler.HTTP_CLIENT_HANDLER
import br.com.zup.beagle.compiler.error
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
    private val registerWidgetProcessorProcessor: RegisterWidgetProcessorProcessor =
        RegisterWidgetProcessorProcessor(processingEnv),
    private val registerActionProcessorProcessor: RegisterActionProcessorProcessor =
        RegisterActionProcessorProcessor(processingEnv),
    private val beagleSetupPropertyGenerator: BeagleSetupPropertyGenerator =
        BeagleSetupPropertyGenerator(processingEnv),
    private val registerAnnotationProcessor: RegisterControllerProcessor =
        RegisterControllerProcessor(processingEnv),
    private val registerBeagleAdapterProcessor: RegisterBeagleAdapterProcessor =
        RegisterBeagleAdapterProcessor(processingEnv)
) {

    fun process(
        basePackageName: String,
        beagleConfigClassName: String,
        roundEnvironment: RoundEnvironment
    ) {
        val beagleSetupClassName = "BeagleSetup"

        val properties = beagleSetupPropertyGenerator.generate(
            basePackageName,
            roundEnvironment
        )
        val typeSpec = TypeSpec.classBuilder(beagleSetupClassName)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addSuperinterface(ClassName(BEAGLE_SDK.packageName, BEAGLE_SDK.className))
            .addFunction(registerWidgetProcessorProcessor.createRegisteredWidgetsFunction())
            .addFunction(registerActionProcessorProcessor.createRegisteredActionsFunction())


        val beagleSetupFile = addDefaultImports(basePackageName, beagleSetupClassName, beagleConfigClassName)

        val propertyIndex = properties.indexOfFirst { it.name == "serverDrivenActivity" }

        var property = properties[propertyIndex]

        registerWidgetProcessorProcessor.process(basePackageName, roundEnvironment)
        registerActionProcessorProcessor.process(basePackageName, roundEnvironment)
        registerAnnotationProcessor.process(basePackageName, roundEnvironment, property.initializer.toString())
        registerBeagleAdapterProcessor.process(
            BEAGLE_CUSTOM_ADAPTER.packageName,
            roundEnvironment)

        val defaultActivity = registerAnnotationProcessor.defaultActivityRegistered
        property = beagleSetupPropertyGenerator.implementServerDrivenActivityProperty(
            defaultActivity,
            isFormatted = true
        )

        val newProperties = properties.toMutableList().apply {
            this[propertyIndex] = property
        }


        val newTypeSpecBuilder = typeSpec.addProperties(newProperties)
            .addProperty(createBeagleConfigAttribute(beagleConfigClassName))
            .addProperty(PropertySpec.builder(
                "typeAdapterResolver",
                ClassName(BEAGLE_CUSTOM_ADAPTER.packageName, BEAGLE_CUSTOM_ADAPTER.className),
                KModifier.OVERRIDE
            ).initializer("${BEAGLE_CUSTOM_ADAPTER_IMPL.className}()")
                .build())
        try {
            beagleSetupFile
                .addType(newTypeSpecBuilder.build())
                .build()
                .writeTo(processingEnv.filer)
        } catch (e: IOException) {
            val errorMessage = "Error when trying to generate code.\n${e.message!!}"
            processingEnv.messager.error(errorMessage)
        }
    }

    private fun addDefaultImports(
        basePackageName: String,
        beagleSetupClassName: String,
        beagleConfigClassName: String
    ): FileSpec.Builder {
        return FileSpec.builder(
            basePackageName,
            beagleSetupClassName
        ).addImport(BEAGLE_CONFIG.packageName, BEAGLE_CONFIG.className)
            .addImport(BEAGLE_SDK.packageName, BEAGLE_SDK.className)
            .addImport(FORM_LOCAL_ACTION_HANDLER.packageName, FORM_LOCAL_ACTION_HANDLER.className)
            .addImport(DEEP_LINK_HANDLER.packageName, DEEP_LINK_HANDLER.className)
            .addImport(HTTP_CLIENT_HANDLER.packageName, HTTP_CLIENT_HANDLER.className)
            .addImport(BEAGLE_LOGGER.packageName, BEAGLE_LOGGER.className)
            .addImport(BEAGLE_IMAGE_DOWNLOADER.packageName, BEAGLE_IMAGE_DOWNLOADER.className)
            .addImport(CONTROLLER_REFERENCE.packageName, CONTROLLER_REFERENCE.className)
            .addImport(BEAGLE_CUSTOM_ADAPTER_IMPL.packageName, BEAGLE_CUSTOM_ADAPTER_IMPL.className)
            .addImport(basePackageName, beagleConfigClassName)
            .addImport(Widget::class, "")
            .addImport(ClassName(ANDROID_ACTION.packageName, ANDROID_ACTION.className), "")
    }

    private fun createBeagleConfigAttribute(beagleConfigClassName: String): PropertySpec {
        return PropertySpec.builder(
            "config",
            ClassName(BEAGLE_CONFIG.packageName, BEAGLE_CONFIG.className),
            KModifier.OVERRIDE
        ).initializer("$beagleConfigClassName()").build()
    }
}