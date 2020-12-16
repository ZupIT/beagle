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

import android.view.View
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextBinding
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith

@DisplayName("Given a View")
class OnInitiableComponentTest : BaseTest() {

    private val view: View = mockk()

    private val contextData = ContextData("contextId", "stub")

    @DisplayName("When setContextData is called")
    @Nested
    inner class SetContextData {

        @DisplayName("Then should set bindings")
        @Test
        fun checkIfContextBindingWasSet() {
            // Given
            val contextBinding = mockk<ContextBinding>(relaxed = true)
            val bindingSlot = slot<ContextBinding>()
            every { view.getContextBinding() } returns contextBinding
            every { view.setContextBinding(capture(bindingSlot)) } just Runs

            // When
            view.setContextData(contextData)

            // Then
            assertEquals(bindingSlot.captured.context, contextData)
            assertEquals(bindingSlot.captured.bindings, contextBinding.bindings)
        }

        @DisplayName("Then should not set bindings")
        @Test
        fun checkIfContextBindingWasNotSet() {
            // Given
            val expectedBindings = mutableSetOf<Bind<*>>()
            val bindingSlot = slot<ContextBinding>()
            every { view.getContextBinding() } returns null
            every { view.setContextBinding(capture(bindingSlot)) } just Runs

            // When
            view.setContextData(contextData)

            // Then
            assertEquals(bindingSlot.captured.context, contextData)
            assertEquals(bindingSlot.captured.bindings, expectedBindings)
        }
    }
}
