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

import br.com.zup.beagle.newanalytics.ActionAnalyticsConfig
import br.com.zup.beagle.newanalytics.ActionAnalyticsProperties
import br.com.zup.beagle.android.data.serializer.BeagleMoshi.moshi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

internal class AnalyticsActionConfigAdapterFactory : JsonAdapter.Factory {

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi,
    ): JsonAdapter<ActionAnalyticsConfig>? {
        return if (Types.getRawType(type) == ActionAnalyticsConfig::class.java) {
            AnalyticsActionConfigAdapter()
        } else {
            null
        }
    }
}

internal class AnalyticsActionConfigAdapter : JsonAdapter<ActionAnalyticsConfig>() {
    override fun fromJson(reader: JsonReader): ActionAnalyticsConfig? {
        val jsonValue = reader.readJsonValue()
        val actionAnalyticsConfig: ActionAnalyticsConfig?
        if (jsonValue is Boolean) {
            actionAnalyticsConfig = if (jsonValue) {
                ActionAnalyticsConfig.Enabled()
            } else {
                ActionAnalyticsConfig.Disabled()
            }
        } else {
            val value = jsonValue as? Map<String, Any>? ?: return null

            var attributes: List<String>? = null
            var additionalEntries: Map<String, Any>? = null
            if (value.containsKey(ATTRIBUTES)) {
                attributes = value[ATTRIBUTES] as List<String>?
            }
            if (value.containsKey(ADDITIONAL_ENTRIES)) {
                additionalEntries = value[ADDITIONAL_ENTRIES] as Map<String, Any>?
            }
            actionAnalyticsConfig = ActionAnalyticsConfig.Enabled(ActionAnalyticsProperties(
                attributes,
                additionalEntries
            ))
        }
        return actionAnalyticsConfig
    }

    override fun toJson(writer: JsonWriter, value: ActionAnalyticsConfig?) {
        if (value == null) {
            writer.nullValue()
        }
        value?.let {
            when (value) {
                is ActionAnalyticsConfig.Disabled -> {
                    moshi.adapter(Boolean::class.java).toJson(writer, false)
                }
                is ActionAnalyticsConfig.Enabled -> {
                    if (value.value == null) {
                        moshi.adapter(Boolean::class.java).toJson(writer, true)
                    } else {
                        writer.beginObject()
                        writer.name(ATTRIBUTES)
                        moshi.adapter(List::class.java).toJson(
                            writer,
                            (value.value as ActionAnalyticsProperties).attributes
                        )
                        writer.name(ADDITIONAL_ENTRIES)
                        moshi.adapter(Map::class.java).toJson(
                            writer,
                            (value.value as ActionAnalyticsProperties).additionalEntries
                        )
                        writer.endObject()
                    }
                }
            }
        }
    }

    companion object {
        private const val ATTRIBUTES: String = "attributes"
        private const val ADDITIONAL_ENTRIES = "additionalEntries"
    }
}
