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

package br.com.zup.beagle.android.networking.urlbuilder

import br.com.zup.beagle.android.testutil.IoUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UrlBuilderDefaultTest {

    private lateinit var urlBuilderDefault: UrlBuilderDefault

    private lateinit var urlBuilders: List<UrlBuilderData>

    @Before
    fun setUp() {
        urlBuilderDefault = UrlBuilderDefault()

        val jsonFileString = IoUtils.getJsonFromFile("../../common/tests/", "UrlBuilderTestSpec.json")

        urlBuilders = IoUtils.getDataListFromJson(jsonFileString)

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

    data class UrlBuilderData(
        val base: String? = null,
        val path: String = "",
        val result: String? = null
    )
}