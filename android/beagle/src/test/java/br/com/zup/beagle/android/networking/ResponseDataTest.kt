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

package br.com.zup.beagle.android.networking

import io.mockk.mockk
import org.junit.Test

import org.junit.Assert.*

class ResponseDataTest {

    @Test
    fun equals_should_compare_two_objects_that_have_equals_data() {
        val responseData1 = ResponseData(0, byteArrayOf())
        val responseData2 = ResponseData(0, byteArrayOf())

        assertTrue(responseData1 == responseData2)
    }

    @Test
    fun equals_should_compare_two_objects_that_have_different_status_code() {
        val responseData1 = ResponseData(0, byteArrayOf())
        val responseData2 = ResponseData(1, byteArrayOf())

        assertFalse(responseData1 == responseData2)
    }

    @Test
    fun equals_should_compare_two_objects_that_have_different_headers() {
        val responseData1 = ResponseData(0, byteArrayOf(), headers = mapOf(Pair("", "")))
        val responseData2 = ResponseData(0, byteArrayOf(), headers = mapOf())

        assertFalse(responseData1 == responseData2)
    }

    @Test
    fun equals_should_compare_two_objects_that_have_different_bytes() {
        val responseData1 = ResponseData(0, byteArrayOf(0x0))
        val responseData2 = ResponseData(0, byteArrayOf(0x0, 0x10))

        assertFalse(responseData1 == responseData2)
    }

    @Test
    fun equals_should_compare_two_objects_of_same_instance() {
        val responseData = ResponseData(0, byteArrayOf())

        assertTrue(responseData == responseData)
    }

    @Test
    fun equals_should_compare_two_different_classes() {
        val responseData = ResponseData(0, byteArrayOf())
        val otherClass = mockk<RequestData>()

        assertFalse(responseData.equals(otherClass))
    }

    @Test
    fun hashCode1() {
        val responseData1 = ResponseData(0, byteArrayOf())
        val responseData2 = ResponseData(0, byteArrayOf())

        assertEquals(responseData1.hashCode(), responseData2.hashCode())
    }
}