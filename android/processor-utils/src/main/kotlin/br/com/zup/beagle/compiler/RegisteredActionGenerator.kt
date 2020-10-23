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

import br.com.zup.beagle.annotation.RegisterAction
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.RoundEnvironment

const val REGISTERED_ACTIONS = "registeredActions"

class RegisteredActionGenerator {

    fun generate(roundEnvironment: RoundEnvironment): FunSpec {
        val registerAnnotatedClasses = roundEnvironment.getElementsAnnotatedWith(RegisterAction::class.java)

        val classValues = registerAnnotatedClasses.joinToString(",\n") { element ->
            "\t${element}::class.java as Class<Action>"
        }

        return createFuncSpec()
            .addCode("""
                        |val $REGISTERED_ACTIONS = listOf<Class<Action>>(
                        |   $classValues
                        |)
                    |""".trimMargin())
            .addStatement("return $REGISTERED_ACTIONS")
            .build()
    }

    fun createFuncSpec(): FunSpec.Builder {
        val listReturnType = List::class.asClassName().parameterizedBy(
            Class::class.asClassName().parameterizedBy(
                ClassName(ANDROID_ACTION.packageName, ANDROID_ACTION.className)
            )
        )

        return FunSpec.builder(REGISTERED_ACTIONS)
            .returns(listReturnType)
    }
}
