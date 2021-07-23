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

package br.com.zup.beagle.android.compiler.generatefunction

import br.com.zup.beagle.compiler.shared.BeagleGeneratorFunction
import br.com.zup.beagle.compiler.shared.RegisteredComponentFullName
import br.com.zup.beagle.compiler.shared.RegisteredComponentId
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.PackageElement

class GenerateFunctionComponentRegistrar<T : Annotation>(
    processingEnv: ProcessingEnvironment,
    functionName: String,
    annotation: Class<T>,
    private val buildElementKey: (Element, Annotation) -> String,
) : BeagleGeneratorFunction<T>(
    processingEnv,
    functionName,
    annotation,
) {
    companion object {
        const val PROPERTY_NAME = "registeredComponents"
    }

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

    override fun buildCodeByElement(element: Element, annotation: Annotation): String {
        return "\n\tPair(\"\"\"${buildElementKey(element, annotation)}\"\"\"," +
            "\"${getPackageName(element)}.${element.simpleName}\"),"
    }

    private fun getPackageName(element: Element): String {
        var enclosing = element
        while (enclosing.kind != ElementKind.PACKAGE) {
            enclosing = enclosing.enclosingElement
        }
        val packageElement = enclosing as PackageElement
        return packageElement.toString()
    }

    override fun returnStatementInGenerate(): String = "return $PROPERTY_NAME"

    override fun getCodeFormatted(allCodeMappedWithAnnotation: String): String =
        """
            |val $PROPERTY_NAME = listOf<Pair<String, String>>(
            |   $allCodeMappedWithAnnotation
            |)
        |""".trimMargin()

    override fun validationElement(element: Element, annotation: Annotation) {
        // Nothing to do here.
    }

    override fun buildCodeByDependency(
        registeredDependency: Pair<RegisteredComponentId, RegisteredComponentFullName>
    ) = ""
}