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

package br.com.zup.beagle.android.data.cache

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BeagleCacheHelperTest {

    private val subject = BeagleCacheHelper

    private val url = RandomData.httpUrl()

    private val component = mockk<ServerDrivenComponent>(relaxUnitFun = true)

    @Test
    fun test_cache_should_return_get_cached_value() {
        //given
        subject.cache(url, component)

        //when
        val result = subject.getFromCache(url)

        //then
        assertEquals(component, result)
    }

    @Test
    fun test_cache_should_return_get_no_cached_value() {
        //Given
        val expectedUrl = RandomData.httpUrl()

        //when
        val result = subject.getFromCache(expectedUrl)

        //then
        assertNull(result)
    }
}