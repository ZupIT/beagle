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
import javax.lang.model.element.ElementKind
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement

class GenerateFunctionWidgetRegistrar(private val processingEnv: ProcessingEnvironment) :
    BeagleGeneratorFunction<RegisterWidget>(
        WIDGET_VIEW,
        REGISTERED_COMPONENTS,
        RegisterWidget::class.java
    ) {

    override fun buildCodeByElement(element: Element, annotation: Annotation): String {
        return "\n\tPair(\"${getPackageName(element)}\",\"${element.simpleName}\"),"
    }

    private fun getPackageName(element: Element): String {
        var enclosing = element
        while (enclosing.kind != ElementKind.PACKAGE) {
            enclosing = enclosing.enclosingElement
        }
        val packageElement = enclosing as PackageElement
        return packageElement.toString()
    }

    override fun validationElement(element: Element, annotation: Annotation) {
        val typeElement = element as TypeElement
        if (!isValidInheritance(typeElement)) {
            val errorMessage = "The class $element need to inherit from the class ${WIDGET_VIEW.className} " +
                "when annotate class with @RegisterWidget."
            processingEnv.messager.error(typeElement, errorMessage)
        }
    }

    override fun returnStatementInGenerate(): String = "return $REGISTERED_COMPONENTS"

    override fun getCodeFormatted(allCodeMappedWithAnnotation: String): String =
        """
            |val $REGISTERED_COMPONENTS = listOf<Pair<String, String>>(
            |   $allCodeMappedWithAnnotation
            |)
        |""".trimMargin()

    override fun createFuncSpec(name: String): FunSpec.Builder {
        val listReturnType = List::class.asClassName().parameterizedBy(
            Pair::class.asClassName().parameterizedBy(
                ClassName("kotlin", "String"),
                ClassName("kotlin", "String"),
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
        const val REGISTERED_COMPONENTS = "registeredComponents"
    }
}