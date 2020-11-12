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

const val REGISTERED_WIDGETS = "registeredWidgets"

class BeagleSetupRegisteredWidgetGenerator(private val processingEnv: ProcessingEnvironment) {

    fun generate(roundEnvironment: RoundEnvironment): FunSpec {
        val classesWithAnnotation = getAllClassWithAnnotation(roundEnvironment)

        return createFuncSpec()
            .addCode(getCodeFormatted(classesWithAnnotation))
            .addStatement("return $REGISTERED_WIDGETS")
            .build()
    }

    private fun getAllClassWithAnnotation(roundEnvironment: RoundEnvironment): String {
        val classValues = StringBuilder()

        val registerWidgetAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(RegisterWidget::class.java)

        registerWidgetAnnotatedClasses.forEachIndexed { index, element ->
            validationElement(element)

            classValues.append("\t${element}::class.java as Class<WidgetView>")
            if (index < registerWidgetAnnotatedClasses.size - 1) {
                classValues.append(",\n")
            }
        }

        return classValues.toString()

    }

    private fun validationElement(element: Element) {
        val typeElement = element as TypeElement
        if (!(isValidInheritance(typeElement))) {
            val errorMessage = "The class $element need to inherit from the class ${WIDGET_VIEW.className} " +
                "when annotate class with @RegisterWidget."
            processingEnv.messager.error(typeElement, errorMessage)
        }
    }

    private fun isValidInheritance(typeElement: TypeElement): Boolean {
        return typeElement.extendsFromClass(WIDGET_VIEW.toString())
            || typeElement.extendsFromClass(BEAGLE_INPUT_WIDGET.toString())
            || typeElement.implementsInterface(BEAGLE_PAGE_INDICATOR.toString())
    }

    private fun getCodeFormatted(partOfCode: String): String =
        """
            |val $REGISTERED_WIDGETS = listOf<Class<WidgetView>>(
            |   $partOfCode
            |)
        |""".trimMargin()


    fun createFuncSpec(): FunSpec.Builder {
        val listReturnType = List::class.asClassName().parameterizedBy(
            Class::class.asClassName().parameterizedBy(
                ClassName(WIDGET_VIEW.packageName, WIDGET_VIEW.className)
            )
        )

        return FunSpec.builder(REGISTERED_WIDGETS)
            .returns(listReturnType)
    }
}