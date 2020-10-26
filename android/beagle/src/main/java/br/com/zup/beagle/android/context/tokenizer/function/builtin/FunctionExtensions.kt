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

package br.com.zup.beagle.android.context.tokenizer.function.builtin

import org.json.JSONArray

internal fun Array<out Any?>.toListOfDoubles(): List<Double> {
    return this.map {
        it as Double
    }
}

internal fun Array<out Any?>.toListOfInts(): List<Int> {
    return this.map {
        it as Int
    }
}

internal fun Array<out Any?>.toListOfBooleans(): List<Boolean> {
    return this.map {
        it as Boolean
    }
}

internal fun Any?.toJSONArray(): JSONArray {
    return this as JSONArray
}