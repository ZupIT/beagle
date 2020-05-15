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

import com.squareup.kotlinpoet.asTypeName

const val KAPT_KEY = "kapt.kotlin.generated"
const val GET = "get"
const val INTERNAL_MARKER = '$'

val JAVA_TO_KOTLIN = arrayOf(
    Any::class,
    Boolean::class,
    Byte::class,
    Char::class,
    Int::class,
    Long::class,
    Float::class,
    Double::class,
    String::class,
    Iterable::class,
    Collection::class,
    List::class,
    Set::class,
    Map::class
).associate { it.javaObjectType.asTypeName() to it.asTypeName() }