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

package br.com.zup.beagle.utils

import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.core.Binding
import java.lang.reflect.ParameterizedType
import java.lang.reflect.WildcardType
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

fun <I, G> Any.implementsGenericTypeOf(
    interfaceClazz: Class<I>,
    genericType: Class<G>
): Boolean {
    return this::class.java.genericInterfaces.filterIsInstance<ParameterizedType>()
        .filter {
            val rawType = it.rawType as Class<*>
            rawType == interfaceClazz
        }.any {
            val types = it.actualTypeArguments
            val paramType = types[0]
            val type = if (paramType is WildcardType) {
                paramType.upperBounds[0]
            } else {
                paramType
            }
            val typeClass = type as Class<*>
            genericType == typeClass
        }
}

fun <T : Any> getValue(binding: Bind<T>, property: T?): T? {
    return when (binding) {
        is Binding.Expression<T> -> {
            property
        }
        is Binding.Value<T> -> {
            binding.value
        }
        else -> {
            throw IllegalStateException("Invalid bind instance")
        }
    }
}

inline fun isMarkedNullable(propertyName: String, obj: Any): Boolean  {
    return obj::class.declaredMemberProperties.first { it.name == propertyName }.returnType.isMarkedNullable
}

class ClassTest(val nullable: String?,val notNullable: String)

fun main() {
    println(isMarkedNullable("nullable", ClassTest(nullable = null,notNullable = "")))
    println(isMarkedNullable("notNullable", ClassTest(nullable = null,notNullable = "")))
//    ClassTest::class.declaredMemberProperties.forEach {
//        println("Property $it isMarkedNullable=${it.returnType.isMarkedNullable}")
//    }
}