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

package br.com.zup.beagle.android.action

import android.view.View
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.logger.BeagleLoggerProxy
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.utils.handleEvent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test

class ConditionTest : BaseTest() {

    private val view: View = mockk()
    private val onFalse: Action = mockk(relaxed = true)
    private val onTrue: Action = mockk(relaxed = true)

    override fun setUp() {
        super.setUp()
        mockkObject(BeagleLoggerProxy)
        mockkStatic("br.com.zup.beagle.android.utils.ActionExtensionsKt")
    }

    @Test
    fun `execute should call onTrue attribute`() {
        // Given
        val condition = Condition(
            condition = expressionOf(""),
            onFalse = listOf(onFalse),
            onTrue = listOf(onTrue)
        )
        every { condition.evaluateExpression(rootView, view, condition.condition) } returns true
        every { condition.handleEvent(rootView, view, listOf(onTrue)) } just Runs

        // When
        condition.execute(rootView, view)

        // Then
        verify { condition.handleEvent(rootView, view, listOf(onTrue)) }
    }

    @Test
    fun `execute should call onFalse attribute`() {
        // Given
        val condition = Condition(
            condition = expressionOf(""),
            onFalse = listOf(onFalse),
            onTrue = listOf(onTrue)
        )
        every { condition.evaluateExpression(rootView, view, condition.condition) } returns false
        every { condition.handleEvent(rootView, view, listOf(onFalse)) } just Runs

        // When
        condition.execute(rootView, view)

        // Then
        verify { condition.handleEvent(rootView, view, listOf(onFalse)) }
    }

    @Test
    fun `execute should receive exception value and to call  onFalse attribute`() {
        // Given
        val result = mockk<Exception>()
        val condition = Condition(
            condition = expressionOf(""),
            onFalse = listOf(onFalse),
            onTrue = listOf(onTrue)
        )
        every { condition.evaluateExpression(rootView, view, condition.condition) } throws result
        every { condition.handleEvent(rootView, view, listOf(onFalse)) } just Runs
        every { BeagleLoggerProxy.warning(any()) } just Runs

        // When
        condition.execute(rootView, view)

        // Then
        verify { condition.handleEvent(rootView, view, listOf(onFalse)) }
        BeagleLoggerProxy.warning("Conditional action. Expected boolean or null. Received: ${condition.condition.value}")
    }

    @Test
    fun `execute should receive null value and to call  onFalse attribute`() {
        // Given
        val condition = Condition(
            condition = expressionOf(""),
            onFalse = listOf(onFalse),
            onTrue = listOf(onTrue)
        )
        every { condition.evaluateExpression(rootView, view, condition.condition) } returns null
        every { condition.handleEvent(rootView, view, listOf(onFalse)) } just Runs

        // When
        condition.execute(rootView, view)

        // Then
        verify { condition.handleEvent(rootView, view, listOf(onFalse)) }
    }
}