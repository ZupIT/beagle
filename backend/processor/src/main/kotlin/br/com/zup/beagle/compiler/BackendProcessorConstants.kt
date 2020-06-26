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

import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.expression.BeagleIterableSubExpression
import br.com.zup.beagle.widget.expression.ExpressionHelper
import com.squareup.kotlinpoet.asTypeName

internal const val ROOT_SUFFIX = "_"
internal const val INTERNAL_SUFFIX = "BeagleSubexpression"
internal const val PLACEHOLDER = "%L"
internal const val PARAMETER = "expression"
internal const val ACCESS = "$PARAMETER.access"
internal const val ITERABLE_PARAM = "it"

internal val EXPRESSION = ExpressionHelper::class.asTypeName()
internal val START = ExpressionHelper.Start::class.asTypeName()
internal val LIST_SUBEXPRESSION = BeagleIterableSubExpression::class.asTypeName()
internal val BIND = Bind::class.asTypeName()
internal val BIND_EXPRESSION = Bind.Expression::class.asTypeName()

internal val LEAF_TYPES = arrayOf(
    Any::class,
    Boolean::class,
    Byte::class,
    Char::class,
    Int::class,
    Long::class,
    Float::class,
    Double::class,
    String::class
).map { it.javaObjectType.asTypeName() }