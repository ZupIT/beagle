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

import br.com.zup.beagle.android.annotation.RegisterController
import br.com.zup.beagle.compiler.BEAGLE_ACTIVITY
import br.com.zup.beagle.compiler.CONTROLLER_REFERENCE
import br.com.zup.beagle.compiler.error
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

const val CONTROLLER_REFERENCE_GENERATED = "ControllerReferenceGenerated"

class RegisterControllerProcessor(private val processingEnv: ProcessingEnvironment) {

    var defaultActivityRegistered: String = ""

    fun process(packageName: String, roundEnvironment: RoundEnvironment, defaultActivity: String) {
        val typeSpec = TypeSpec.classBuilder(CONTROLLER_REFERENCE_GENERATED)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addSuperinterface(ClassName(
                CONTROLLER_REFERENCE.packageName,
                CONTROLLER_REFERENCE.className
            ))
            .addFunction(createClassForMethod(roundEnvironment, defaultActivity))
            .build()

        try {
            FileSpec.builder(packageName, CONTROLLER_REFERENCE_GENERATED)
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

        val spec = FunSpec.builder("classFor")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("id", String::class.asTypeName().copy(true))

        defaultActivityRegistered = validatorLines.second

        if (validatorLines.second.isEmpty()) {
            defaultActivityRegistered = defaultActivity
            spec.addStatement("return $defaultActivityRegistered")
        } else {
            spec.addStatement("""
                |return when(id) {
                |   ${validatorLines.first}
                |   else -> ${validatorLines.second}
                |}
            |""".trimMargin())
        }
        return spec.returns(returnType)
            .build()
    }

    private fun createValidatorLines(roundEnvironment: RoundEnvironment): Pair<String, String> {
        val validators = StringBuilder()
        val registerValidatorAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(
            RegisterController::class.java
        )

        var defaultControllerClass = ""

        registerValidatorAnnotatedClasses.forEachIndexed { index, element ->
            val registerValidatorAnnotation = element.getAnnotation(RegisterController::class.java)
            val name = try {
                (registerValidatorAnnotation as RegisterController).id
            } catch (mte: MirroredTypeException) {
                mte.typeMirror.toString()
            }

            if (name.isEmpty()) {
                defaultControllerClass = "$element::class.java as Class<BeagleActivity>"
                return@forEachIndexed
            }

            validators.append("\"$name\" -> $element::class.java as Class<BeagleActivity>")
            if (index < registerValidatorAnnotatedClasses.size - 1) {
                validators.append("\n")
            }
        }


        if (defaultControllerClass.isEmpty() && registerValidatorAnnotatedClasses.isNotEmpty()) {
            processingEnv.messager.error(" Default Beagle Activity were not defined. " +
                "Did you miss to create your own Activity that extends" +
                " from Beagle Activity and annotate it with @RegisterController and without id?")
        }
        return validators.toString() to defaultControllerClass
    }
}