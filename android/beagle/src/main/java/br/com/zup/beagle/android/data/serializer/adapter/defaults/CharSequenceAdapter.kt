package br.com.zup.beagle.android.data.serializer.adapter.defaults

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

internal class CharSequenceAdapter {

    @ToJson
    fun toJson(charSequence: CharSequence): String = charSequence.toString()

    @FromJson
    fun fromJson(json: String): CharSequence = json
}