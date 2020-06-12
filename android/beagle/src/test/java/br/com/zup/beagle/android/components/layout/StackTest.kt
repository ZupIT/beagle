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

package br.com.zup.beagle.android.components.layout


import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexPositionType
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class StackTest : BaseComponentTest() {

    private val button = mockk<Button>()
    private val children: List<ServerDrivenComponent> = mutableListOf(button)

    private val clipChildren = slot<Boolean>()
    private val flex = slot<Flex>()

    private lateinit var stack: Stack

    @Test
    fun build() {
        // Given
        every { beagleFlexView.clipChildren = capture(clipChildren) } just Runs
        every { beagleFlexView.addView(any(), capture(flex)) } just Runs
        every { button.flex } returns null

        stack = Stack(children)

        // When
        stack.buildView(rootView)

        // Then
        assertEquals(false, clipChildren.captured)
        verify(exactly = once()) { beagleFlexView.addView(any(), Flex(positionType = FlexPositionType.ABSOLUTE)) }
    }
}