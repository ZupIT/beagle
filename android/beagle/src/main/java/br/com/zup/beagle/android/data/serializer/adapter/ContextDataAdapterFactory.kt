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

package br.com.zup.beagle.android.data.serializer.adapter

import android.util.Log
import br.com.zup.beagle.android.annotation.ContextDataValue
import br.com.zup.beagle.android.utils.readValue
import br.com.zup.beagle.core.BeagleJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.nextAnnotations
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type

@BeagleJson
internal class ContextDataAdapterFactory : JsonAdapter.Factory {

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi,
    ): JsonAdapter<*>? {
        Log.v("uzias context adapter", "entrou")
        Log.v("uzias context adapter", this::class.java.toGenericString())
        Log.v("uzias context adapter context data ", ContextDataValue::class.java.toGenericString())
        Log.v("uzias context adapter", annotations.toString())

        return annotations.nextAnnotations<ContextDataValue>()?.let {
            Log.v("uzias annotation", "trueeee")
            val adapter: JsonAdapter<Any> = moshi.adapter(type)
            AnyToJsonObjectAdapter(
                adapter
            )
        }
    }
}

@BeagleJson
internal class AnyToJsonObjectAdapter(
    private val adapter: JsonAdapter<Any>,
) : JsonAdapter<Any>() {

    override fun fromJson(reader: JsonReader): Any? {
        Log.v("uzias from", reader.toString())
        return reader.readValue()
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        Log.v("uzias any", value.toString())
        Log.v("uzias any2", (value is JSONObject).toString())
        if (value is JSONObject || value is JSONArray) {
            val json = value.toString()
            val buffer = Buffer()
            buffer.write(json.toByteArray())
            writer.value(buffer)
        } else {
            adapter.toJson(writer, value)
        }
    }
}