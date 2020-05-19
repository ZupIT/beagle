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

package br.com.zup.beagle.networking.urlbuilder

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.InputStream
import java.lang.reflect.Type

class UrlBuilderDefaultTest {

    private lateinit var urlBuilderDefault: UrlBuilderDefault

    lateinit var urlBuilders: List<UrlBuilderData>

    @Before
    fun setUp() {
        urlBuilderDefault = UrlBuilderDefault()

        val jsonFileString = getJsonFromFile()

        urlBuilders = getUrlBuilderListFromJson(jsonFileString)

    }

    @Test
    fun format_should_concatenate_relative_path() {
        urlBuilders.forEach {
            // When
            val actual = urlBuilderDefault.format(it.base, it.path)

            // Then
            Assert.assertEquals(it.result, actual)
        }
    }

    private fun getUrlBuilderListFromJson(jsonFileString: String): List<UrlBuilderData> {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val type: Type = Types.newParameterizedType(MutableList::class.java, UrlBuilderData::class.java)
        val adapter: JsonAdapter<List<UrlBuilderData>> = moshi.adapter(type)
        return adapter.fromJson(jsonFileString) ?: listOf()
    }

    private fun getJsonFromFile(): String {
        val file = File("../../common/tests/", "UrlBuilderTestSpec.json")

        val inputStream: InputStream = file.inputStream()

        return inputStream.bufferedReader().use { it.readText() }
    }

    data class UrlBuilderData(
        val base: String? = null,
        val path: String = "",
        val result: String? = null
    )
}