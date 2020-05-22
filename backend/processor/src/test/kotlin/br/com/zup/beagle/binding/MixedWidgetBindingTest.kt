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

package br.com.zup.beagle.binding

import br.com.zup.beagle.core.Bind
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals

internal class MixedWidgetBindingTest {
    companion object {
        val OTHER = Random.Default.nextDouble()
        val SOMETHING = object {}
        const val EXPRESSION = "@{}"
    }

    @Test
    fun test_Value_construction() {
        val actual = MixedWidgetBinding(Bind.Value(OTHER), Bind.Value(SOMETHING))

        assertEquals(OTHER, actual.other.value)
        assertEquals(SOMETHING, actual.something.value)
        assertEquals(MixedWidget::class.supertypes, MixedWidgetBinding::class.supertypes)
    }

    @Test
    fun test_Expression_construction() {
        val actual = MixedWidgetBinding(Bind.Expression(EXPRESSION), Bind.Expression(EXPRESSION))

        assertEquals(EXPRESSION, actual.other.value)
        assertEquals(EXPRESSION, actual.something.value)
        assertEquals(MixedWidget::class.supertypes, MixedWidgetBinding::class.supertypes)
    }

    @Test
    fun test_Mixed_construction() {
        val actual = MixedWidgetBinding(Bind.Value(OTHER), Bind.Expression(EXPRESSION))

        assertEquals(OTHER, actual.other.value)
        assertEquals(EXPRESSION, actual.something.value)
        assertEquals(MixedWidget::class.supertypes, MixedWidgetBinding::class.supertypes)
    }
}