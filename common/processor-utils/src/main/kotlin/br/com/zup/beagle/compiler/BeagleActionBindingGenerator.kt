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
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

private const val HANDLE_METHOD = "execute"
private const val ROOT_VIEW_PROPERTY = "rootView"

class BeagleActionBindingGenerator(processingEnv: ProcessingEnvironment) : BeagleBindingHandler(
    processingEnv) {
    private val typeUtils = processingEnv.typeUtils

    fun buildActionClassSpec(element: TypeElement, suffix: String): TypeSpec.Builder {
        val typeSpecBuilder = element.visibleGetters.map {
            createBindParameter(it)
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
            val getValueMethodName = GET_VALUE_NOT_NULL_METHOD
            attributeValues.append("${e.fieldName} = ${getValueMethodName}(${e.fieldName})")
            if (index < parameters.size - 1) {
                attributeValues.append(",\n")
            }
        }


        return FunSpec.builder(HANDLE_METHOD)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(ROOT_VIEW_PROPERTY, ClassName(ROOT_VIEW.packageName, ROOT_VIEW.className))
            .addStatement("${element.simpleName}(${attributeValues}).$HANDLE_METHOD($ROOT_VIEW_PROPERTY)"
                .trimMargin())
            .build()
    }
}