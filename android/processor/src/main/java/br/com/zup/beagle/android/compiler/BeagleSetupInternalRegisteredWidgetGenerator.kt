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

import br.com.zup.beagle.compiler.WIDGET_VIEW
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName

class BeagleSetupInternalRegisteredWidgetGenerator {

    fun generate(isOverride: Boolean = true): FunSpec {
        val listReturnType = List::class.asClassName().parameterizedBy(
            Class::class.asClassName().parameterizedBy(
                ClassName(WIDGET_VIEW.packageName, WIDGET_VIEW.className)
            )
        )
        val spec = FunSpec.builder("registeredInternalWidgets")

        if (isOverride)
            spec.addModifiers(KModifier.OVERRIDE)

        return spec
            .returns(listReturnType)
            .addCode("""
                        |val registeredWidgets = br.com.zup.beagle.android.setup.InternalWidgetFactory.registeredWidgets()
                    |""".trimMargin())
            .addStatement("return registeredWidgets")
            .build()
    }
}