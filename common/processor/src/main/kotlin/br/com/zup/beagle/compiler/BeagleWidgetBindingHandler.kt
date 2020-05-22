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

import br.com.zup.beagle.core.BindAttribute
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
import kotlin.reflect.KClass

class BeagleWidgetBindingHandler(
    processingEnvironment: ProcessingEnvironment,
    private val outputDirectory: File,
    private val bindClass: KClass<out BindAttribute<*>>
) {
    companion object {
        const val SUFFIX = "Binding"
    }

    private val elementUtils = processingEnvironment.elementUtils
    private val typeUtils = processingEnvironment.typeUtils

    fun handle(element: TypeElement) {
        FileSpec.get(this.elementUtils.getPackageAsString(element), this.createBindingClass(element))
            .writeTo(this.outputDirectory)
    }

    private fun createBindingClass(element: TypeElement) =
        element.visibleGetters.map { this.createBindParameter(it) }.let { parameters ->
            TypeSpec.classBuilder("${element.simpleName}$SUFFIX")
                .superclass(this.typeUtils.getKotlinName(element.superclass))
                .addSuperinterfaces(element.interfaces.map(TypeMirror::asTypeName))
                .primaryConstructor(FunSpec.constructorFrom(parameters))
                .addProperties(parameters.map { PropertySpec.from(it) })
                .build()
        }

    private fun createBindParameter(element: ExecutableElement) =
        ParameterSpec.builder(
            element.fieldName,
            bindClass.asTypeName().parameterizedBy(this.typeUtils.getKotlinName(element.returnType))
        ).build()
}