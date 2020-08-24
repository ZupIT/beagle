package br.com.zup.beagle.android.data.serializer.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import org.json.JSONArray
import org.json.JSONObject
import com.squareup.moshi.JsonReader.Token.END_ARRAY
import com.squareup.moshi.JsonReader.Token.END_OBJECT
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import okio.Buffer

class SimpleJsonAdapter {

    @FromJson
    fun fromJson(reader: JsonReader): JSONObject? {
        return reader.readObject()
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: JSONObject?) {
        val json = value.toString()
        val buffer = Buffer()
        buffer.write(json.toByteArray())
        writer.value(buffer)
    }
}

internal fun JsonReader.readValue(): Any? =
    peek().let { token ->
        when (token) {
            JsonReader.Token.BEGIN_ARRAY -> readArray()
            JsonReader.Token.BEGIN_OBJECT -> readObject()
            JsonReader.Token.STRING -> nextString()
            JsonReader.Token.NUMBER -> nextNumber()
            JsonReader.Token.BOOLEAN -> nextBoolean()
            JsonReader.Token.NULL -> nextNull() ?: null
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
    while (peek() != END_OBJECT) {
        jsonObject.put(nextName(), readValue())
    }
    endObject()

    return jsonObject
}

internal fun JsonReader.readArray(): JSONArray {
    val jsonArray = JSONArray()

    beginArray()
    while (peek() != END_ARRAY) {
        jsonArray.put(readJsonValue())
    }
    endArray()

    return jsonArray
}