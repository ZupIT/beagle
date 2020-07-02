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

package br.com.zup.beagle.android.components.page

import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.viewFactory
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeaglePageView
import br.com.zup.beagle.core.Style
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class PageViewTest : BaseComponentTest() {

    private val beaglePageView: BeaglePageView = mockk(relaxed = true)
    private val pageIndicatorComponent: PageIndicatorComponent = mockk(relaxed = true)
    private val children = listOf<ServerDrivenComponent>(mockk<Button>())

    private lateinit var pageView: PageView

    private val styleSlot = mutableListOf<Style>()

    override fun setUp() {
        super.setUp()

        every { anyConstructed<ViewFactory>().makeViewPager(any()) } returns beaglePageView
        every { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), capture(styleSlot)) } returns beagleFlexView
        every { beagleFlexView.addView(any(), capture(styleSlot)) } just Runs
    }

    @Test
    fun `buildView should use style with grow 1`() {
        // Given
        pageView = PageView(children, null)

        // When
        pageView.buildView(rootView)

        // Then
        assertEquals(1.0, styleSlot[0].flex?.grow)
        assertEquals(1.0, styleSlot[1].flex?.grow)
    }

    @Test
    fun `buildView should make and addView once when indicator is null`() {
        // GIVEN
        pageView = PageView(children, null)

        // WHEN
        pageView.buildView(rootView)

        // THEN
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeViewPager(any()) }
        verify(atLeast = once()) { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), styleSlot[0]) }
        verify(atLeast = once()) { beagleFlexView.addView(any(), styleSlot[1]) }
    }

    @Test
    fun `buildView should call addView when indicator is not null`() {
        // GIVEN
        pageView = PageView(children, pageIndicatorComponent)

        // WHEN
        pageView.buildView(rootView)

        // THEN
        verify(exactly = once()) { beagleFlexView.addView(any()) }
    }
}
