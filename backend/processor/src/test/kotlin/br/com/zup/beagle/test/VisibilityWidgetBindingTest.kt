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
import kotlin.random.Random
import kotlin.test.assertEquals

internal class VisibilityWidgetBindingTest {
    companion object {
        val B = Random.Default.nextBytes(1)[0]
        val D = Random.Default.nextLong()
        const val EXPRESSION = "@{}"
    }

    @Test
    fun test_Value_construction() {
        val actual = VisibilityWidgetBinding(Bind.Value(B), Bind.Value(D))

        assertEquals(B, actual.b.value)
        assertEquals(D, actual.d.value)
        assertEquals(VisibilityWidget::class.supertypes, VisibilityWidgetBinding::class.supertypes)
    }

    @Test
    fun test_Expression_construction() {
        val actual = VisibilityWidgetBinding(Bind.Expression(EXPRESSION), Bind.Expression(EXPRESSION))

        assertEquals(EXPRESSION, actual.b.value)
        assertEquals(EXPRESSION, actual.d.value)
        assertEquals(VisibilityWidget::class.supertypes, VisibilityWidgetBinding::class.supertypes)
    }

    @Test
    fun test_Mixed_construction() {
        val actual = VisibilityWidgetBinding(Bind.Expression(EXPRESSION), Bind.Value(D))

        assertEquals(EXPRESSION, actual.b.value)
        assertEquals(D, actual.d.value)
        assertEquals(VisibilityWidget::class.supertypes, VisibilityWidgetBinding::class.supertypes)
    }
}