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

package br.com.zup.beagle.compiler.shared

import br.com.zup.beagle.annotation.RegisterOperation
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

const val REGISTERED_OPERATIONS = "registeredOperations"

class RegisteredOperationGenerator {

    private val temporaryListOfNames = mutableListOf<String>()

    fun generate(roundEnvironment: RoundEnvironment, processingEnv: ProcessingEnvironment): FunSpec {
        val operations = StringBuilder()
        val registerOperationElements = roundEnvironment.getElementsAnnotatedWith(RegisterOperation::class.java)

        registerOperationElements.forEach { element ->
            val registerOperationAnnotation = element.getAnnotation(RegisterOperation::class.java)
            val name = registerOperationAnnotation.name

            if (temporaryListOfNames.contains(name)) {
                val errorMessage = "there is another operation with the same name\n " +
                    "a class that was found with a duplicate name: $element"
                processingEnv.messager.error(errorMessage)
                return@forEach
            }

            temporaryListOfNames.add(name)
            operations.append("\"$name\" to $element(),")
            operations.append("\n")
        }

        val operationsFormatted = operations.toString().removeSuffix("\n").removeSuffix(",")

        return createFuncSpec()
            .addCode("""
                        |val operations = mapOf<String, Operation>(
                        |   $operationsFormatted
                        |)
                    |""".trimMargin())
            .addStatement("return operations")
            .build()
    }

    fun createFuncSpec(): FunSpec.Builder {
        val returnType = Map::class.asClassName().parameterizedBy(
            String::class.asClassName(),
            ClassName(ANDROID_OPERATION.packageName, ANDROID_OPERATION.className)
        )

        return FunSpec.builder(REGISTERED_OPERATIONS)
            .returns(returnType)
    }
}