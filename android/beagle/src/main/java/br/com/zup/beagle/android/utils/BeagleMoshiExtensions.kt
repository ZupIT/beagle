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

import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import org.json.JSONArray
import org.json.JSONObject

internal fun String.tryToDeserialize(): Any? =
    try {
        when (val value = BeagleMoshi.moshi.adapter(Any::class.java).fromJson(this)) {
            is Collection<*> -> {
                try {
                    val type = Types.newParameterizedType(MutableList::class.java, JSONObject::class.java)
                    val adapter: JsonAdapter<MutableList<JSONObject>> = BeagleMoshi.moshi.adapter(type)
                    JSONArray(adapter.fromJson(this))
                } catch (ex: Exception) {
                    JSONArray(value)
                }
            }
            is Map<*, *> -> BeagleMoshi.moshi.adapter(JSONObject::class.java).fromJson(this)
            is Number -> {
                try {
                    this.toInt()
                } catch (ex: NumberFormatException) {
                    this.toDoubleOrNull()
                }
            }
            else -> value
        }
    } catch (ex: Exception) {
        this
    }
