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

package br.com.zup.beagle.compiler.shared

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

class GenericFactoryProcessor<T : Annotation>(
    private val processingEnv: ProcessingEnvironment,
    private val className: String,
    private val beagleGeneratorFunction: BeagleGeneratorFunction<T>) {

    fun process(
        basePackageName: String,
        roundEnvironment: RoundEnvironment,
        importClass: BeagleClass,
        isInternalClass: Boolean
    ) {
        val typeSpec = getTypeSpec(roundEnvironment, isInternalClass)

        val beagleSetupFile = FileSpec.builder(
            basePackageName,
            className
        )
            .addImport(importClass.packageName, importClass.className)
            .addType(typeSpec)
            .build()

        beagleSetupFile.writeTo(processingEnv.filer)
    }

    fun createFunction(): FunSpec = beagleGeneratorFunction.createFuncSpec(beagleGeneratorFunction.getFunctionName())
        .addModifiers(KModifier.OVERRIDE)
        .addStatement("return $className.${beagleGeneratorFunction.getFunctionName()}()")
        .build()

    private fun getTypeSpec(roundEnvironment: RoundEnvironment, isInternalClass: Boolean): TypeSpec {
        val typeSpecBuilder = TypeSpec.objectBuilder(className)
            .addFunction(beagleGeneratorFunction.generate(roundEnvironment))
        if (isInternalClass) {
            typeSpecBuilder.addModifiers(KModifier.INTERNAL)
        } else {
            typeSpecBuilder.addModifiers(KModifier.PUBLIC, KModifier.FINAL)
        }
        return typeSpecBuilder.build()
    }
}