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

package br.com.zup.beagle.android.compiler

import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.compiler.WIDGET_VIEW
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.RoundEnvironment

class BeagleSetupRegisteredWidgetGenerator {

    fun generate(roundEnvironment: RoundEnvironment): FunSpec {
        val classValues = StringBuilder()
        val registerWidgetAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(RegisterWidget::class.java)
        val listReturnType = List::class.asClassName().parameterizedBy(
            Class::class.asClassName().parameterizedBy(
                ClassName(WIDGET_VIEW.packageName, WIDGET_VIEW.className)
            )
        )

        registerWidgetAnnotatedClasses.forEachIndexed { index, element ->
            classValues.append("\t${element}::class.java as Class<WidgetView>")
            if (index < registerWidgetAnnotatedClasses.size - 1) {
                classValues.append(",\n")
            }
        }

        return FunSpec.builder("registeredWidgets")
            .addModifiers(KModifier.OVERRIDE)
            .returns(listReturnType)
            .addCode("""
                        |val registeredWidgets = listOf<Class<WidgetView>>(
                        |   $classValues
                        |)
                    |""".trimMargin())
            .addStatement("return registeredWidgets")
            .build()
    }
}