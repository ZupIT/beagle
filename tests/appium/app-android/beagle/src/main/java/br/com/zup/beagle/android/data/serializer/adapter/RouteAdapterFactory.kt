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

import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.data.serializer.BeagleMoshi.moshi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

class RouteAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<Route>? {
        val adapter: JsonAdapter<Bind<String>> = getBindAdapter()
        return if (Types.getRawType(type) == Route::class.java) {
            RouteAdapter(adapter)
        } else {
            null
        }
    }
}

private fun getBindAdapter(): JsonAdapter<Bind<String>> {
    val type: Type = Types.newParameterizedType(Bind::class.java, String::class.java)
    return moshi.adapter(type)
}

internal class RouteAdapter(private val adapter: JsonAdapter<Bind<String>>) : JsonAdapter<Route>() {
    override fun fromJson(reader: JsonReader): Route? {
        val jsonValue = reader.readJsonValue()

        val value = jsonValue as Map<String, Any>
        return if (value.containsKey(URL)) {
            val url = adapter.fromJsonValue(value[URL] as String)!!
            Route.Remote(url, value[SHOULD_PREFETCH] as Boolean, convertScreen(value[FALLBACK]))
        } else {
            val message = "Expected a Screen for the screen key in $value."
            Route.Local(convertScreen(value[SCREEN]) ?: throw JsonDataException(message))
        }
    }

    override fun toJson(writer: JsonWriter, value: Route?) {
        writer.beginObject()
        when (value) {
            is Route.Remote -> {
                writer.name(URL)
                getBindAdapter().toJson(writer, value.url)
                writer.name(SHOULD_PREFETCH)
                moshi.adapter(Boolean::class.java).toJson(writer, value.shouldPrefetch)
                writer.name(FALLBACK)
                moshi.adapter(Screen::class.java).toJson(writer, value.fallback)
            }
            is Route.Local -> {
                writer.name(SCREEN)
                moshi.adapter(Screen::class.java).toJson(writer, value.screen)
            }
        }
        writer.endObject()
    }

    private fun convertScreen(value: Any?) =
        moshi.adapter(Screen::class.java).fromJsonValue(value)

    companion object {
        private const val URL: String = "url"
        private const val SHOULD_PREFETCH = "shouldPrefetch"
        private const val FALLBACK: String = "fallback"
        private const val SCREEN: String = "screen"
    }
}
