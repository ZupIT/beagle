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

package br.com.zup.beagle.android.data.serializer.adapter.defaults

import br.com.zup.beagle.android.utils.readObject
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

private const val FIRST_KEY = "first"
private const val SECOND_KEY = "second"

internal object PairAdapterFactory : JsonAdapter.Factory {

    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
        if (type !is ParameterizedType || Pair::class.java != type.rawType) return null

        return PairAdapter(
            moshi.adapter(type.actualTypeArguments[0]),
            moshi.adapter(type.actualTypeArguments[1])
        )
    }

    private class PairAdapter(
        private val firstAdapter: JsonAdapter<Any>,
        private val secondAdapter: JsonAdapter<Any>
    ) : JsonAdapter<Pair<Any, Any>>() {

        override fun toJson(writer: JsonWriter, value: Pair<Any, Any>?) {
            writer.beginObject()
            writer.name(FIRST_KEY)
            firstAdapter.toJson(writer, value?.first)
            writer.name(SECOND_KEY)
            secondAdapter.toJson(writer, value?.second)
            writer.endObject()
        }

        override fun fromJson(reader: JsonReader): Pair<Any, Any>? {
            val jsonObject = reader.readObject()

            val first = jsonObject.get(FIRST_KEY)
            val second = jsonObject.get(SECOND_KEY)
            return getObject(first, firstAdapter) to getObject(second, secondAdapter)
        }

        private fun getObject(jsonObject: Any, adapter: JsonAdapter<Any>): Any {
            return if (jsonObject is JSONArray || jsonObject is JSONObject)
                adapter.fromJson(jsonObject.toString())!! else adapter.fromJsonValue(jsonObject)!!
        }
    }
}