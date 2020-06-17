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
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asTypeName
import org.jetbrains.annotations.Nullable
import javax.lang.model.AnnotatedConstruct
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

private val TypeName.kotlin: TypeName get() = JAVA_TO_KOTLIN[this] ?: this

val AnnotatedConstruct.isMarkedNullable: Boolean get() = this.getAnnotation(Nullable::class.java) != null
val ExecutableElement.isOverride: Boolean get() = this.getAnnotation(Override::class.java) != null

val ExecutableElement.fieldName: String
    get() = this.simpleName.toString()
        .removePrefix(GET)
        .takeWhile { it != INTERNAL_MARKER }
        .let { it.replaceFirst(it.first(), it.first().toLowerCase()) }

val TypeElement.visibleGetters: List<ExecutableElement>
    get() = this.enclosedElements.filter { it.kind.isField }.map { it.simpleName.toString() }.toSet().let { names ->
        this.enclosedElements
            .filter { it.kind == ElementKind.METHOD && GET in it.simpleName && Modifier.PUBLIC in it.modifiers }
            .filterIsInstance<ExecutableElement>()
            .filter { it.fieldName in names }
    }

fun Types.isSubtype(type: TypeMirror, superTypeName: String): Boolean =
    when (this.erasure(type).asTypeName().toString()) {
        Any::class.java.canonicalName -> false
        superTypeName -> true
        else -> this.directSupertypes(type).any { this.isSubtype(it, superTypeName) }
    }

fun Elements.getPackageAsString(element: Element): String = this.getPackageOf(element).toString()

fun FunSpec.Companion.constructorFrom(parameters: List<ParameterSpec>) =
    this.constructorBuilder().addParameters(parameters).build()

fun PropertySpec.Companion.from(parameter: ParameterSpec, needsOverride: Boolean = false) =
    this.builder(parameter.name, parameter.type).initializer(parameter.name)
        .let { if (needsOverride) it.addModifiers(KModifier.OVERRIDE) else it }
        .build()

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
