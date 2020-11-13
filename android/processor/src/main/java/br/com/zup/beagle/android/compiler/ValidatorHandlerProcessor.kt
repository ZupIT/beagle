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

import br.com.zup.beagle.compiler.shared.error
import br.com.zup.beagle.android.annotation.RegisterValidator
import br.com.zup.beagle.compiler.shared.implementsInterface
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
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException

const val VALIDATOR_HANDLER_IMPL_NAME = "ValidatorHandlerImpl"

class ValidatorHandlerProcessor(private val processingEnv: ProcessingEnvironment) {

    fun process(packageName: String, roundEnvironment: RoundEnvironment) {
        val validatorHandlerTypeSpec = TypeSpec.classBuilder(VALIDATOR_HANDLER_IMPL_NAME)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addSuperinterface(ClassName(
                VALIDATOR_HANDLER.packageName,
                VALIDATOR_HANDLER.className
            ))
            .addFunction(createValidatorMethod(roundEnvironment))
            .build()

        try {
            FileSpec.builder(packageName, VALIDATOR_HANDLER_IMPL_NAME)
                .addImport(VALIDATOR_HANDLER.packageName, VALIDATOR_HANDLER.className)
                .addImport(VALIDATOR.packageName, VALIDATOR.className)
                .addType(validatorHandlerTypeSpec)
                .build()
                .writeTo(processingEnv.filer)
        } catch (e: IOException) {
            val errorMessage = "Error when trying to generate code.\n${e.message!!}"
            processingEnv.messager.error(errorMessage)
        }
    }

    private fun createValidatorMethod(roundEnvironment: RoundEnvironment): FunSpec {
        val validatorLines = createValidatorLines(roundEnvironment)
        val returnType = ClassName(VALIDATOR.packageName, VALIDATOR.className)
            .parameterizedBy(Any::class.asClassName(), Any::class.asClassName())
            .copy(nullable = true)
        return FunSpec.builder("getValidator")
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
            RegisterValidator::class.java
        )
        registerValidatorAnnotatedClasses.forEachIndexed { index, element ->
            val typeElement = element as TypeElement
            if (!typeElement.implementsInterface(VALIDATOR.toString())) {
                val errorMessage = "Class annotated with @RegisterValidator " +
                        "does not implement $VALIDATOR."
                processingEnv.messager.error(typeElement, errorMessage)
            }

            val registerValidatorAnnotation = element.getAnnotation(RegisterValidator::class.java)
            val name = try {
                (registerValidatorAnnotation as RegisterValidator).name
            } catch (mte: MirroredTypeException) {
                mte.typeMirror.toString()
            }
            validators.append("\"$name\" -> $element() as Validator<Any, Any>")
            if (index < registerValidatorAnnotatedClasses.size - 1) {
                validators.append("\n")
            }
        }
        return validators.toString()
    }
}