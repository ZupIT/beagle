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

import br.com.zup.beagle.annotation.RegisterWidget
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.RoundEnvironment

const val REGISTERED_WIDGETS = "registeredWidgets"

class BeagleSetupRegisteredWidgetGenerator {

    fun generate(roundEnvironment: RoundEnvironment): FunSpec {
        val classValues = StringBuilder()
        val registerWidgetAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(RegisterWidget::class.java)

        registerWidgetAnnotatedClasses.forEachIndexed { index, element ->
            classValues.append("\t${element}::class.java as Class<WidgetView>")
            if (index < registerWidgetAnnotatedClasses.size - 1) {
                classValues.append(",\n")
            }
        }

        return createFuncSpec()
            .addCode("""
                        |val $REGISTERED_WIDGETS = listOf<Class<WidgetView>>(
                        |   $classValues
                        |)
                    |""".trimMargin())
            .addStatement("return $REGISTERED_WIDGETS")
            .build()
    }

    fun createFuncSpec(): FunSpec.Builder {
        val listReturnType = List::class.asClassName().parameterizedBy(
            Class::class.asClassName().parameterizedBy(
                ClassName(WIDGET_VIEW.packageName, WIDGET_VIEW.className)
            )
        )

        return FunSpec.builder(REGISTERED_WIDGETS)
            .returns(listReturnType)
    }
}