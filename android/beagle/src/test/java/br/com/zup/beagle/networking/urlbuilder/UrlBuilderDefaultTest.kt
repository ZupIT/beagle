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

import br.com.zup.beagle.networking.urlbuilder.UrlBuilderDefault
import br.com.zup.beagle.testutil.RandomData
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UrlBuilderDefaultTest {

    private lateinit var urlBuilderDefault: UrlBuilderDefault

    @Before
    fun setUp() {
        urlBuilderDefault = UrlBuilderDefault()
    }

    @Test
    fun format_should_concatenate_relative_path() {
        // Given
        val endpoint = RandomData.httpUrl()
        val path = "/" + RandomData.string()

        // When
        val actual = urlBuilderDefault.format(endpoint, path)

        // Then
        val expected = endpoint + path
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun format_should_return_absolute_path() {
        // Given
        val endpoint = RandomData.httpUrl()
        val path = RandomData.httpUrl()

        // When
        val actual = urlBuilderDefault.format(endpoint, path)

        // Then
        Assert.assertEquals(path, actual)
    }

    @Test
    fun format_should_return_path_that_has_no_slash() {
        // Given
        val endpoint = RandomData.httpUrl()
        val path = RandomData.string()

        // When
        val actual = urlBuilderDefault.format(endpoint, path)

        // Then
        Assert.assertEquals(path, actual)
    }
}