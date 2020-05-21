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
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

class BeagleWidgetBindingHandler(processingEnvironment: ProcessingEnvironment, private val outputDirectory: File =
    processingEnvironment.kaptGeneratedDirectory) {
    companion object {
        const val SUFFIX = "Binding"
        val BINDING_CLASS = ClassName(
            "br.com.zup.beagle.core",
            "Binding"
        )
    }

    private val elementUtils = processingEnvironment.elementUtils
    private val typeUtils = processingEnvironment.typeUtils

    fun handle(element: TypeElement) {
        getFileSpec(element)
            .writeTo(this.outputDirectory)
    }

    fun getFileSpec(element: TypeElement) =
        getFileSpec(element, this.createBindingClass(element).build())

    fun getFileSpec(element: TypeElement, typeSpec: TypeSpec) =
        FileSpec.get(this.elementUtils.getPackageAsString(element), typeSpec)

    fun createBindingClass(element: TypeElement): TypeSpec.Builder =
        element.visibleGetters.map { this.createBindParameter(it) }.let { parameters ->
            TypeSpec.classBuilder("${element.simpleName}$SUFFIX")
                .superclass(this.typeUtils.getKotlinName(element.superclass))
                .addSuperinterfaces(element.interfaces.map(TypeMirror::asTypeName))
                .primaryConstructor(FunSpec.constructorFrom(parameters))
                .addProperties(parameters.map { PropertySpec.from(it) })
        }

    private fun createBindParameter(element: ExecutableElement): ParameterSpec {
        return ParameterSpec.builder(
            element.fieldName,
            BINDING_CLASS.parameterizedBy(this.typeUtils.getKotlinName(element.returnType))
        ).build()
    }
}