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

import br.com.zup.beagle.android.compiler.ANDROID_ACTION
import br.com.zup.beagle.annotation.RegisterAction
import br.com.zup.beagle.compiler.shared.BeagleGeneratorFunction
import br.com.zup.beagle.compiler.shared.RegisteredComponentFullName
import br.com.zup.beagle.compiler.shared.RegisteredComponentId
import br.com.zup.beagle.compiler.shared.RegistrarComponentsProvider
import br.com.zup.beagle.compiler.shared.error
import br.com.zup.beagle.compiler.shared.implements
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class GenerateFunctionAction(
    processingEnv: ProcessingEnvironment,
    registrarComponentsProvider: RegistrarComponentsProvider? = null,
) : BeagleGeneratorFunction<RegisterAction>(
    processingEnv,
    REGISTERED_ACTIONS,
    RegisterAction::class.java,
    registrarComponentsProvider,
) {
    override fun buildCodeByElement(element: Element, annotation: Annotation): String {
        return buildCode(element.toString())
    }

    override fun validationElement(element: Element, annotation: Annotation) {
        val typeElement = element as TypeElement
        if (!isValidInheritance(typeElement)) {
            val errorMessage = "The class $element need to inherit from the class ${ANDROID_ACTION.className} " +
                "when annotate class with @RegisterAction."
            processingEnv.messager.error(typeElement, errorMessage)
        }
    }

    override fun returnStatementInGenerate(): String = "return $REGISTERED_ACTIONS"

    override fun getCodeFormatted(allCodeMappedWithAnnotation: String): String =
        """
            |val $REGISTERED_ACTIONS = listOf<Class<Action>>(
            |   $allCodeMappedWithAnnotation
            |)
        |""".trimMargin()

    override fun createFuncSpec(name: String): FunSpec.Builder {
        val listReturnType = List::class.asClassName().parameterizedBy(
            Class::class.asClassName().parameterizedBy(
                ClassName(ANDROID_ACTION.packageName, ANDROID_ACTION.className)
            )
        )

        return FunSpec.builder(name)
            .returns(listReturnType)
    }

    private fun isValidInheritance(typeElement: TypeElement): Boolean {
        return typeElement.implements(ANDROID_ACTION, processingEnv)
    }

    companion object {
        const val REGISTERED_ACTIONS = "registeredActions"
        const val REGISTERED_ACTIONS_SUFFIX = "::class.java as Class<Action>"
    }

    override fun buildCodeByDependency(
        registeredDependency: Pair<RegisteredComponentId, RegisteredComponentFullName>
    ): String {
        return buildCode(registeredDependency.second)
    }

    private fun buildCode(elementDescription: String): String {
        return "\n\t$elementDescription$REGISTERED_ACTIONS_SUFFIX,"
    }
}