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

import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.Bind
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.Flex
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.random.Random
import kotlin.test.assertEquals

internal class OverrideParentWidgetBindingTest {
    companion object {
        val A = Random.Default.nextBoolean()
        val B = Random.Default.nextLong()
        val C = UUID.randomUUID().toString()
        val ACCESSIBILITY = Accessibility()
        val FLEX = Flex()
        val STYLE = Style()
        val CHILD = object : ServerDrivenComponent {}
        const val EXPRESSION = "@{}"
    }

    @Test
    fun test_Value_construction() {
        val actual = OverrideParentWidgetBinding(
            Bind.Value(A),
            Bind.Value(B),
            Bind.Value(C),
            ACCESSIBILITY,
            STYLE,
            FLEX,
            CHILD
        )

        assertEquals(A, actual.a.value)
        assertEquals(B, actual.b.value)
        assertEquals(C, actual.c.value)
        assertEquals(ACCESSIBILITY, actual.accessibility)
        assertInvariant(actual)
    }

    @Test
    fun test_Expression_construction() {
        val actual = OverrideParentWidgetBinding(
            Bind.Expression(EXPRESSION),
            Bind.Expression(EXPRESSION),
            Bind.Expression(EXPRESSION),
            ACCESSIBILITY,
            STYLE,
            FLEX,
            CHILD
        )

        assertEquals(EXPRESSION, actual.a.value)
        assertEquals(EXPRESSION, actual.b.value)
        assertEquals(EXPRESSION, actual.c.value)
        assertInvariant(actual)
    }

    @Test
    fun test_Mixed_construction() {
        val actual = OverrideParentWidgetBinding(
            Bind.Value(A),
            Bind.Expression(EXPRESSION),
            Bind.Value(C),
            ACCESSIBILITY,
            STYLE,
            FLEX,
            CHILD
        )

        assertEquals(A, actual.a.value)
        assertEquals(EXPRESSION, actual.b.value)
        assertEquals(C, actual.c.value)
        assertInvariant(actual)
    }

    private fun assertInvariant(actual: OverrideParentWidgetBinding) {
        assertEquals(STYLE, actual.style)
        assertEquals(FLEX, actual.flex)
        assertEquals(CHILD, actual.child)
        assertEquals(OverrideParentWidget::class.supertypes, OverrideParentWidgetBinding::class.supertypes)
    }
}