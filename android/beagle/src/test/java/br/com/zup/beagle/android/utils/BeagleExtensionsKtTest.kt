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

package br.com.zup.beagle.android.utils

import org.junit.Test

import org.junit.Assert.assertEquals

class BeagleExtensionsKtTest {

    @Test
    fun toAndroidId_should_return_3546_when_value_is_oi() {
        // Given
        val myId = "oi"

        // When
        val result = myId.toAndroidId()

        // Then
        assertEquals(3546, result)
    }

    @Test
    fun toAndroidId_should_return_3366_when_value_is_OI() {
        // Given
        val myId = "OI"

        // When
        val result = myId.toAndroidId()

        // Then
        assertEquals(2522, result)
    }

    @Test
    fun toAndroidId_should_return_3366_when_value_is_io() {
        // Given
        val myId = "io"

        // When
        val result = myId.toAndroidId()

        // Then
        assertEquals(3366, result)
    }
}