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

import br.com.zup.beagle.action.Action
import br.com.zup.beagle.data.serializer.BeagleSerializer
import br.com.zup.beagle.data.serializer.makeCustomActionJson
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.networking.ResponseData
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.view.ScreenRequest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

private val URL = RandomData.httpUrl()
private val JSON_SUCCESS = makeCustomActionJson()

@ExperimentalCoroutinesApi
class ActionRequesterTest {

    @MockK
    private lateinit var beagleApi: BeagleApi
    @MockK
    private lateinit var serializer: BeagleSerializer

    private lateinit var actionRequester: ActionRequester

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        actionRequester = ActionRequester(beagleApi, serializer)
    }

    @Test
    fun fetchAction_should_deserialize_a_action_response() = runBlockingTest {
        // Given
        val action = mockk<Action>()
        val responseData = mockk<ResponseData> {
            every { data } returns JSON_SUCCESS.toByteArray()
        }
        coEvery { beagleApi.fetchData(ScreenRequest(URL)) } returns responseData
        every { serializer.deserializeAction(JSON_SUCCESS) } returns action

        // When
        val actionResult = actionRequester.fetchAction(URL)

        // Then
        verify(exactly = once()) { serializer.deserializeAction(JSON_SUCCESS) }
        assertEquals(action, actionResult)
    }
}