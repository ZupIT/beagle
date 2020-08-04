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

import br.com.zup.beagle.compiler.ANDROID_ACTION
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName

class BeagleSetupInternalRegisteredActionGenerator {

    fun generate(isOverride: Boolean = true): FunSpec {
        val listReturnType = List::class.asClassName().parameterizedBy(
            Class::class.asClassName().parameterizedBy(
                ClassName(ANDROID_ACTION.packageName, ANDROID_ACTION.className)
            )
        )
        val spec = FunSpec.builder("registeredInternalActions")

        if (isOverride)
            spec.addModifiers(KModifier.OVERRIDE)

        return spec
            .returns(listReturnType)
            .addStatement("return InternalActionFactory.registeredActions()")
            .build()
    }
}