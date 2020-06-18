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
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeaglePageView
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class PageViewTest : BaseComponentTest() {

    private val beaglePageView: BeaglePageView = mockk(relaxed = true)
    private val pageIndicatorComponent: PageIndicatorComponent = mockk(relaxed = true)
    private val children = listOf<ServerDrivenComponent>(mockk<Button>())

    private lateinit var pageView: PageView

    override fun setUp() {
        super.setUp()

        every { beagleFlexView.addView(any()) } just Runs
        every { anyConstructed<ViewFactory>().makeViewPager(any()) } returns beaglePageView
    }

    @Test
    fun build_when_page_indicator_is_null() {
        // GIVEN
        pageView = PageView(children, null)

        // WHEN
        pageView.buildView(rootView)

        // THEN
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeViewPager(any()) }
        verify(atLeast = 2) { beagleFlexView.addView(any()) }
        verify(atLeast = 2) { anyConstructed<ViewFactory>().makeBeagleFlexView(any()) }
    }

    @Test
    fun build_when_page_indicator_is_not_null() {
        // GIVEN
        pageView = PageView(children, pageIndicatorComponent)
        // WHEN
        pageView.buildView(rootView)

        // THEN
        verify(exactly = 3) { beagleFlexView.addView(any()) }
    }
}
