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
import br.com.zup.beagle.compiler.elementType
import br.com.zup.beagle.compiler.error
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.IOException
import java.lang.reflect.Type
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

const val BEAGLE_ADAPTER_REFERENCE_GENERATED = "TypeAdapterResolverImpl"
const val JAVA_CLASS = "::class.java"
const val BEAGLE_TYPE_ADAPTER_INTERFACE = "BeagleTypeAdapter<T>"
const val TYPES_INSTANCE = "Types.newParameterizedType(\n       "
const val T_GENERIC = "T"
const val BREAK_LINE = ",\n       "

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
                .addImport("com.squareup.moshi", "Types")
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
        val validatorLines = createValidatorLines(roundEnvironment, StringBuilder())
        val returnType = ClassName(BEAGLE_CUSTOM_ADAPTER.packageName, "BeagleTypeAdapter").parameterizedBy(
            TypeVariableName(T_GENERIC)
        ).copy(true) as ParameterizedTypeName

        val spec = FunSpec.builder("getAdapter")
            .addModifiers(KModifier.OVERRIDE)
            .addTypeVariable(TypeVariableName(T_GENERIC))
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

    private fun createValidatorLines(roundEnvironment: RoundEnvironment, adapters: StringBuilder): String {
        val registerAdapterAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(
            RegisterBeagleAdapter::class.java
        )

        registerAdapterAnnotatedClasses.forEach { element ->
            val typeElement = element as TypeElement
            val declaredType = typeElement.interfaces[0] as DeclaredType
            val elementParameterizedTypeName = ((typeElement.interfaces[0] as DeclaredType).elementType as DeclaredType).asElement()

            if (typeElement.interfaces.size == 1) {
                if (declaredType.toTypeArguments().isEmpty()) {
                    adapters.append("$elementParameterizedTypeName$JAVA_CLASS -> $element() as $BEAGLE_TYPE_ADAPTER_INTERFACE\n")
                } else {
                    adapters.append("$TYPES_INSTANCE$elementParameterizedTypeName$JAVA_CLASS")
                    callRecursive(adapters, declaredType, element)
                }
            } else if (typeElement.interfaces.size > 1) {
                processingEnv.messager.error("Error: $element must implement just the $BEAGLE_TYPE_ADAPTER_INTERFACE")
            } else {
                processingEnv.messager.error("Error: $element must implement the $BEAGLE_TYPE_ADAPTER_INTERFACE")
            }
        }

        return adapters.toString()
    }

    private fun callRecursive(adapters: StringBuilder, declaredType: DeclaredType, element: TypeElement) {
        declaredType.toTypeArguments().forEach { item ->

            if (item is DeclaredType) {
                val typeArgumentsItem = item.toTypeArguments()

                if (typeArgumentsItem.isEmpty()) {
                    adapters.append("$BREAK_LINE${item.toString().removeExtends()}$JAVA_CLASS")
                } else {
                    adapters.append("$TYPES_INSTANCE${item.toString().removeExtends()}$JAVA_CLASS$BREAK_LINE")
                    callRecursive(adapters, item, element)
                }
            } else {
                adapters.append("$BREAK_LINE${item.toString().removeExtends()}$JAVA_CLASS")
            }
        }

        adapters.append("\n   ) -> $element() as $BEAGLE_TYPE_ADAPTER_INTERFACE\n")
    }
}

private fun DeclaredType.toTypeArguments() : List<TypeMirror> {
    return try {
        (this.elementType as DeclaredType).typeArguments
    } catch (e: Exception) {
        ArrayList()
    }
}

private fun String.removeExtends() = this.replace("? extends ", "")
