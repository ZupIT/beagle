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
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.viewmodel.BeagleViewModel
import io.mockk.Runs
import io.mockk.called
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PreFetchHelperTest : BaseTest() {

    private val helper = PreFetchHelper()
    private val route = Route.Remote(RandomData.string(), shouldPrefetch = true)
    private val cachedTypes =
        listOf(
            Navigate.PushStack(route),
            Navigate.PushView(route),
            Navigate.ResetApplication(route),
            Navigate.ResetStack(route)
        )

    @MockK
    private lateinit var beagleViewModel: BeagleViewModel

    @BeforeEach
    override fun setUp() {
        super.setUp()

        prepareViewModelMock(beagleViewModel)

        coEvery { beagleViewModel.fetchForCache(any()) } returns mockk()

        mockkObject(BeagleMessageLogs)
        every { BeagleMessageLogs.expressionNotSupportInPreFetch() } just Runs

    }

    @Test
    fun should_call_fetch_for_cache_test() {
        cachedTypes.forEach {
            helper.handlePreFetch(rootView, it)
            verify { beagleViewModel.fetchForCache(route.url.value as String) }
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

    @Test
    fun prefetch_log_should_be_displayed() {
        //GIVEN
        val route = Route.Remote(expressionOf("http://@{test}"), shouldPrefetch = true)
        val navigation = Navigate.PushView(route)

        //WHEN
        helper.handlePreFetch(rootView, navigation)

        //THEN
        verify { BeagleMessageLogs.expressionNotSupportInPreFetch() }
    }

    @Test
    fun should_not_call_fetch_for_cache_when_url_has_an_expression() {
        cachedTypes.forEach {
            route.url is Bind.Expression<String>
            val url = RandomData.string()
            helper.handlePreFetch(rootView, it)
            verify { beagleViewModel.fetchForCache(url) wasNot called }
        }
    }


}
