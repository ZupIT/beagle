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

import org.junit.Assert.*

private interface A<T>
private class B : A<String>
private class C

class GenericExtensionsKtTest {

    @Test
    fun implementsGenericTypeOf_should_return_true_when_generic_type_match() {
        // Given
        val clazz = B()

        // When
        val actual = clazz.implementsGenericTypeOf(A::class.java, String::class.java)

        // Then
        assertTrue(actual)
    }

    @Test
    fun implementsGenericTypeOf_should_return_false_when_generic_type_does_not_match() {
        // Given
        val clazz = B()

        // When
        val actual = clazz.implementsGenericTypeOf(A::class.java, Int::class.java)

        // Then
        assertFalse(actual)
    }

    @Test
    fun implementsGenericTypeOf_should_return_false_when_class_does_not_implements_expected_interface() {
        // Given
        val clazz = C()

        // When
        val actual = clazz.implementsGenericTypeOf(A::class.java, String::class.java)

        // Then
        assertFalse(actual)
    }
}