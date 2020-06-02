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

package br.com.zup.beagle.data.serializer.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
internal annotation class ContextDataValue

internal class ContextDataAdapterFactory : JsonAdapter.Factory {

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        return Types.nextAnnotations(annotations, ContextDataValue::class.java)?.let {
            val adapter: JsonAdapter<Any> = moshi.adapter(type)
            AnyToJsonObjectAdapter(adapter)
        }
    }
}

internal class AnyToJsonObjectAdapter(
    private val adapter: JsonAdapter<Any>
) : JsonAdapter<Any>() {

    override fun fromJson(reader: JsonReader): Any? {
        val type = reader.peek()
        val value = reader.readJsonValue()
        return when (type) {
            JsonReader.Token.BEGIN_OBJECT -> {
                val json = adapter.toJson(value)
                JSONObject(json)
            }
            JsonReader.Token.BEGIN_ARRAY -> {
                val json = adapter.toJson(value)
                JSONArray(json)
            }
            else -> {
                value
            }
        }
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        adapter.toJson(writer, value)
    }
}