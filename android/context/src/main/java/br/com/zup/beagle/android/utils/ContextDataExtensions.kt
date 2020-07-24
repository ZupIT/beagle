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

package br.com.zup.beagle.android.utils

import br.com.zup.beagle.android.context.ContextConstant.moshi
import br.com.zup.beagle.android.context.ContextData
import org.json.JSONArray
import org.json.JSONObject

/*
 * This function should be used in cases where user created the context implicit or explicit
 * because if user pass a map, list or any kind of object, this should be normalized
 * to a JSONArray or JSONObject.
 */

//TODO removed internal
fun ContextData.normalize(): ContextData {
    return if (isValueNormalized()) {
        this
    } else {
        val newValue = moshi.adapter(Any::class.java).toJson(value) ?: ""
        val normalizedValue = newValue.normalizeContextValue()
        ContextData(this.id, normalizedValue)
    }
}

internal fun String.normalizeContextValue(): Any {
    return when {
        this.startsWith("{") -> JSONObject(this)
        this.startsWith("[") -> JSONArray(this)
        else -> this
    }
}

private fun ContextData.isValueNormalized(): Boolean {
    return value is String || value is Number || value is Boolean || value is JSONArray || value is JSONObject
}