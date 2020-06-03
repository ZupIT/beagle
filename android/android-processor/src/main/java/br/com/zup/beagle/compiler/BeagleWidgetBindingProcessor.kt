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
import br.com.zup.beagle.compiler.util.ANDROID_CONTEXT
import br.com.zup.beagle.compiler.util.ANDROID_VIEW
import br.com.zup.beagle.compiler.util.BIND
import br.com.zup.beagle.compiler.util.BINDING_ADAPTER
import br.com.zup.beagle.compiler.util.GET_VALUE_NOT_NULL
import br.com.zup.beagle.compiler.util.GET_VALUE_NULL
import br.com.zup.beagle.compiler.util.INPUT_WIDGET
import br.com.zup.beagle.compiler.util.WIDGET
import br.com.zup.beagle.compiler.util.error
import br.com.zup.beagle.compiler.util.warning
import br.com.zup.beagle.core.BindAttribute
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class BeagleWidgetBindingProcessor(
    private val processingEnv: ProcessingEnvironment,
    private val beagleWidgetBindingGenerator: BeagleWidgetBindingGenerator =
        BeagleWidgetBindingGenerator(
            processingEnv
        )
) {

    fun process(
        roundEnvironment: RoundEnvironment
    ) {
        val registerWidgetAnnotatedClasses = roundEnvironment
            .getElementsAnnotatedWith(RegisterWidget::class.java)
        registerWidgetAnnotatedClasses.forEach { element ->
            handle(element)
        }

    }

    private fun handle(element: Element) {
        if (element is TypeElement && element.kind.isClass) {
            try {
                val beagleWidgetBindingHandler = BeagleWidgetBindingHandler(processingEnv, bindClass = Class.forName(
                    BIND.toString()
                ).kotlin as KClass<out BindAttribute<*>>)
                val typeSpecBuilder = beagleWidgetBindingHandler.createBindingClass(element)

                typeSpecBuilder.addProperty(getAttributeWidgetInstance(element))
                typeSpecBuilder.addProperty(getAttributeView())
                typeSpecBuilder.addFunction(getFunctionBuildView())
                typeSpecBuilder.addFunction(getFunctionOnBind())
                    .addFunction(
                        beagleWidgetBindingGenerator.getFunctionBindModel(element)
                    )
                    .addFunction(
                        beagleWidgetBindingGenerator.getFunctionGetBindAttributes(element)
                    ).addSuperinterface(ClassName(
                        BINDING_ADAPTER.packageName,
                        BINDING_ADAPTER.className
                    ))

                if (isInputWidget(element))
                    addInputWidgetMethods(typeSpecBuilder)

                val typeSpec = typeSpecBuilder.build()
                val fileSpec = FileSpec.builder(
                    processingEnv.elementUtils.getPackageAsString(element),
                    "${element.simpleName}${BeagleWidgetBindingHandler.SUFFIX}"
                ).addImport(GET_VALUE_NULL.packageName, GET_VALUE_NULL.className)
                    .addImport(GET_VALUE_NOT_NULL.packageName, GET_VALUE_NOT_NULL.className)
                    .addImport(ANDROID_VIEW.packageName, ANDROID_VIEW.className)
                    .addType(typeSpec)
                    .build()

                fileSpec.writeTo(processingEnv.filer)
            } catch (e: IOException) {
                val errorMessage = "Error when trying to generate code.\n${e.message!!}"
                processingEnv.messager.error(errorMessage)
            }
        } else {
            processingEnv.messager.warning(element, "Skipped element $element.simpleName")
        }
    }

    private fun addInputWidgetMethods(typeSpecBuilder: TypeSpec.Builder) {
        typeSpecBuilder.addFunction(
            beagleWidgetBindingGenerator.getFunctionRetrieveValue()
        )
            .addFunction(beagleWidgetBindingGenerator.getFunctionOnErrorMessage())
    }

    private fun isInputWidget(element: TypeElement) =
        processingEnv.typeUtils.isSubtype(element.asType(), INPUT_WIDGET.toString())

    private fun getFunctionBuildView(): FunSpec {

        val returnType = ClassName(ANDROID_VIEW.packageName, ANDROID_VIEW.className)

        return FunSpec.builder(BUILD_VIEW_METHOD)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(CONTEXT_PROPERTY, ClassName(ANDROID_CONTEXT.packageName, ANDROID_CONTEXT.className))
            .addStatement("""
                |   this.$VIEW_PROPERTY = $WIDGET_INSTANCE_PROPERTY.buildView(context)
                |   bindModel()
                |return this.$VIEW_PROPERTY""".trimMargin())
            .returns(returnType)
            .build()
    }

    private fun getFunctionOnBind(): FunSpec {

        return FunSpec.builder(ON_BIND_METHOD)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(WIDGET_PROPERTY, ClassName(WIDGET.packageName, WIDGET.className))
            .addParameter(VIEW_PROPERTY, ClassName(ANDROID_VIEW.packageName, ANDROID_VIEW.className))
            .addStatement("return $WIDGET_INSTANCE_PROPERTY.$ON_BIND_METHOD($WIDGET_PROPERTY, $VIEW_PROPERTY)")
            .build()
    }

    private fun getAttributeWidgetInstance(element: TypeElement): PropertySpec {
        return PropertySpec.builder(
            WIDGET_INSTANCE_PROPERTY,
            ClassName(
                processingEnv.elementUtils.getPackageAsString(element),
                "${element.simpleName}"
            )
        ).addAnnotation(Transient::class).initializer("${element.simpleName}()").build()
    }

    private fun getAttributeView(): PropertySpec {
        return PropertySpec.builder(
            name = VIEW_PROPERTY,
            type = ClassName(
                ANDROID_VIEW.packageName,
                ANDROID_VIEW.className
            ),
            modifiers = *arrayOf(KModifier.LATEINIT)
        ).mutable(true).addAnnotation(Transient::class).build()
    }

    companion object {
        private const val BUILD_VIEW_METHOD = "buildView"
        private const val ON_BIND_METHOD = "onBind"
        private const val VIEW_PROPERTY = "view"
        private const val WIDGET_PROPERTY = "widget"
        private const val CONTEXT_PROPERTY = "context"
    }
}