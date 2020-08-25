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
import br.com.zup.beagle.compiler.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.type.MirroredTypeException

const val CONTROLLER_REFERENCE_GENERATED = "ControllerReferenceGenerated"

class RegisterControllerProcessor(private val processingEnv: ProcessingEnvironment) {

    fun process(packageName: String, roundEnvironment: RoundEnvironment) {
        val typeSpec = TypeSpec.classBuilder(CONTROLLER_REFERENCE_GENERATED)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addSuperinterface(ClassName(
                CONTROLLER_REFERENCE.packageName,
                CONTROLLER_REFERENCE.className
            ))
            .addFunction(createClassForMethod(roundEnvironment))
            .build()

        try {
            FileSpec.builder(packageName, CONTROLLER_REFERENCE_GENERATED)
                .addImport(CONTROLLER_REFERENCE.packageName, CONTROLLER_REFERENCE.className)
                .addImport(BEAGLE_ACTIVITY.packageName, BEAGLE_ACTIVITY.className)
                .addType(typeSpec)
                .build()
        } catch (e: IOException) {
            val errorMessage = "Error when trying to generate code.\n${e.message!!}"
            processingEnv.messager.error(errorMessage)
        }
    }

    private fun createClassForMethod(roundEnvironment: RoundEnvironment): FunSpec {
        val validatorLines = createValidatorLines(roundEnvironment)
        val returnType = List::class.asClassName().parameterizedBy(
            Class::class.asClassName().parameterizedBy(
                ClassName(CONTROLLER_REFERENCE.packageName, CONTROLLER_REFERENCE.className)
            )
        )

        return FunSpec.builder("classFor")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("name", String::class)
            .returns(returnType)
            .addStatement("""
                |return when(name) {
                |   $validatorLines
                |   else -> null
                |}
            |""".trimMargin())
            .build()
    }

    private fun createValidatorLines(roundEnvironment: RoundEnvironment): String {
        val validators = StringBuilder()
        val registerValidatorAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(
            RegisterController::class.java
        )
        registerValidatorAnnotatedClasses.forEachIndexed { index, element ->
            val registerValidatorAnnotation = element.getAnnotation(RegisterController::class.java)
            val name = try {
                (registerValidatorAnnotation as RegisterController).id
            } catch (mte: MirroredTypeException) {
                mte.typeMirror.toString()
            }
            validators.append("\"$name\" -> $element as Class<BeagleActivity>")
            if (index < registerValidatorAnnotatedClasses.size - 1) {
                validators.append("\n")
            }
        }
        return validators.toString()
    }
}