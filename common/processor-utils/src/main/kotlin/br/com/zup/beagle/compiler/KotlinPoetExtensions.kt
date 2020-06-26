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
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

internal val TypeName.kotlin: TypeName get() = JAVA_TO_KOTLIN[this] ?: this

fun ClassName.specialize(vararg names: TypeName): ParameterizedTypeName = this.parameterizedBy(names.map { it.kotlin })

fun FunSpec.Companion.constructorFrom(parameters: List<ParameterSpec>): FunSpec =
    this.constructorBuilder().addParameters(parameters).build()

fun PropertySpec.Companion.from(parameter: ParameterSpec, needsOverride: Boolean = false): PropertySpec =
    this.builder(parameter.name, parameter.type).initializer(parameter.name)
        .let { if (needsOverride) it.addModifiers(KModifier.OVERRIDE) else it }
        .build()

fun TypeElement.implementsInterface(interfaceName: String): Boolean {
    for (interfaceTypeMirror in this.interfaces) {
        val typeMirror = ((interfaceTypeMirror as DeclaredType)).asElement()
        if (typeMirror.toString() == interfaceName) {
            return true
        }
    }
    return false
}

fun TypeElement.extendsFromClass(className: String): Boolean {
    val typeMirror = ((this.superclass as DeclaredType)).asElement()
    if (typeMirror.toString() == className) {
        return true
    }
    return false
}