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

import br.com.zup.beagle.compiler.BIND_MODEL_METHOD
import br.com.zup.beagle.compiler.BeagleBindingHandler
import br.com.zup.beagle.compiler.GET_VALUE_NOT_NULL_METHOD
import br.com.zup.beagle.compiler.GET_VALUE_NULL_METHOD
import br.com.zup.beagle.compiler.MESSAGE_PARAMETER
import br.com.zup.beagle.compiler.ON_ERROR_MESSAGE_METHOD
import br.com.zup.beagle.compiler.RETRIEVE_VALUE_METHOD
import br.com.zup.beagle.compiler.WIDGET_INSTANCE_PROPERTY
import br.com.zup.beagle.compiler.fieldName
import br.com.zup.beagle.compiler.isMarkedNullable
import br.com.zup.beagle.compiler.visibleGetters
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

class BeagleWidgetBindingGenerator(processingEnv: ProcessingEnvironment) :
    BeagleBindingHandler(processingEnv) {

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
            .addModifiers(KModifier.PRIVATE)
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

}