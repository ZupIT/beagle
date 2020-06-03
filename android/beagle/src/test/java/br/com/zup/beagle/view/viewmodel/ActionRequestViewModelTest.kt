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

package br.com.zup.beagle.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.zup.beagle.action.Action
import br.com.zup.beagle.action.SendRequestAction
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.data.ActionRequester
import br.com.zup.beagle.data.ComponentRequester
import br.com.zup.beagle.exception.BeagleException
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.networking.ResponseData
import br.com.zup.beagle.testutil.CoroutineTestRule
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.utils.CoroutineDispatchers
import br.com.zup.beagle.utils.generateViewModelInstance
import br.com.zup.beagle.view.ScreenRequest
import br.com.zup.beagle.view.mapper.SendRequestActionMapper
import br.com.zup.beagle.view.mapper.toRequestData
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
class ActionRequestViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val scope = CoroutineTestRule()

    private val actionRequester: ActionRequester = mockk()

    private val observer: Observer<ActionRequestViewModel.FetchViewState> = mockk()

    private val action: SendRequestAction = mockk()

    @InjectMockKs
    private lateinit var viewModel: ActionRequestViewModel


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic("br.com.zup.beagle.view.mapper.SendRequestActionMapperKt")

        every { observer.onChanged(any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should emit success when fetch data`() {
        // Given
        val response: ResponseData = mockk()
        every { action.toRequestData() } returns mockk()
        coEvery { actionRequester.fetchData(any()) } returns response

        // When
        viewModel.fetch(action).observeForever(observer)

        // Then
        verify(exactly = once()) {
            observer.onChanged(ActionRequestViewModel.FetchViewState.Success(response))
        }
    }

    @Test
    fun `should emit fail when fetch data`() {
        // Given
        every { action.toRequestData() } returns mockk()
        val exception = BeagleException("Error")
        coEvery { actionRequester.fetchData(any()) } throws exception

        // When
        viewModel.fetch(action).observeForever(observer)

        // Then
        verify(exactly = once()) {
            observer.onChanged(ActionRequestViewModel.FetchViewState.Error(exception))
        }
    }

    @Test
    fun `should clean when cancel`() {
        // Given
        val viewModelSpy = spyk(viewModel)
        every { viewModelSpy.cancel() } just Runs

        // When
        viewModelSpy.onCleared()

        // Then
        verify(exactly = once()) { viewModelSpy.cancel() }
    }
}
