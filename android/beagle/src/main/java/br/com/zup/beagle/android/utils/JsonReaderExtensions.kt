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

import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.JsonReader
import org.json.JSONArray
import org.json.JSONObject

internal fun JsonReader.readValue(): Any? =
    peek().let { token ->
        when (token) {
            JsonReader.Token.BEGIN_ARRAY -> readArray()
            JsonReader.Token.BEGIN_OBJECT -> readObject()
            JsonReader.Token.STRING -> nextString()
            JsonReader.Token.NUMBER -> nextNumber()
            JsonReader.Token.BOOLEAN -> nextBoolean()
            JsonReader.Token.NULL -> nextNull()
            else -> throw JsonEncodingException("Unexpected token: $token")
        }
    }

internal fun JsonReader.nextNumber(): Number {
    return try {
        nextInt()
    } catch (ignored: Throwable) {
        try {
            nextLong()
        } catch (ignored: Throwable) {
            nextDouble()
        }
    }
}

internal fun JsonReader.readObject(): JSONObject {
    val jsonObject = JSONObject()

    beginObject()
    while (peek() != JsonReader.Token.END_OBJECT) {
        jsonObject.put(nextName(), readValue())
    }
    endObject()

    return jsonObject
}

internal fun JsonReader.readArray(): JSONArray {
    val jsonArray = JSONArray()

    beginArray()
    while (peek() != JsonReader.Token.END_ARRAY) {
        jsonArray.put(readValue())
    }
    endArray()

    return jsonArray
}