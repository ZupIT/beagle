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

package br.com.zup.beagle.android.components

import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SpacerTest : BaseComponentTest() {

    private lateinit var spacer: Spacer

    override fun setUp() {
        super.setUp()

        spacer = Spacer(10.0)
    }

    @Test
    fun build() {
        // Given
        val beagleFlexView = mockk<BeagleFlexView>()
        val styleSlot = slot<Style>()
        every { anyConstructed<ViewFactory>().makeBeagleFlexView(rootView.getContext(),
            capture(styleSlot)) } returns beagleFlexView

        // When
        val actual = spacer.buildView(rootView)

        // Then
        assertNotNull(actual)
        assertEquals(10.0, styleSlot.captured.size?.width?.value)
        assertEquals(10.0, styleSlot.captured.size?.height?.value)
    }
}