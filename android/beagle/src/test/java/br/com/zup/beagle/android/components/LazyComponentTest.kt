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

import android.view.View
import androidx.core.view.get
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleView
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertTrue

private val URL = RandomData.httpUrl()

class LazyComponentTest : BaseComponentTest() {

    private val initialStateView: View = mockk()
    private val initialState: ServerDrivenComponent = mockk()
    private val beagleView: BeagleView = mockk(relaxed = true)

    private lateinit var lazyComponent: LazyComponent

    override fun setUp() {
        super.setUp()

        every { anyConstructed<ViewFactory>().makeBeagleView(any()) } returns beagleView
        every { beagleView[0] } returns initialStateView

        lazyComponent = LazyComponent(URL, initialState)
    }

    @Test
    fun build_should_call_make_a_BeagleView() {
        val actual = lazyComponent.buildView(rootView)

        assertTrue(actual is BeagleView)
    }

    @Test
    fun build_should_add_initialState_and_trigger_updateView() {
        lazyComponent.buildView(rootView)

        verify(exactly = once()) { beagleView.addServerDrivenComponent(initialState) }
        verify(exactly = once()) { beagleView.updateView(URL, initialStateView) }
    }
}