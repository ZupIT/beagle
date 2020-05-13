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

import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.compiler.util.BINDING
import br.com.zup.beagle.compiler.util.BINDING_ADAPTER
import br.com.zup.beagle.compiler.util.GET_VALUE
import br.com.zup.beagle.compiler.util.error
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.PackageElement

class BeagleWidgetBindingAdapterProcessor(
    private val processingEnv: ProcessingEnvironment
) {

    fun process(
        roundEnvironment: RoundEnvironment
    ) {
        val registerWidgetAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(RegisterWidget::class.java)
        registerWidgetAnnotatedClasses.forEachIndexed { _, element ->

            val bindingAdapterClassName = "${element.simpleName}BindingAdapter"

            val typeSpec = TypeSpec.classBuilder(bindingAdapterClassName)
                .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
                .primaryConstructor(constructorParameters(element)
                    .build())
                .addProperty(propertyWidget(element))
                .addProperty(propertyBinding(element))
                .addSuperinterface(ClassName(
                    BINDING_ADAPTER.packageName,
                    BINDING_ADAPTER.className
                ))
                .addFunction(
                    functionBindModel(element)
                )
                .addFunction(
                    functionGetBindAttributes(element)
                )
                .build()
            val packageElement: PackageElement = processingEnv.elementUtils.getPackageOf(element)

            val beagleSetupFile = FileSpec.builder(
                packageElement.toString(),
                bindingAdapterClassName
            ).addImport(GET_VALUE.packageName, GET_VALUE.className).addType(typeSpec)
                .build()

            try {
                beagleSetupFile.writeTo(processingEnv.filer)
            } catch (e: IOException) {
                val errorMessage = "Error when trying to generate code.\n${e.message!!}"
                processingEnv.messager.error(errorMessage)
            }
        }


    }

    private fun propertyBinding(element: Element) = addConstructorParameter(element)

    private fun propertyWidget(element: Element): PropertySpec {
        val packageElement: PackageElement = processingEnv.elementUtils.getPackageOf(element)
        val bindingClassName = "${element.simpleName}"
        return PropertySpec.builder("widget", ClassName(
            packageElement.toString(),
            bindingClassName
        )).initializer("widget").build()
    }

    private fun functionBindModel(element: Element): FunSpec {
        val attributeValues = StringBuilder()
        val notifyValues = StringBuilder()

        val constructorParameters = getConstructorParameters(element)
        constructorParameters.forEachIndexed { index, e ->
            attributeValues.append("\t${e.simpleName} = getValue(binding.${e.simpleName}, widget.${e.simpleName})")
            if (index < constructorParameters.size - 1) {
                attributeValues.append(",\n")
            }
        }

        constructorParameters.forEachIndexed { index, e ->
            notifyValues.append("""
                |binding.${e.simpleName}.observes {
                |myWidget = myWidget.copy(${e.simpleName} = it)
                |   widget.onBind(myWidget)
                |}
                |
            """.trimMargin())
        }
        return FunSpec.builder("bindModel")
            .addModifiers(KModifier.OVERRIDE)
            .addCode("""
                |var myWidget = ${element.simpleName}(
                |$attributeValues
                |)
                |widget.onBind(myWidget)
                |$notifyValues
                        |""".trimMargin())
            .build()
    }

    private fun addConstructorParameter(element: Element): PropertySpec {
        val packageElement: PackageElement = processingEnv.elementUtils.getPackageOf(element)
        val bindingClassName = "${element.simpleName}Binding"
        return PropertySpec.builder("binding", ClassName(
            packageElement.toString(),
            bindingClassName
        )).initializer("binding").build()
    }

    private fun functionGetBindAttributes(element: Element): FunSpec {

        val returnType = List::class.asClassName().parameterizedBy(
            ClassName(BINDING.packageName, BINDING.className)
                .parameterizedBy(STAR)
        )
        val attributeValues = StringBuilder()

        val constructorParameters = getConstructorParameters(element)
        constructorParameters.forEachIndexed { index, e ->
            attributeValues.append("\tbinding.${e.simpleName}")
            if (index < constructorParameters.size - 1) {
                attributeValues.append(",\n")
            }
        }

        return FunSpec.builder("getBindAttributes")
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

    private fun getConstructorParameters(element: Element) =
        (element.enclosedElements.first { it.kind == ElementKind.CONSTRUCTOR } as ExecutableElement).parameters

    private fun constructorParameters(element: Element): FunSpec.Builder {
        val packageElement: PackageElement = processingEnv.elementUtils.getPackageOf(element)
        val bindingClassName = "${element.simpleName}Binding"
        return FunSpec.constructorBuilder()
            .addParameter("widget", ClassName(
                packageElement.toString(),
                element.simpleName.toString()
            ))
            .addParameter("binding", ClassName(
                packageElement.toString(),
                bindingClassName
            ))
    }
}