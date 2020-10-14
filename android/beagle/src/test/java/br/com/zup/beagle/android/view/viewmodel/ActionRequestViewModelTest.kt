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

package br.com.zup.beagle.android.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.zup.beagle.android.action.SendRequestInternal
import br.com.zup.beagle.android.data.ActionRequester
import br.com.zup.beagle.android.exception.BeagleApiException
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.networking.ResponseData
import br.com.zup.beagle.android.testutil.CoroutineTestRule
import br.com.zup.beagle.android.view.mapper.toRequestData
import br.com.zup.beagle.android.view.mapper.toResponse
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ActionRequestViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val scope = CoroutineTestRule()

    private val actionRequester: ActionRequester = mockk()

    private val observer: Observer<FetchViewState> = mockk()

    private val action: SendRequestInternal = mockk()

    private lateinit var viewModel: ActionRequestViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic("br.com.zup.beagle.android.view.mapper.SendRequestActionMapperKt")

        viewModel = ActionRequestViewModel(actionRequester = actionRequester)

        every { observer.onChanged(any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should emit success when fetch data`() {
        // Given
        val response: ResponseData = mockk(relaxed = true)
        val responseMapped: Response = mockk()
        every { action.toRequestData() } returns mockk()
        every { response.toResponse() } returns responseMapped
        coEvery { actionRequester.fetchData(any()) } returns response

        // When
        viewModel.fetch(action).observeForever(observer)

        // Then
        verify(exactly = once()) {
            observer.onChanged(FetchViewState.Success(responseMapped))
        }
    }

    @Test
    fun `should emit fail when fetch data`() {
        // Given
        val error: BeagleApiException = mockk()
        val responseData: ResponseData = mockk(relaxed = true)
        val responseMapped: Response = mockk()
        every { action.toRequestData() } returns mockk()
        every { responseData.toResponse() } returns responseMapped
        every { error.responseData } returns  responseData
        coEvery { actionRequester.fetchData(any()) } throws error

        // When
        viewModel.fetch(action).observeForever(observer)

        // Then
        verify(exactly = once()) {
            observer.onChanged(FetchViewState.Error(responseMapped))
        }
    }
}
