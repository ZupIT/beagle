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

package br.com.zup.beagle.data

import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.action.NavigationType.ADD_VIEW
import br.com.zup.beagle.action.NavigationType.PRESENT_VIEW
import br.com.zup.beagle.action.NavigationType.SWAP_VIEW
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.utils.generateViewModelInstance
import io.mockk.called
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.Test

class PreFetchHelperTest : BaseTest() {

    private val helper = PreFetchHelper()
    @MockK
    private lateinit var rootView: RootView
    private val cachedTypes =
        listOf(
            ADD_VIEW,
            SWAP_VIEW,
            PRESENT_VIEW
        )

    @MockK
    private lateinit var beagleViewModel: BeagleViewModel

    override fun setUp() {
        super.setUp()

        mockkStatic("br.com.zup.beagle.utils.ViewExtensionsKt")
        every { rootView.generateViewModelInstance() } returns beagleViewModel
        coEvery { beagleViewModel.fetchForCache(any()) } returns mockk()
    }

    override fun tearDown() {
        super.tearDown()
        unmockkStatic("br.com.zup.beagle.utils.ViewExtensionsKt")
    }

    @Test
    fun should_call_fetch_for_cache_test() {
        cachedTypes.forEach {
            val url = RandomData.string()
            helper.handlePreFetch(rootView, Navigate(type = it, path = url, shouldPrefetch = true))
            verify(exactly = once()) { beagleViewModel.fetchForCache(url) }
        }
    }

    @Test
    fun should_not_call_fetch_for_cache_test() {
        cachedTypes.forEach {
            val url = RandomData.string()
            helper.handlePreFetch(rootView, Navigate(type = it, path = url))
            verify { beagleViewModel.fetchForCache(url) wasNot called }
        }
    }
}