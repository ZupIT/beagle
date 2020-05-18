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

import br.com.zup.beagle.core.Binding
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ConstructorWidgetBindingTest {
    @Test
    fun test_Value_construction() {
        val value = object {}

        val actual = ConstructorWidgetBinding(Binding.Value(value))

        assertEquals(value, actual.something.value)
        assertEquals(ConstructorWidget::class.supertypes, ConstructorWidgetBinding::class.supertypes)
    }

    @Test
    fun test_Expression_construction() {
        val expression = "@{}"

        val actual = ConstructorWidgetBinding(Binding.Expression(expression))

        assertEquals(expression, actual.something.value)
        assertEquals(ConstructorWidget::class.supertypes, ConstructorWidgetBinding::class.supertypes)
    }
}