package br.com.zup.beagle.android.data.serializer.adapter

import br.com.zup.beagle.android.utils.readArray
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import okio.Buffer
import org.json.JSONArray

class SimpleJsonArrayAdapter {

    @FromJson
    fun fromJson(reader: JsonReader): JSONArray? {
        return reader.readArray()
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: JSONArray?) {
        val json = value.toString()
        val buffer = Buffer()
        buffer.write(json.toByteArray())
        writer.value(buffer)
    }
}