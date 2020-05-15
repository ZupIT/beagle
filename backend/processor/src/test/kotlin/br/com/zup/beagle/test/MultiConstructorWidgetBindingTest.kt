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
import java.util.*
import kotlin.test.assertEquals

internal class MultiConstructorWidgetBindingTest {
    @Test
    fun test_Value_construction() {
        val value = UUID.randomUUID().toString()

        val actual = MultiConstructorWidgetBinding(Bind.Value(value))

        assertEquals(value, actual.value.value)
        assertEquals(MultiConstructorWidget::class.supertypes, MultiConstructorWidgetBinding::class.supertypes)
    }

    @Test
    fun test_Expression_construction() {
        val expression = "@{}"

        val actual = MultiConstructorWidgetBinding(Bind.Expression(expression))

        assertEquals(expression, actual.value.value)
        assertEquals(MultiConstructorWidget::class.supertypes, MultiConstructorWidgetBinding::class.supertypes)
    }
}