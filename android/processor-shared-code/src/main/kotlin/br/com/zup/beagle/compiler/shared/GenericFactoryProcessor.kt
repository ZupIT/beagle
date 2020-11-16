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

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

class GenericFactoryProcessor<T : Annotation>(
    private val processingEnv: ProcessingEnvironment,
    private val className: String,
    private val beagleGeneratorFunction: BeagleGeneratorFunction<T>,
) {

    fun process(
        basePackageName: String,
        roundEnvironment: RoundEnvironment,
        importClass: BeagleClass,
        isInternalClass: Boolean,
    ) {
        process(basePackageName, roundEnvironment, listOf(importClass), isInternalClass)
    }

    fun process(
        basePackageName: String,
        roundEnvironment: RoundEnvironment,
        importClass: BeagleClass,
    ) {
        process(basePackageName, roundEnvironment, listOf(importClass))
    }

    fun process(
        basePackageName: String,
        roundEnvironment: RoundEnvironment,
        importClasses: List<BeagleClass>,
        isInternalClass: Boolean = false,
        superInterface: BeagleClass? = null,
    ) {
        val typeSpec = getTypeSpec(roundEnvironment, isInternalClass, superInterface)

        val fileSpecBuilder = FileSpec.builder(
            basePackageName,
            className
        )

        importClasses.forEach { importClass ->
            fileSpecBuilder.addImport(importClass.packageName, importClass.className)
        }

        fileSpecBuilder.addAnnotation(
            AnnotationSpec.builder(Suppress::class.java)
                .addMember("%S, %S, %S, %S", "OverridingDeprecatedMember", "DEPRECATION",
                    "UNCHECKED_CAST", "UNUSED_EXPRESSION")
                .build()
        )

        val fileSpec = fileSpecBuilder.addType(typeSpec)
            .build()

        fileSpec.writeTo(processingEnv.filer)
    }

    fun createFunction(): FunSpec = beagleGeneratorFunction.createFuncSpec(beagleGeneratorFunction.getFunctionName())
        .addModifiers(KModifier.OVERRIDE)
        .addStatement("return $className.${beagleGeneratorFunction.getFunctionName()}()")
        .build()

    private fun getTypeSpec(
        roundEnvironment: RoundEnvironment,
        isInternalClass: Boolean,
        superInterface: BeagleClass?,
    ): TypeSpec {
        val typeSpecBuilder = TypeSpec.objectBuilder(className)
            .addFunction(beagleGeneratorFunction.generate(roundEnvironment))

        if (isInternalClass) {
            typeSpecBuilder.addModifiers(KModifier.INTERNAL)
        } else {
            typeSpecBuilder.addModifiers(KModifier.PUBLIC, KModifier.FINAL)
        }

        superInterface?.let {
            typeSpecBuilder.addSuperinterface(ClassName(
                superInterface.packageName,
                superInterface.className
            ))
        }
        return typeSpecBuilder.build()
    }
}