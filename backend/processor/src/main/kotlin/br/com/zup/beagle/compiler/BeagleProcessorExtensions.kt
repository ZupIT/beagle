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
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.reflect.KClass

private val TypeName.kotlin get() = JAVA_TO_KOTLIN[this] ?: this
internal val TypeMirror.elementType: TypeMirror get() = if (this is DeclaredType) this.typeArguments[0] else this
internal val TypeMirror.suffixedClassName get() = ClassName.bestGuess("$this$INTERNAL_SUFFIX")

internal val ExecutableElement.fieldName
    get() = this.simpleName.toString().removePrefix(GET).let { it.replaceFirst(it.first(), it.first().toLowerCase()) }

internal fun Types.isLeaf(type: TypeMirror) =
    type.kind.isPrimitive || type.asTypeName() in LEAF_TYPES || this.isSubtype(type, Enum::class)

internal fun Types.isIterable(type: TypeMirror) = this.isSubtype(type, Iterable::class)

internal fun Element.getNameWith(suffix: String = "") = "${this.simpleName}$suffix"

internal fun Elements.getPackageName(element: Element) = this.getPackageOf(element).qualifiedName.toString()

internal fun ClassName.specialize(vararg names: TypeName) = this.parameterizedBy(names.map { it.kotlin })

internal tailrec fun Types.getFinalElementType(type: TypeMirror): TypeMirror =
    if (this.isIterable(type)) this.getFinalElementType(type.elementType) else type

internal fun Types.getKotlinName(type: TypeMirror): TypeName =
    if (this.isIterable(type)) (this.erasure(type).asTypeName().kotlin as ClassName).parameterizedBy(this.getKotlinName(type.elementType))
    else type.asTypeName().kotlin

private fun Types.isSubtype(type: TypeMirror, superType: KClass<*>): Boolean =
    when (this.erasure(type).asTypeName()) {
        Any::class.java.asTypeName() -> false
        superType.java.asTypeName() -> true
        else -> this.directSupertypes(type).any { this.isSubtype(it, superType) }
    }

internal fun Elements.getVisibleGetters(element: TypeElement) =
    this.getAllMembers(element)
        .filter { it.kind == ElementKind.METHOD && it.simpleName matches GETTER && Modifier.PUBLIC in it.modifiers }
        .map { it as ExecutableElement }
