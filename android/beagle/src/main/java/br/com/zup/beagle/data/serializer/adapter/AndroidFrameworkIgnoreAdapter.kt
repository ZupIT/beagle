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

package br.com.zup.beagle.data.serializer.adapter

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

internal class AndroidFrameworkIgnoreAdapterFactory : JsonAdapter.Factory {

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        return if (isTypeFromAndroid(type)) {
            AndroidFrameworkIgnoreAdapter()
        } else {
            null
        }
    }

    private fun isTypeFromAndroid(type: Type): Boolean {
        listOf(View::class.java, Context::class.java, Fragment::class.java).forEach {
            val superclass = Types.getRawType(type).superclass as? Class<Any>

            if (checkIfSuperClassIsTypeOf(superclass, it)) {
                return true
            }
        }

        return false
    }

    private fun checkIfSuperClassIsTypeOf(superclass: Class<in Any>?, clazz: Class<out Any>): Boolean {
        return when (superclass) {
            null -> false
            clazz -> true
            else -> checkIfSuperClassIsTypeOf(superclass.superclass, clazz)
        }
    }
}

internal class AndroidFrameworkIgnoreAdapter : JsonAdapter<Any>() {

    override fun fromJson(reader: JsonReader): Any? {
        reader.skipValue()
        return null
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        writer.nullValue()
    }
}