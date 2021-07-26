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

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment

class PropertiesRegistrarProcessor {

    fun process(
        processingEnv: ProcessingEnvironment,
        properties: List<PropertySpec>,
        className: String,
    ) {

        val fileSpecBuilder = FileSpec.builder(
            REGISTRAR_COMPONENTS_PACKAGE,
            className,
        )

        val funSpec = createFuncSpec()
            .addCode(getCodeFormatted(registeredProperties(properties)))
            .addStatement(createReturnStatement())
            .build()

        val typeSpec = createTypeSpec(className)
            .addFunction(funSpec)
            .build()

        val fileSpec = fileSpecBuilder
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(processingEnv.filer)
    }

    private fun createTypeSpec(className: String): TypeSpec.Builder {
        return TypeSpec.objectBuilder(className)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
    }

    private fun createFuncSpec(): FunSpec.Builder {
        val listReturnType = List::class.asClassName().parameterizedBy(
            Pair::class.asClassName().parameterizedBy(
                ClassName("kotlin", "String"),
                ClassName("kotlin", "String"),
            )
        )

        return FunSpec.builder(PROPERTIES_REGISTRAR_METHOD_NAME)
            .returns(listReturnType)
    }

    private fun createReturnStatement() = "return $PROPERTIES_REGISTRAR_PROPERTY_NAME"

    private fun getCodeFormatted(propertiesRegistered: String): String =
        """
            |val $PROPERTIES_REGISTRAR_PROPERTY_NAME = listOf<Pair<String, String>>(
            |   $propertiesRegistered
            |)
        |""".trimMargin()

    fun registeredProperties(properties: List<PropertySpec>): String {
        val registeredProperties = StringBuilder()
        properties.forEach { propertySpec ->
            if (!propertySpec.initializer.toString().startsWith("null")) {
                registeredProperties
                    .append("\tPair(\"${propertySpec.name}\", \"\"\"${propertySpec.initializer.toString()}\"\"\"),\n")
            }
        }

        return registeredProperties.toString()
    }
}