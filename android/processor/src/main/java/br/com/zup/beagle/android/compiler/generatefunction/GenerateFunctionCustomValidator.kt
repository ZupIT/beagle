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

import br.com.zup.beagle.android.annotation.RegisterValidator
import br.com.zup.beagle.android.compiler.BeagleSetupProcessor.Companion.REGISTERED_CUSTOM_VALIDATOR_GENERATED
import br.com.zup.beagle.android.compiler.VALIDATOR
import br.com.zup.beagle.android.compiler.VALIDATOR_HANDLER
import br.com.zup.beagle.compiler.shared.BeagleGeneratorFunction
import br.com.zup.beagle.compiler.shared.error
import br.com.zup.beagle.compiler.shared.implementsInterface
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class GenerateFunctionCustomValidator(private val processingEnv: ProcessingEnvironment) :
    BeagleGeneratorFunction<RegisterValidator>(
        VALIDATOR_HANDLER,
        REGISTERED_CUSTOM_VALIDATOR_GENERATED,
        RegisterValidator::class.java
    ) {

    private var allCodeMappedWithAnnotation = ""

    override fun buildCodeByElement(element: Element, annotation: Annotation): String {
        val name = (annotation as RegisterValidator).name
        return "\"$name\" -> $element() as Validator<Any, Any>\n"
    }

    override fun validationElement(element: Element, annotation: Annotation) {
        val typeElement = element as TypeElement
        if (!typeElement.implementsInterface(VALIDATOR.toString())) {
            val errorMessage = "The class $element need to inherit from the class " +
                "${VALIDATOR.className} when annotate class with @RegisterValidator."
            processingEnv.messager.error(typeElement, errorMessage)
        }
    }

    override fun returnStatementInGenerate(): String = """
         |return when(name) {
                |   $allCodeMappedWithAnnotation
                |   else -> null
                |}
        |""".trimMargin()

    override fun getCodeFormatted(allCodeMappedWithAnnotation: String): String {
        this.allCodeMappedWithAnnotation = allCodeMappedWithAnnotation
        return ""
    }

    override fun createFuncSpec(name: String): FunSpec.Builder {
        val returnType = ClassName(VALIDATOR.packageName, VALIDATOR.className)
            .parameterizedBy(Any::class.asClassName(), Any::class.asClassName())
            .copy(nullable = true)

        return FunSpec.builder(REGISTERED_CUSTOM_VALIDATOR)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("name", String::class)
            .returns(returnType)
    }

    companion object {
        const val REGISTERED_CUSTOM_VALIDATOR = "getValidator"
    }
}