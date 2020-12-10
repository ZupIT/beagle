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
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class GenerateFunctionOperation(private val processingEnv: ProcessingEnvironment)
    : BeagleGeneratorFunction<RegisterOperation>(
    ANDROID_OPERATION,
    REGISTERED_OPERATIONS,
    RegisterOperation::class.java
) {

    private val temporaryListOfNames = mutableListOf<String>()

    override fun returnStatementInGenerate(): String = "return operations"

    override fun buildCodeByElement(element: Element, annotation: Annotation): String {
        val name = (annotation as RegisterOperation).name
        temporaryListOfNames.add(name)
        return "\"$name\" to $element(), \n"
    }

    override fun validationElement(element: Element, annotation: Annotation) {
        val name = (annotation as RegisterOperation).name

        val typeElement = element as TypeElement
        when {
            temporaryListOfNames.contains(name) -> {
                val errorMessage = "there is another operation with the same name\n " +
                    "a class that was found with a duplicate name: $element"
                processingEnv.messager.error(errorMessage)
            }
            !isValidInheritance(typeElement) -> {
                val errorMessage = "The class $element need to inherit from the class " +
                    "${ANDROID_OPERATION.className} when annotate class with @RegisterOperation."
                processingEnv.messager.error(typeElement, errorMessage)
            }
        }
    }

    override fun getCodeFormatted(allCodeMappedWithAnnotation: String): String =
        """
            |val operations = mapOf<String, Operation>(
            |   $allCodeMappedWithAnnotation
            |)
        |""".trimMargin()

    override fun createFuncSpec(name: String): FunSpec.Builder {
        val returnType = Map::class.asClassName().parameterizedBy(
            String::class.asClassName(),
            ClassName(ANDROID_OPERATION.packageName, ANDROID_OPERATION.className)
        )

        return FunSpec.builder(name)
            .returns(returnType)
    }

    private fun isValidInheritance(typeElement: TypeElement): Boolean =
        typeElement.implements(ANDROID_OPERATION, processingEnv)

    companion object {
        const val REGISTERED_OPERATIONS = "registeredOperations"
    }
}