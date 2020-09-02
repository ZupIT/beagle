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
import br.com.zup.beagle.android.context.ContextBinding
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.extensions.once
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class ViewExtensionsKtTest : BaseTest() {

    private val view: View = mockk()

    private val contextData = ContextData("contextId", "stub")

    @Test
    fun setContextData_with_contextBinding_should_clear_cache() {
        // Given
        val contextBinding = mockk<ContextBinding>(relaxed = true)
        val contextSlot  = slot<ContextData>()
        every { view.getContextBinding() } returns contextBinding
        every { contextBinding.context = capture(contextSlot) } just Runs

        // When
        view.setContextData(contextData)

        // Then
        assertEquals(contextSlot.captured, contextData)
        verify(exactly = once()) { contextBinding.cache.evictAll() }
    }

    @Test
    fun setContextData_without_contextBinding_should_set_contextBinding() {
        // Given
        val bindingSlot = slot<ContextBinding>()
        every { view.getContextBinding() } returns null
        every { view.setContextBinding(capture(bindingSlot)) } just Runs

        // When
        view.setContextData(contextData)

        // Then
        assertEquals(bindingSlot.captured.context, contextData)
    }
}
