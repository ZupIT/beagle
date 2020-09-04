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

package br.com.zup.beagle.android.compiler

import br.com.zup.beagle.android.annotation.RegisterBeagleAdapter
import br.com.zup.beagle.compiler.BEAGLE_CUSTOM_ADAPTER
import br.com.zup.beagle.compiler.error
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.IOException
import java.lang.reflect.Type
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

const val BEAGLE_ADAPTER_REFERENCE_GENERATED = "TypeAdapterResolverImpl"

class RegisterBeagleAdapterProcessor (private val processingEnv: ProcessingEnvironment) {

    fun process(packageName: String, roundEnvironment: RoundEnvironment) {
        val typeSpec = TypeSpec.classBuilder(BEAGLE_ADAPTER_REFERENCE_GENERATED)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class.java)
                    .addMember("%S", "UNCHECKED_CAST")
                    .build()
            )
            .addSuperinterface(ClassName(
                BEAGLE_CUSTOM_ADAPTER.packageName,
                BEAGLE_CUSTOM_ADAPTER.className
            ))
            .addFunction(createClassForMethod(roundEnvironment))
            .build()

        try {
            FileSpec.builder(packageName, BEAGLE_ADAPTER_REFERENCE_GENERATED)
                .addImport(BEAGLE_CUSTOM_ADAPTER.packageName, BEAGLE_CUSTOM_ADAPTER.className)
                .addType(typeSpec)
                .build()
                .writeTo(processingEnv.filer)
        } catch (e: IOException) {
            val errorMessage = "Error when trying to generate code.\n${e.message!!}"
            processingEnv.messager.error(errorMessage)
        }
    }

    private fun createClassForMethod(roundEnvironment: RoundEnvironment): FunSpec {
        val validatorLines = createValidatorLines(roundEnvironment)
        val returnType = ClassName(BEAGLE_CUSTOM_ADAPTER.packageName, "BeagleTypeAdapter").parameterizedBy(
            TypeVariableName("T")
        ).copy(true) as ParameterizedTypeName

        //ClassName(BEAGLE_CUSTOM_ADAPTER.packageName, "BeagleTypeAdapter")

        val spec = FunSpec.builder("getAdapter")
            .addModifiers(KModifier.OVERRIDE)
            .addTypeVariable(TypeVariableName("T"))
            .addParameter("type", Type::class.asTypeName().copy(false))

        spec.addStatement("""
                |return when(type) {
                |   $validatorLines
                |   else -> null
                |}
            |""".trimMargin())

        return spec.returns(returnType)
            .build()
    }

    private fun createValidatorLines(roundEnvironment: RoundEnvironment): String {
        val adapters = StringBuilder()
        val registerAdapterAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(
            RegisterBeagleAdapter::class.java
        )

        registerAdapterAnnotatedClasses.forEachIndexed { index, element ->
            adapters.append("$element::class.java -> $element() as BeagleTypeAdapter<T>")
        }

        return adapters.toString()
    }
}