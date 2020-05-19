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

package br.com.zup.beagle.testutil

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.io.InputStream
import java.lang.reflect.Type

object IoUtils {

    fun getJsonFromFile(parent: String, child: String): String {
        val file = File(parent, child)

        val inputStream: InputStream = file.inputStream()

        return inputStream.bufferedReader().use { it.readText() }
    }

    inline fun <reified T> getDataListFromJson(jsonFileString: String): List<T> {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val type: Type = Types.newParameterizedType(MutableList::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(type)
        return adapter.fromJson(jsonFileString) ?: listOf()
    }

}