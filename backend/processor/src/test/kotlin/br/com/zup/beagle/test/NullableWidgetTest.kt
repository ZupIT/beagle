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

package br.com.zup.beagle.test

import br.com.zup.beagle.core.Bind
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class NullableWidgetTest {
    @Test
    fun test_Value_construction() {
        val value = object : Any() {}

        val actual = NullableWidgetBinding(Bind.Value(value))

        assertNotNull(actual.thing)
        assertEquals(value, actual.thing.value)
        assertEquals(NullableWidget::class.supertypes, NullableWidgetBinding::class.supertypes)
    }

    @Test
    fun test_Expression_construction() {
        val expression = "@{}"

        val actual = NullableWidgetBinding(Bind.Expression(expression))

        assertNotNull(actual.thing)
        assertEquals(expression, actual.thing.value)
        assertEquals(NullableWidget::class.supertypes, NullableWidgetBinding::class.supertypes)
    }

    @Test
    fun test_Null_construction() {
        val actual = NullableWidgetBinding(null)

        assertNull(actual.thing)
        assertEquals(NullableWidget::class.supertypes, NullableWidgetBinding::class.supertypes)
    }
}