package br.com.zup.beagle.android.data.serializer.adapter

import br.com.zup.beagle.android.utils.readObject
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import org.json.JSONObject
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