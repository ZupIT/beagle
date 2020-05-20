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
import br.com.zup.beagle.compiler.util.WIDGET
import br.com.zup.beagle.compiler.util.WIDGET_VIEW
import br.com.zup.beagle.compiler.util.error
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class BeagleWidgetBindingProcessor(
    private val processingEnv: ProcessingEnvironment
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

    private fun handle(element: Element?) {
        if (element is TypeElement && element.kind.isClass) {
            this.processingEnv.let {
                try {
                    val beagleWidgetBindingHandler = BeagleWidgetBindingHandler(it)
                    val typeSpecBuilder = beagleWidgetBindingHandler.createBindingClass(element)

                    typeSpecBuilder.addProperty(getAttributeWidgetInstance("${element.simpleName}"))
                    typeSpecBuilder.addFunction(getFunctionToView())
                    typeSpecBuilder.addFunction(getFunctionOnBind())
                    val fileSpec = beagleWidgetBindingHandler.getFileSpec(element, typeSpecBuilder.build())
                    fileSpec.writeTo(processingEnv.filer)
                } catch (e: IOException) {
                    val errorMessage = "Error when trying to generate code.\n${e.message!!}"
                    processingEnv.messager.error(errorMessage)
                }

            }
        }
    }

    private fun getFunctionToView(): FunSpec {

        val returnType = ClassName(ANDROID_VIEW.packageName, ANDROID_VIEW.className)

        return FunSpec.builder(TO_VIEW_METHOD)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(CONTEXT_PROPERTY, ClassName(ANDROID_CONTEXT.packageName, ANDROID_CONTEXT.className))
            .addStatement("return $WIDGET_INSTANCE_PROPERTY.$TO_VIEW_METHOD($CONTEXT_PROPERTY)")
            .returns(returnType)
            .build()
    }

    private fun getFunctionOnBind(): FunSpec {

        return FunSpec.builder(ON_BIND_METHOD)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(WIDGET_PROPERTY, ClassName(WIDGET.packageName, WIDGET.className))
            .addStatement("return $WIDGET_INSTANCE_PROPERTY.$ON_BIND_METHOD($WIDGET_PROPERTY)")
            .build()
    }

    private fun getAttributeWidgetInstance(widgetName: String): PropertySpec {
        return PropertySpec.builder(
            WIDGET_INSTANCE_PROPERTY,
            ClassName(WIDGET_VIEW.packageName, WIDGET_VIEW.className)
        ).initializer("$widgetName()").build()
    }

    companion object {
        private const val TO_VIEW_METHOD = "toView"
        private const val ON_BIND_METHOD = "onBind"
        private const val WIDGET_INSTANCE_PROPERTY = "widgetInstance"
        private const val WIDGET_PROPERTY = "widget"
        private const val CONTEXT_PROPERTY = "context"
    }
}