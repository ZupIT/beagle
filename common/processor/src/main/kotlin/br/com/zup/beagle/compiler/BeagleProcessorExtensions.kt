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

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.WildcardType
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

private val TypeName.kotlin get() = JAVA_TO_KOTLIN[this] ?: this

val ProcessingEnvironment.kaptGeneratedDirectory get() = File(this.options[KAPT_KEY]!!)

val ExecutableElement.fieldName
    get() = this.simpleName.toString()
        .removePrefix(GET)
        .takeWhile { it != INTERNAL_MARKER }
        .let { it.replaceFirst(it.first(), it.first().toLowerCase()) }

val TypeElement.visibleGetters
    get() = this.enclosedElements
        .filter { it.kind == ElementKind.METHOD && GET in it.simpleName && Modifier.PUBLIC in it.modifiers }
        .map { it as ExecutableElement }

fun Elements.getPackageAsString(element: Element) = this.getPackageOf(element).toString()

fun PropertySpec.Companion.from(parameter: ParameterSpec) =
    this.builder(parameter.name, parameter.type).initializer(parameter.name).build()

fun FunSpec.Companion.constructorFrom(parameters: List<ParameterSpec>) =
    this.constructorBuilder().addParameters(parameters).build()

fun Types.getKotlinName(type: TypeMirror): TypeName = when {
    type is DeclaredType && !type.typeArguments.isNullOrEmpty() ->
        (this.erasure(type).asTypeName().kotlin as ClassName)
            .parameterizedBy(type.typeArguments.map { this.getKotlinName(it) })
    type is WildcardType -> when {
        type.extendsBound != null -> WildcardTypeName.producerOf(this.getKotlinName(type.extendsBound))
        type.superBound != null -> WildcardTypeName.consumerOf(this.getKotlinName(type.superBound))
        else -> STAR
    }
    else -> type.asTypeName().kotlin
}
