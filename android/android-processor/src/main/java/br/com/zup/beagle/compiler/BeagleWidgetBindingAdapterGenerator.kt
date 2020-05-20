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

import br.com.zup.beagle.compiler.util.BINDING
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

private const val WIDGET_PROPERTY = "widget"
private const val BINDING_PROPERTY = "binding"
private const val BIND_MODEL_METHOD = "bindModel"
private const val ADAPTER_SUFFIX = "BindingAdapter"
private const val GET_VALUE_NULL_METHOD = "getValueNull"
private const val GET_VALUE_NOT_NULL_METHOD = "getValueNotNull"
private const val GET_BIND_ATTRIBUTES_METHOD = "getBindAttributes"

class BeagleWidgetBindingAdapterGenerator(private val processingEnv: ProcessingEnvironment) {
    fun getPropertyBinding(element: Element) = addConstructorParameter(element)

    fun getPropertyWidget(element: Element): PropertySpec {
        val packageElement = getPackageOf(element)
        val bindingClassName = "${element.simpleName}"
        return PropertySpec.builder(WIDGET_PROPERTY, ClassName(
            packageElement,
            bindingClassName
        )).initializer(WIDGET_PROPERTY).build()
    }

    fun getFunctionBindModel(element: Element): FunSpec {
        val attributeValues = StringBuilder()
        val notifyValues = StringBuilder()

        val constructorParameters = element.visibleGetters
        constructorParameters.forEachIndexed { index, e ->
            val isNullable = e.isMarkedNullable()
            val getValueMethodName = if (isNullable) GET_VALUE_NULL_METHOD else GET_VALUE_NOT_NULL_METHOD

            val attr = e.fieldName
            attributeValues.append("\t$attr = ${getValueMethodName}($BINDING_PROPERTY.$attr, $WIDGET_PROPERTY.$attr)")

            if (index < constructorParameters.size - 1) {
                attributeValues.append(",\n")
            }
        }

        constructorParameters.forEachIndexed { index, e ->
            val attr = e.fieldName
            notifyValues.append("""
                |$BINDING_PROPERTY.$attr.observes {
                |   myWidget = myWidget.copy($attr = it)
                |   $WIDGET_PROPERTY.onBind(myWidget)
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
                |$WIDGET_PROPERTY.onBind(myWidget)
                |   $notifyValues
                |""".trimMargin())
            .build()
    }

    fun getBindingAdapterClassNameForWidget(element: Element) =
        "${element.simpleName}$ADAPTER_SUFFIX"

    fun getPackageOf(element: Element) = processingEnv.elementUtils.getPackageOf(element).toString()

    private fun addConstructorParameter(element: Element): PropertySpec {
        val packageElement = getPackageOf(element)
        val bindingClassName = "${element.simpleName}Binding"
        return PropertySpec.builder(BINDING_PROPERTY, ClassName(
            packageElement,
            bindingClassName
        )).initializer(BINDING_PROPERTY).build()
    }

    fun getFunctionGetBindAttributes(element: Element): FunSpec {

        val returnType = List::class.asClassName().parameterizedBy(
            ClassName(BINDING.packageName, BINDING.className)
                .parameterizedBy(STAR)
        )
        val attributeValues = StringBuilder()

        val constructorParameters = element.visibleGetters
        constructorParameters.forEachIndexed { index, e ->
            attributeValues.append("\t$BINDING_PROPERTY.${e.fieldName}")
            if (index < constructorParameters.size - 1) {
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

    fun getConstructorParametersSpec(element: Element): FunSpec.Builder {
        val packageElement = getPackageOf(element)
        val bindingClassName = "${element.simpleName}Binding"
        return FunSpec.constructorBuilder()
            .addParameter(WIDGET_PROPERTY, ClassName(
                packageElement,
                element.simpleName.toString()
            ))
            .addParameter(BINDING_PROPERTY, ClassName(
                packageElement,
                bindingClassName
            ))
    }

}