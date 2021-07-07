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

package br.com.zup.beagle.android.compiler.generatefunction

import br.com.zup.beagle.android.annotation.RegisterController
import br.com.zup.beagle.android.compiler.BEAGLE_ACTIVITY
import br.com.zup.beagle.android.compiler.CONTROLLER_REFERENCE
import br.com.zup.beagle.android.compiler.DEFAULT_BEAGLE_ACTIVITY
import br.com.zup.beagle.compiler.shared.error
import br.com.zup.beagle.compiler.shared.forEachRegisteredDependency
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.type.MirroredTypeException

const val REGISTERED_CONTROLLERS_GENERATED = "RegisteredControllers"

internal class RegisterControllerProcessor(private val processingEnv: ProcessingEnvironment) {

    companion object {
        const val REGISTERED_CONTROLLERS = "classFor"
        const val CONTROLLER_DEFINITION_SUFFIX = "::class.java as Class<BeagleActivity>"
    }

    var defaultActivityRegistered: String = ""

    fun process(packageName: String, roundEnvironment: RoundEnvironment, defaultActivity: String) {
        val typeSpec = TypeSpec.classBuilder(REGISTERED_CONTROLLERS_GENERATED)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addSuperinterface(ClassName(
                CONTROLLER_REFERENCE.packageName,
                CONTROLLER_REFERENCE.className
            ))
            .addFunction(createClassForMethod(roundEnvironment, defaultActivity))
            .build()

        try {
            FileSpec.builder(packageName, REGISTERED_CONTROLLERS_GENERATED)
                .addImport(CONTROLLER_REFERENCE.packageName, CONTROLLER_REFERENCE.className)
                .addImport(BEAGLE_ACTIVITY.packageName, BEAGLE_ACTIVITY.className)
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

    private fun createClassForMethod(roundEnvironment: RoundEnvironment, defaultActivity: String): FunSpec {
        val validatorLines = createValidatorLines(roundEnvironment)
        val returnType = Class::class.asClassName().parameterizedBy(
            ClassName(BEAGLE_ACTIVITY.packageName, BEAGLE_ACTIVITY.className)
        )

        val spec = FunSpec.builder(REGISTERED_CONTROLLERS)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("id", String::class.asTypeName().copy(true))


        val defaultBeagleClass = ClassName(DEFAULT_BEAGLE_ACTIVITY.packageName,
            DEFAULT_BEAGLE_ACTIVITY.className).canonicalName
        // TODO: refactored. Verificar
//        defaultActivityRegistered = if (validatorLines.second.isEmpty())
//            "$defaultBeagleClass::class.java as Class<BeagleActivity>" else validatorLines.second
        defaultActivityRegistered =
            if (defaultActivityRegistered.isEmpty() && !defaultActivity.startsWith("null::class")) {
                defaultActivity
            } else if (defaultActivityRegistered.isEmpty()) {
                "$defaultBeagleClass$CONTROLLER_DEFINITION_SUFFIX"
            } else {
                defaultActivityRegistered
            }

        var code = ""

        when {
            //validatorLines.second.isEmpty() && !defaultActivity.startsWith("null::class") -> {
            // TODO: essa linha abaixo refatora a de cima
//            defaultActivityRegistered.isEmpty() && !defaultActivity.startsWith("null::class") -> {
//                defaultActivityRegistered = defaultActivity
//            }
//            validatorLines.first.isNotEmpty() -> {
            validatorLines.isNotEmpty() -> {

                code = """
                    |return when(id) {
                    |$validatorLines
                    |    else -> $defaultActivityRegistered
                    |}
                |""".trimMargin()
            }
        }

        if (code.isEmpty()) {
            code = "return $defaultActivityRegistered"
        }

        return spec
            .addStatement(code)
            .returns(returnType)
            .build()
    }

    private fun createValidatorLines(roundEnvironment: RoundEnvironment): String {
        val validators = StringBuilder()
        val registerValidatorAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(
            RegisterController::class.java
        )

//        var defaultControllerClass = ""

        registerValidatorAnnotatedClasses.forEachIndexed { index, element ->
            val registerValidatorAnnotation = element.getAnnotation(RegisterController::class.java)
            val name = try {
                (registerValidatorAnnotation as RegisterController).id
            } catch (mte: MirroredTypeException) {
                mte.typeMirror.toString()
            }

            if (name.isEmpty()) {
//                defaultControllerClass = "$element::class.java as Class<BeagleActivity>"
                defaultActivityRegistered = "$element$CONTROLLER_DEFINITION_SUFFIX"
                return@forEachIndexed
            }

            validators.append("    \"$name\" -> $element$CONTROLLER_DEFINITION_SUFFIX")
            if (index < registerValidatorAnnotatedClasses.size - 1) {
                validators.append("\n")
            }
        }

        validators.append(getRegisteredControllersInDependencies(defaultActivityRegistered))

//        return validators.toString() to defaultControllerClass
        return validators.toString()
    }

    private fun getRegisteredControllersInDependencies(defaultControllerClass: String): java.lang.StringBuilder {
        val registeredWidgets = StringBuilder()
//        processingEnv.elementUtils.getPackageElement(REGISTRAR_COMPONENTS_PACKAGE)?.enclosedElements?.forEach {
//            val fullClassName = it.toString()
//            val cls = Class.forName(fullClassName)
//            val kotlinClass = cls.kotlin
//            try {
//                (cls.getMethod(REGISTERED_CONTROLLERS).invoke(kotlinClass.objectInstance) as List<Pair<String, String>>).forEach { registeredDependency ->
//                    //TODO: extrair para método
//                    if (defaultControllerClass.isNotEmpty() && registeredDependency.first.isEmpty()) {
//                        processingEnv.messager?.error("Default controller defined multiple times: " +
//                            "\n$defaultControllerClass" +
//                            "\n${registeredDependency.second}" +
//                            "\n\nYou must remove one implementation from the application.")
//                    }
//
//                    if (registeredDependency.first.isEmpty()) {
//                        defaultActivityRegistered = "${registeredDependency.second}::class.java as Class<BeagleActivity>"
//                    } else {
//                        registeredWidgets.append("\n    \"${registeredDependency.first}\" -> ${registeredDependency.second}::class.java as Class<BeagleActivity>")
//                    }
//                }
//            } catch (e: NoSuchMethodException) {
//                // intentionally left blank
//            }
//        }
        forEachRegisteredDependency(
            processingEnv,
            REGISTERED_CONTROLLERS_GENERATED,
            REGISTERED_CONTROLLERS
        ) { registeredDependency ->
            //TODO: extrair para método
            if (defaultActivityRegistered.isNotEmpty() && registeredDependency.first.isEmpty()) {
                processingEnv.messager?.error("Default controller defined multiple times: " +
                    "\n${defaultActivityRegistered.substringBefore(CONTROLLER_DEFINITION_SUFFIX)}" +
                    "\n${registeredDependency.second}" +
                    "\n\nYou must remove one implementation from the application.")
            }

            if (registeredDependency.first.isEmpty()) {
                defaultActivityRegistered = "${registeredDependency.second}$CONTROLLER_DEFINITION_SUFFIX"
            } else {
                registeredWidgets.append("\n    \"${registeredDependency.first}\" -> ${registeredDependency.second}$CONTROLLER_DEFINITION_SUFFIX")
            }
        }
        return registeredWidgets
    }
}