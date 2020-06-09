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

import br.com.zup.beagle.compiler.util.ANDROID_CONTEXT
import br.com.zup.beagle.core.BindAttribute
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

private const val HANDLE_METHOD = "handle"
private const val CONTEXT_PROPERTY = "context"

class BeagleActionBindingGenerator(private val processingEnv: ProcessingEnvironment) {
    private val typeUtils = processingEnv.typeUtils

    fun buildActionClassSpec(element: TypeElement, suffix: String,
                             bindClass: KClass<out BindAttribute<*>>): TypeSpec.Builder {
        val typeSpecBuilder = element.visibleGetters.map {
            this.createBindParameter(it,
                bindClass)
        }.let { parameters ->
            TypeSpec.classBuilder("${element.simpleName}${suffix}")
                .superclass(this.typeUtils.getKotlinName(element.superclass))
                .addSuperinterfaces(element.interfaces.map(TypeMirror::asTypeName))
                .primaryConstructor(FunSpec.constructorFrom(parameters))
                .addProperties(parameters.map { PropertySpec.from(it) })
        }
        return typeSpecBuilder
    }

    fun getFunctionHandleSpec(element: TypeElement): FunSpec {

        val attributeValues = StringBuilder()

        val parameters = element.visibleGetters
        parameters.forEachIndexed { index, e ->
            val isNullable = e.isMarkedNullable
            val getValueMethodName = if (isNullable) GET_VALUE_NULL_METHOD else GET_VALUE_NOT_NULL_METHOD
            attributeValues.append("\t${getValueMethodName}(${e.fieldName})")
            if (index < parameters.size - 1) {
                attributeValues.append(",\n")
            }
        }


        return FunSpec.builder(HANDLE_METHOD)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(CONTEXT_PROPERTY, ClassName(ANDROID_CONTEXT.packageName, ANDROID_CONTEXT.className))
            .addStatement("${element.simpleName}(${attributeValues}).${HANDLE_METHOD}(${CONTEXT_PROPERTY})"
                .trimMargin())
            .build()
    }

    private fun createBindParameter(element: ExecutableElement, bindClass: KClass<out BindAttribute<*>>) =
        ParameterSpec.builder(
            element.fieldName,
            bindClass.asTypeName().parameterizedBy(this.typeUtils.getKotlinName(element.returnType))
        ).build()

}