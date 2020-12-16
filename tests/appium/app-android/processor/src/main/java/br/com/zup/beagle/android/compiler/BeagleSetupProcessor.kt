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

import br.com.zup.beagle.android.compiler.generatefunction.GenerateFunctionAction
import br.com.zup.beagle.android.compiler.generatefunction.GenerateFunctionCustomAdapter
import br.com.zup.beagle.android.compiler.generatefunction.GenerateFunctionCustomValidator
import br.com.zup.beagle.android.compiler.generatefunction.RegisterControllerProcessor
import br.com.zup.beagle.compiler.shared.ANDROID_OPERATION
import br.com.zup.beagle.compiler.shared.GenerateFunctionOperation
import br.com.zup.beagle.compiler.shared.GenerateFunctionWidget
import br.com.zup.beagle.compiler.shared.GenericFactoryProcessor
import br.com.zup.beagle.compiler.shared.WIDGET_VIEW
import br.com.zup.beagle.compiler.shared.error
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

internal data class BeagleSetupProcessor(
    private val processingEnv: ProcessingEnvironment,
    private val beagleSetupPropertyGenerator: BeagleSetupPropertyGenerator =
        BeagleSetupPropertyGenerator(processingEnv),
    private val registerAnnotationProcessor: RegisterControllerProcessor =
        RegisterControllerProcessor(processingEnv),
) {

    private val widgetFactoryProcessor = GenericFactoryProcessor(
        processingEnv,
        REGISTERED_WIDGETS_GENERATED,
        GenerateFunctionWidget(processingEnv)
    )

    private val operationFactoryProcessor = GenericFactoryProcessor(
        processingEnv,
        REGISTERED_OPERATIONS_GENERATED,
        GenerateFunctionOperation(processingEnv)
    )

    private val actionFactoryProcessor = GenericFactoryProcessor(
        processingEnv,
        REGISTERED_ACTIONS_GENERATED,
        GenerateFunctionAction(processingEnv)
    )

    private val customAdapterFactoryProcessor = GenericFactoryProcessor(
        processingEnv,
        REGISTERED_CUSTOM_TYPE_ADAPTER_GENERATED,
        GenerateFunctionCustomAdapter(processingEnv)
    )

    private val customValidatorFactoryProcessor = GenericFactoryProcessor(
        processingEnv,
        REGISTERED_CUSTOM_VALIDATOR_GENERATED,
        GenerateFunctionCustomValidator(processingEnv)
    )

    fun process(
        basePackageName: String,
        beagleConfigClassName: String,
        roundEnvironment: RoundEnvironment,
    ) {

        val properties = beagleSetupPropertyGenerator.generate(roundEnvironment)
        val typeSpec = TypeSpec.classBuilder(BEAGLE_SETUP_GENERATED)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addSuperinterface(ClassName(BEAGLE_SDK.packageName, BEAGLE_SDK.className))
            .addFunction(widgetFactoryProcessor.createFunction())
            .addFunction(operationFactoryProcessor.createFunction())
            .addFunction(actionFactoryProcessor.createFunction())


        val beagleSetupFile = addDefaultImports(basePackageName, beagleConfigClassName)

        val propertyIndex = properties.indexOfFirst { it.name == "serverDrivenActivity" }

        var property = properties[propertyIndex]

        handleAllProcess(basePackageName, roundEnvironment, property)

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
            ).initializer(REGISTERED_CUSTOM_TYPE_ADAPTER_GENERATED)
                .build())
            .addProperty(PropertySpec.builder(
                "validatorHandler",
                ClassName(VALIDATOR_HANDLER.packageName, VALIDATOR_HANDLER.className),
                KModifier.OVERRIDE
            ).initializer(REGISTERED_CUSTOM_VALIDATOR_GENERATED)
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

    private fun handleAllProcess(basePackageName: String, roundEnvironment: RoundEnvironment, property: PropertySpec) {
        widgetFactoryProcessor.process(basePackageName, roundEnvironment, WIDGET_VIEW)
        operationFactoryProcessor.process(basePackageName, roundEnvironment, ANDROID_OPERATION)
        actionFactoryProcessor.process(basePackageName, roundEnvironment, ANDROID_ACTION)
        customAdapterFactoryProcessor.process(basePackageName, roundEnvironment,
            listOf(BEAGLE_CUSTOM_ADAPTER, BEAGLE_PARAMETERIZED_TYPE_FACTORY), false, BEAGLE_CUSTOM_ADAPTER)
        registerAnnotationProcessor.process(basePackageName, roundEnvironment, property.initializer.toString())
        customValidatorFactoryProcessor.process(basePackageName, roundEnvironment,
            listOf(VALIDATOR_HANDLER, VALIDATOR), false, VALIDATOR_HANDLER)
    }

    private fun addDefaultImports(
        basePackageName: String,
        beagleConfigClassName: String,
    ): FileSpec.Builder {
        return FileSpec.builder(
            basePackageName,
            BEAGLE_SETUP_GENERATED
        ).addImport(BEAGLE_CONFIG.packageName, BEAGLE_CONFIG.className)
            .addImport(BEAGLE_SDK.packageName, BEAGLE_SDK.className)
            .addImport(FORM_LOCAL_ACTION_HANDLER.packageName, FORM_LOCAL_ACTION_HANDLER.className)
            .addImport(DEEP_LINK_HANDLER.packageName, DEEP_LINK_HANDLER.className)
            .addImport(HTTP_CLIENT_HANDLER.packageName, HTTP_CLIENT_HANDLER.className)
            .addImport(BEAGLE_LOGGER.packageName, BEAGLE_LOGGER.className)
            .addImport(BEAGLE_IMAGE_DOWNLOADER.packageName, BEAGLE_IMAGE_DOWNLOADER.className)
            .addImport(CONTROLLER_REFERENCE.packageName, CONTROLLER_REFERENCE.className)
            .addImport(basePackageName, beagleConfigClassName)
            .addImport(ClassName(ANDROID_ACTION.packageName, ANDROID_ACTION.className), "")
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class.java)
                    .addMember("%S, %S, %S", "OverridingDeprecatedMember", "DEPRECATION", "UNCHECKED_CAST")
                    .build()
            )
    }

    private fun createBeagleConfigAttribute(beagleConfigClassName: String): PropertySpec {
        return PropertySpec.builder(
            "config",
            ClassName(BEAGLE_CONFIG.packageName, BEAGLE_CONFIG.className),
            KModifier.OVERRIDE
        ).initializer("$beagleConfigClassName()").build()
    }

    companion object {
        internal const val REGISTERED_WIDGETS_GENERATED = "RegisteredWidgets"
        internal const val REGISTERED_OPERATIONS_GENERATED = "RegisteredOperations"
        internal const val REGISTERED_ACTIONS_GENERATED = "RegisteredActions"
        internal const val REGISTERED_CUSTOM_TYPE_ADAPTER_GENERATED = "RegisteredCustomTypeAdapter"
        internal const val REGISTERED_CUSTOM_VALIDATOR_GENERATED = "RegisteredCustomValidator"
        internal const val BEAGLE_SETUP_GENERATED = "BeagleSetup"
    }
}