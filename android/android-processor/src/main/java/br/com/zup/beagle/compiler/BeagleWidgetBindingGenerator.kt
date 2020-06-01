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

import br.com.zup.beagle.compiler.util.BIND
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

internal const val WIDGET_INSTANCE_PROPERTY = "widgetInstance"
internal const val BIND_MODEL_METHOD = "bindModel"
internal const val GET_VALUE_NULL_METHOD = "getValueNull"
internal const val GET_VALUE_NOT_NULL_METHOD = "getValueNotNull"
internal const val GET_BIND_ATTRIBUTES_METHOD = "getBindAttributes"
internal const val RETRIEVE_VALUE_METHOD = "getValue"
internal const val ON_ERROR_MESSAGE_METHOD = "onErrorMessage"
internal const val MESSAGE_PARAMETER = "message"

class BeagleWidgetBindingGenerator(private val processingEnv: ProcessingEnvironment) {

    fun getFunctionBindModel(element: TypeElement): FunSpec {
        val attributeValues = StringBuilder()
        val notifyValues = StringBuilder()

        val parameters = element.visibleGetters
        parameters.forEachIndexed { index, e ->
            val isNullable = e.isMarkedNullable
            val getValueMethodName = if (isNullable) GET_VALUE_NULL_METHOD else GET_VALUE_NOT_NULL_METHOD

            val attr = e.fieldName
            attributeValues.append("\t$attr = ${getValueMethodName}($attr, $WIDGET_INSTANCE_PROPERTY.$attr)")

            if (index < parameters.size - 1) {
                attributeValues.append(",\n")
            }
        }

        parameters.forEach { e ->
            val attr = e.fieldName
            notifyValues.append("""
                |$attr.observes {
                |   myWidget = myWidget.copy($attr = it)
                |   $WIDGET_INSTANCE_PROPERTY.onBind(myWidget, view)
                |}
                |
            """.trimMargin())
        }
        return FunSpec.builder(BIND_MODEL_METHOD)
            .addModifiers(KModifier.OVERRIDE)
            .addCode("""
                |var myWidget = ${element.simpleName}(
                |$attributeValues
                |)
                |$WIDGET_INSTANCE_PROPERTY.onBind(myWidget, view)
                |   $notifyValues
                |""".trimMargin())
            .build()
    }

    fun getFunctionRetrieveValue(): FunSpec {

        return FunSpec.builder(RETRIEVE_VALUE_METHOD)
            .addModifiers(KModifier.OVERRIDE)
            .returns(Any::class)
            .addStatement("return $WIDGET_INSTANCE_PROPERTY.${RETRIEVE_VALUE_METHOD}()")
            .build()
    }

    fun getFunctionOnErrorMessage(): FunSpec {

        return FunSpec.builder(ON_ERROR_MESSAGE_METHOD)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(MESSAGE_PARAMETER, String::class)
            .addStatement("return $WIDGET_INSTANCE_PROPERTY.${ON_ERROR_MESSAGE_METHOD}(message)")
            .build()
    }

    fun getFunctionGetBindAttributes(element: TypeElement): FunSpec {

        val returnType = List::class.asClassName().parameterizedBy(
            ClassName(BIND.packageName, BIND.className)
                .parameterizedBy(STAR)
        )
        val attributeValues = StringBuilder()

        val parameters = element.visibleGetters
        parameters.forEachIndexed { index, e ->
            attributeValues.append("\t${e.fieldName}")
            if (index < parameters.size - 1) {
                attributeValues.append(",\n")
            }
        }

        return FunSpec.builder(GET_BIND_ATTRIBUTES_METHOD)
            .addModifiers(KModifier.OVERRIDE)
            .addCode("""
                        |val bindAttributes = listOf(
                        |   $attributeValues
                        |)
                    |""".trimMargin())
            .addStatement("return bindAttributes")
            .returns(returnType)
            .build()
    }

}