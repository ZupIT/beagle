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

package br.com.zup.beagle.android.context

import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import org.json.JSONArray
import org.json.JSONObject

/*
 * This function should be used in cases where user created the context implicit or explicit
 * because if user pass a map, list or any kind of object, this should be normalized
 * to a JSONArray or JSONObject.
 */
internal fun ContextData.normalize(): ContextData {
    return if (isValueNormalized()) {
        this
    } else {
        val newValue = BeagleMoshi.moshi.adapter(Any::class.java).toJson(value) ?: ""
        val normalizedValue: Any = when {
            newValue.startsWith("{") -> JSONObject(newValue)
            newValue.startsWith("[") -> JSONArray(newValue)
            else -> newValue
        }
        ContextData(this.id, normalizedValue)
    }
}

private fun ContextData.isValueNormalized(): Boolean {
    return value is String || value is Number || value is Boolean || value is JSONArray || value is JSONObject
}