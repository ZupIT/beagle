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

import br.com.zup.beagle.annotation.RegisterWidget
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class GenerateFunctionWidget(private val processingEnv: ProcessingEnvironment) :
    BeagleGeneratorFunction<RegisterWidget>(
        WIDGET_VIEW,
        REGISTERED_WIDGETS,
        RegisterWidget::class.java
    ) {

    override fun buildCodeByElement(element: Element, annotation: Annotation): String {
        return "\n\t${element}::class.java as Class<WidgetView>,"
    }

    override fun validationElement(element: Element, annotation: Annotation) {
        val typeElement = element as TypeElement
        if (!isValidInheritance(typeElement)) {
            val errorMessage = "The class $element need to inherit from the class ${WIDGET_VIEW.className} " +
                "when annotate class with @RegisterWidget."
            processingEnv.messager.error(typeElement, errorMessage)
        }
    }

    override fun returnStatementInGenerate(): String = "return $REGISTERED_WIDGETS"

    override fun getCodeFormatted(allCodeMappedWithAnnotation: String): String =
        """
            |val $REGISTERED_WIDGETS = listOf<Class<WidgetView>>(
            |   $allCodeMappedWithAnnotation
            |)
        |""".trimMargin()

    override fun generate(roundEnvironment: RoundEnvironment): FunSpec {
        val dependenciesRegisteredWidgets = getDependenciesRegisteredWidgets()
        val classesWithAnnotation = getAllClassWithAnnotation(roundEnvironment)
        return createFuncSpec(getFunctionName())
            .addCode(getCodeFormatted(classesWithAnnotation + dependenciesRegisteredWidgets))
            .addStatement(returnStatementInGenerate())
            .build()
    }

    private fun getDependenciesRegisteredWidgets(): StringBuilder {
        val test = 1
        val registeredComponents = StringBuilder()
        processingEnv.elementUtils.getPackageElement(REGISTRAR_COMPONENTS_PACKAGE)?.enclosedElements?.forEach {
            val fullClassName = it.toString()
            val cls = Class.forName(fullClassName)
            val kotlinClass = cls.kotlin
            (cls.getMethod("registeredComponents").invoke(kotlinClass.objectInstance) as List<Pair<String, String>>).forEach { component ->
                registeredComponents.append("\n\t${component.first}.${component.second}::class.java as Class<WidgetView>,")
            }
        }
        return registeredComponents
    }


    override fun createFuncSpec(name: String): FunSpec.Builder {
        val listReturnType = List::class.asClassName().parameterizedBy(
            Class::class.asClassName().parameterizedBy(
                ClassName(WIDGET_VIEW.packageName, WIDGET_VIEW.className)
            )
        )

        return FunSpec.builder(name)
            .returns(listReturnType)
    }

    private fun isValidInheritance(typeElement: TypeElement): Boolean {
        return typeElement.implements(WIDGET_VIEW, processingEnv)
            || typeElement.implements(BEAGLE_INPUT_WIDGET, processingEnv)
            || typeElement.implements(BEAGLE_PAGE_INDICATOR, processingEnv)
    }

    companion object {
        const val REGISTERED_WIDGETS = "registeredWidgets"
    }
}