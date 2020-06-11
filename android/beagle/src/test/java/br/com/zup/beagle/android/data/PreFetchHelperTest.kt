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

package br.com.zup.beagle.android.data

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.action.Route
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.widget.core.RootView
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
    private val route = Route.Remote(route = RandomData.string(), shouldPrefetch = true)
    private val cachedTypes =
        listOf(
            Navigate.PushStack(route),
            Navigate.PushView(route),
            Navigate.ResetApplication(route),
            Navigate.ResetStack(route)
        )

    @MockK
    private lateinit var beagleViewModel: BeagleViewModel

    override fun setUp() {
        super.setUp()

        mockkStatic("br.com.zup.beagle.android.utils.RootViewKt")
        every { rootView.generateViewModelInstance() } returns beagleViewModel
        coEvery { beagleViewModel.fetchForCache(any()) } returns mockk()
    }

    override fun tearDown() {
        super.tearDown()
        unmockkStatic("br.com.zup.beagle.android.utils.RootViewKt")
    }

    @Test
    fun should_call_fetch_for_cache_test() {
        cachedTypes.forEach {
            helper.handlePreFetch(rootView, it)
            verify { beagleViewModel.fetchForCache(route.route) }
        }
    }

    @Test
    fun should_not_call_fetch_for_cache_test() {
        cachedTypes.forEach {
            val url = RandomData.string()
            helper.handlePreFetch(rootView, it)
            verify { beagleViewModel.fetchForCache(url) wasNot called }
        }
    }
}
