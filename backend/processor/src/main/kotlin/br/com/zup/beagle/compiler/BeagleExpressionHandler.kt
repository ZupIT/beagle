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

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

internal class BeagleExpressionHandler(processingEnvironment: ProcessingEnvironment) {
    private val outputDirectory = processingEnvironment.kaptGeneratedDirectory
    private val elementUtils = processingEnvironment.elementUtils
    private val typeUtils = processingEnvironment.typeUtils
    private val visited = mutableSetOf<TypeMirror>()

    fun handle(element: TypeElement) = this.handle(element, ROOT_SUFFIX)

    private fun handle(element: TypeElement, suffix: String) {
        this.createMainGenerator(element, suffix)
        this.createSubGenerators(element)
    }

    private fun createMainGenerator(element: TypeElement, suffix: String) =
        FileSpec.get(this.elementUtils.getPackageAsString(element), this.createType(element, suffix))
            .writeTo(this.outputDirectory)

    private fun createType(element: TypeElement, suffix: String) =
        this.createTypeBuilder(element, suffix)
            .addProperties(this.createGenerationProperties(element, suffix == ROOT_SUFFIX))
            .build()

    private fun createTypeBuilder(element: TypeElement, suffix: String) =
        if (suffix == ROOT_SUFFIX) {
            TypeSpec.objectBuilder(element.getNameWith(suffix))
        } else {
            TypeSpec.classBuilder(element.getNameWith(suffix))
                .primaryConstructor(this.createSubexpressionConstructor(element))
        }

    private fun createSubexpressionConstructor(element: TypeElement) =
        FunSpec.constructorBuilder().addParameter(PARAMETER, EXPRESSION.specialize(element.asClassName())).build()

    private fun createGenerationProperties(element: TypeElement, isRoot: Boolean) =
        this.elementUtils.getVisibleGetters(element).map { this.createGenerationProperty(it, isRoot) }

    private fun createGenerationProperty(element: ExecutableElement, isRoot: Boolean) =
        PropertySpec.builder(element.fieldName, this.createGenerationType(element.returnType))
            .let {
                if (element.returnType in this.visited) {
                    it.delegate("lazy { ${this.createGenerationCode(element, isRoot)} }")
                } else {
                    it.initializer(this.createGenerationCode(element, isRoot))
                }
            }.build()

    private fun createGenerationType(fieldType: TypeMirror): TypeName = when {
        this.typeUtils.isLeaf(fieldType) -> BIND.specialize(fieldType.asTypeName())
        this.typeUtils.isIterable(fieldType) ->
            fieldType.elementType.let {
                LIST_SUBEXPRESSION.parameterizedBy(this.typeUtils.getKotlinName(it), this.createGenerationType(it))
            }
        else -> fieldType.suffixedClassName
    }

    private fun createGenerationCode(element: ExecutableElement, isRoot: Boolean) =
        CodeBlock.of(
            this.createGenerationFormat(element.returnType),
            CodeBlock.of("${if (isRoot) "%T" else "%L"}(%S)", if (isRoot) START else ACCESS, element.fieldName)
        )

    private fun createGenerationFormat(fieldType: TypeMirror, depth: Int = 0): String = when {
        this.typeUtils.isLeaf(fieldType) -> "$BIND_EXPRESSION($PLACEHOLDER)"
        this.typeUtils.isIterable(fieldType) ->
            this.createGenerationFormat(fieldType.elementType, depth + 1)
                .replace(PLACEHOLDER, "$ITERABLE_PARAM$depth")
                .let { "${LIST_SUBEXPRESSION.simpleName}($PLACEHOLDER) { $ITERABLE_PARAM$depth -> $it }" }
        else -> "${fieldType.suffixedClassName.simpleName}($PLACEHOLDER)"
    }

    private fun createSubGenerators(element: TypeElement) =
        this.elementUtils.getVisibleGetters(element)
            .map { this.typeUtils.getFinalElementType(it.returnType) }
            .filter { !this.typeUtils.isLeaf(it) && it !in this.visited }
            .map { this.elementUtils.getTypeElement(it.toString()) }
            .forEach {
                this.visited.add(it.asType())
                this.handle(it, INTERNAL_SUFFIX)
            }
}