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
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

private val TypeName.kotlin get() = JAVA_TO_KOTLIN[this] ?: this as ClassName

val Element.enclosedFields get() = this.enclosedElements.filter { it.kind.isField }
val ProcessingEnvironment.kaptGeneratedDirectory get() = File(this.options[KAPT_KEY]!!)

fun Elements.getPackageAsString(element: Element) = this.getPackageOf(element).toString()

fun PropertySpec.Companion.from(parameter: ParameterSpec) =
    this.builder(parameter.name, parameter.type).initializer(parameter.name).build()

fun FunSpec.Companion.constructorFrom(parameters: List<ParameterSpec>) =
    this.constructorBuilder().addParameters(parameters).build()

fun Types.getKotlinName(type: TypeMirror): TypeName =
    if (type is DeclaredType && !type.typeArguments.isNullOrEmpty()) {
        this.erasure(type).asTypeName().kotlin.parameterizedBy(type.typeArguments.map { this.getKotlinName(it) })
    } else {
        type.asTypeName().kotlin
    }
