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

import br.com.zup.beagle.expression.BeagleIterableSubexpression
import br.com.zup.beagle.expression.Expression
import com.squareup.kotlinpoet.asTypeName

internal const val ROOT_SUFFIX = "_"
internal const val INTERNAL_SUFFIX = "BeagleSubexpression"
internal const val PLACEHOLDER = "%L"
internal const val PARAMETER = "expression"
internal const val ACCESS = "$PARAMETER.access"
internal const val ITERABLE_PARAM = "it"
internal const val GET = "get"

internal val EXPRESSION = Expression::class.asTypeName()
internal val START = Expression.Start::class.asTypeName()
internal val LIST_SUBEXPRESSION = BeagleIterableSubexpression::class.asTypeName()
internal val INTERNAL = Regex("\\$.*")
internal val GETTER = Regex("$GET(?!Class).*")

private val BASIC_TYPES = arrayOf(
    Any::class,
    Boolean::class,
    Byte::class,
    Char::class,
    Int::class,
    Long::class,
    Float::class,
    Double::class,
    String::class
)

internal val LEAF_TYPES = BASIC_TYPES.map { it.javaObjectType.asTypeName() }

internal val JAVA_TO_KOTLIN = (BASIC_TYPES + Iterable::class + Collection::class + List::class + Set::class)
    .associate { it.javaObjectType.asTypeName() to it.asTypeName() }