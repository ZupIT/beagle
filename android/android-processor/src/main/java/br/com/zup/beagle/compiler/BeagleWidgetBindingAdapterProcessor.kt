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
import javax.lang.model.element.PackageElement


class BeagleWidgetBindingAdapterProcessor(
    private val processingEnv: ProcessingEnvironment,
    private val beagleSetupRegisteredWidgetGenerator: BeagleSetupRegisteredWidgetGenerator =
        BeagleSetupRegisteredWidgetGenerator(),
    private val beagleSetupPropertyGenerator: BeagleSetupPropertyGenerator =
        BeagleSetupPropertyGenerator(processingEnv)
) {

    fun process(
        basePackageName: String,
        roundEnvironment: RoundEnvironment
    ) {
        val classValues = StringBuilder()
        val registerWidgetAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(RegisterWidget::class.java)

        registerWidgetAnnotatedClasses.forEachIndexed { index, element ->
            classValues.append("\t$element::class.java as Class<WidgetView>")
            if (index < registerWidgetAnnotatedClasses.size - 1) {
                classValues.append(",\n")
            }

            val bindingAdapterClassName = "${element.simpleName}BindingAdapter"
//            processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "bindingAdapterClassName=$bindingAdapterClassName")

            val typeSpec = TypeSpec.classBuilder(bindingAdapterClassName)
                .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
                .primaryConstructor(constructorParameters(element)
                    .build())
                .addProperty(PropertySpec.builder("widget", ClassName(
                    BINDING_ADAPTER.packageName,
                    BINDING_ADAPTER.className
                )).initializer("widget").build())
                .addProperty(addConstructorParameter(element))
                .addSuperinterface(ClassName(
                    BINDING_ADAPTER.packageName,
                    BINDING_ADAPTER.className
                ))
                .addFunction(
                    FunSpec.builder("bindModel")
                        .addModifiers(KModifier.OVERRIDE)
                        .addCode("""
                        TODO()""".trimMargin())
//                        .addStatement("return registeredWidgets")
                        .build()
                )
                .addFunction(
                    getFunction()
                )
                .build()
            val packageElement: PackageElement = processingEnv.elementUtils.getPackageOf(element)
//            processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "packageElement=$packageElement")

            val beagleSetupFile = FileSpec.builder(
                packageElement.toString(),
                bindingAdapterClassName
            ).addType(typeSpec)
                .build()

            try {
                beagleSetupFile.writeTo(processingEnv.filer)
            } catch (e: IOException) {
                val errorMessage = "Error when trying to generate code.\n${e.message!!}"
                processingEnv.messager.error(errorMessage)
            }
        }


    }

    private fun addConstructorParameter(element: Element): PropertySpec {
        val packageElement: PackageElement = processingEnv.elementUtils.getPackageOf(element)
        val bindingClassName = "${element.simpleName}Binding"
        return PropertySpec.builder("binding", ClassName(
            packageElement.toString(),
            bindingClassName
        )).initializer("binding").build()
    }

    private fun getFunction(): FunSpec {

        val returnType = List::class.asClassName().parameterizedBy(
            ClassName(BINDING.packageName, BINDING.className)
                .parameterizedBy(STAR)
        )

        return FunSpec.builder("getBindAttributes")
            .addModifiers(KModifier.OVERRIDE)
            .addCode("""
                        TODO()
                        """.trimMargin())
//            .addStatement("return registeredWidgets")
            .returns(returnType)
            .build()
    }

    private fun constructorParameters(element: Element): FunSpec.Builder {
        val packageElement: PackageElement = processingEnv.elementUtils.getPackageOf(element)
        val bindingClassName = "${element.simpleName}Binding"
        return FunSpec.constructorBuilder()
            .addParameter("widget", ClassName(
                BINDING_ADAPTER.packageName,
                BINDING_ADAPTER.className
            ))
            .addParameter("binding", ClassName(
                packageElement.toString(),
                bindingClassName
            ))
    }
}