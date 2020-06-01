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
import java.util.UUID
import kotlin.test.assertEquals

internal class TransformingWidgetBindingTest {
    @Test
    fun test_Value_construction() {
        val message = UUID.randomUUID().toString()

        val actual = TransformingWidgetBinding(Bind.Value(message))

        assertEquals(message, actual.message.value)
        assertEquals(TransformingWidget::class.supertypes, TransformingWidgetBinding::class.supertypes)
    }

    @Test
    fun test_Expression_construction() {
        val expression = "@{}"

        val actual = TransformingWidgetBinding(Bind.Expression(expression))

        assertEquals(expression, actual.message.value)
        assertEquals(TransformingWidget::class.supertypes, TransformingWidgetBinding::class.supertypes)
    }
}