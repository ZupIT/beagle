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

package br.com.zup.beagle.android.networking.grpc.serializer.adapter

import beagle.Messages
import br.com.zup.beagle.android.networking.grpc.serializer.NetworkingMoshi.moshi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Types
import java.lang.reflect.Type

class MessageAdapter : JsonAdapter<Messages.ViewNode>() {

    override fun fromJson(reader: JsonReader): Messages.ViewNode? {
        TODO("Not yet implemented")
    }

    override fun toJson(writer: JsonWriter, value: Messages.ViewNode?) {
        writer.beginObject()
        value?.let {
            writer.name("_beagleComponent_")

            writer.value(value.beagleComponent)

            if (value.id != null && value.id.isNotEmpty()) {
                writer.name("id")
                writer.value(value.id)
            }

            if (value.hasContext()) {
                writer.name("context")
                moshi.adapter(Messages.DataContext::class.java).toJson(writer, value.context)
            }

            if (value.childrenList.isNotEmpty()) {
                writer.name("children")
                val type: Type = Types.newParameterizedType(MutableList::class.java, Messages.ViewNode::class.java)
                val viewNodeAdapter: JsonAdapter<List<Messages.ViewNode>> = moshi.adapter(type)
                viewNodeAdapter.toJson(writer, value.childrenList)
            }

            if (value.hasChild()) {
                writer.name("child")
                moshi.adapter(Messages.ViewNode::class.java).toJson(writer, value.child)
            }

            if (value.style != null && value.style.isNotEmpty()) {
                val type = Types.newParameterizedType(MutableMap::class.java, String::class.java, Any::class.java)
                val adapter: JsonAdapter<Map<String, Any>> = moshi.adapter(type)
                val map = adapter.fromJson(value.style)
                writer.name("style")
                adapter.toJson(writer, map)
            }

            if (value.attributes != null && value.attributes.isNotEmpty()) {
                val type = Types.newParameterizedType(MutableMap::class.java, String::class.java, Any::class.java)
                val adapter: JsonAdapter<Map<String, Any>> = moshi.adapter(type)
                val map = adapter.fromJson(value.attributes)
                map?.forEach { mapEntry ->
                    writer.name(mapEntry.key)
                    moshi.adapter(Any::class.java).toJson(writer, mapEntry.value)

                }
            }
        }
        writer.endObject()
    }
}