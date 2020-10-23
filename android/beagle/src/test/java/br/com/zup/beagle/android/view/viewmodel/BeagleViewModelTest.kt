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
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.data.ActionRequester
import br.com.zup.beagle.android.data.ComponentRequester
import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.CoroutineTestRule
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class BeagleViewModelTest : BaseTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val scope = CoroutineTestRule()

    @MockK
    private lateinit var component: ServerDrivenComponent

    @MockK
    private lateinit var action: Action

    @MockK
    private lateinit var componentRequester: ComponentRequester

    @MockK
    private lateinit var actionRequester: ActionRequester

    @MockK
    private lateinit var observer: Observer<ViewState>

    private lateinit var beagleUIViewModel: BeagleViewModel

    private val slotViewState = mutableListOf<ViewState>()

    override fun setUp() {
        super.setUp();

        beagleUIViewModel = BeagleViewModel(componentRequester = componentRequester)

        coEvery { componentRequester.fetchComponent(any()) } returns component
        coEvery { actionRequester.fetchAction(any()) } returns action
        every { observer.onChanged(any()) } just Runs
        coEvery { observer.onChanged(capture(slotViewState)) } just Runs
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `GIVEN a screenRequest WHEN fetch SHOULD return render ViewState`() {
        // Given
        val screenRequest = ScreenRequest(RandomData.httpUrl())

        // When
        beagleUIViewModel.fetchComponent(screenRequest).observeForever(observer)

        // Then
        coVerifyOrder {
            observer.onChanged(ViewState.Loading(true))
            observer.onChanged(ViewState.DoRender(screenRequest.url, component))
            observer.onChanged(ViewState.Loading(false))
        }
    }

    @Test
    fun `GIVEN a screenRequest throws exception WHEN fetch SHOULD return a error ViewState`() {
        // Given
        val screenRequest = ScreenRequest(RandomData.httpUrl())
        val exception = BeagleException("Error")
        coEvery { componentRequester.fetchComponent(any()) } throws exception

        // When
        beagleUIViewModel.fetchComponent(screenRequest).observeForever(observer)

        // Then
        coVerifySequence {
            observer.onChanged(ViewState.Loading(true))
            observer.onChanged(any<ViewState.Error>())
            observer.onChanged(ViewState.Loading(false))
        }
    }

    @Test
    fun `GIVEN a screenRequest throws exception WHEN fetch SHOULD return a error ViewState retry`() {
        // Given
        val screenRequest = ScreenRequest(RandomData.httpUrl())
        val exception = BeagleException("Error")

        coEvery { componentRequester.fetchComponent(any()) } throws exception andThen component

        // When
        beagleUIViewModel.fetchComponent(screenRequest, null).observeForever(observer)
        (slotViewState[1] as ViewState.Error).retry.invoke()

        // Then
        coVerifyOrder {
            observer.onChanged(ViewState.Loading(true))
            observer.onChanged(any<ViewState.Error>())
            observer.onChanged(ViewState.Loading(false))
            observer.onChanged(ViewState.Loading(true))
            observer.onChanged(ViewState.DoRender(screenRequest.url, component))
            observer.onChanged(ViewState.Loading(false))
        }
    }

    @Test
    fun `GIVEN a ServerDrivenComponent WHEN fetchComponents called SHOULD post ViewState doRender `() {
        //GIVEN
        val screenRequest = ScreenRequest("")

        //WHEN
        beagleUIViewModel.fetchComponent(screenRequest, component).observeForever(observer)

        //THEN
        verify(exactly = once()) { observer.onChanged(ViewState.DoRender(null, component)) }
    }

    @Test
    fun `GIVEN a IdentifierComponent WHEN fetchComponents called SHOULD post ViewState doRender `() {
        //GIVEN
        val screenRequest = ScreenRequest("")
        val component: IdentifierComponent = mockk()
        val id = "id"
        every { component.id } returns id

        //WHEN
        beagleUIViewModel.fetchComponent(screenRequest, component).observeForever(observer)

        //THEN
        verify(exactly = once()) { observer.onChanged(ViewState.DoRender(id, component)) }
    }

    @Test
    fun `GIVEN a NULL ScreenComponent WHEN fetchComponents called SHOULD post ViewState doRender `() {
        //GIVEN
        val screenRequest = ScreenRequest("url")


        //WHEN
        beagleUIViewModel.fetchComponent(screenRequest, null).observeForever(observer)

        //THEN
        verify(exactly = once()) { observer.onChanged(ViewState.DoRender(screenRequest.url, component)) }
    }

    @Test
    fun `GIVEN screen with full path WHEN fetchComponents called SHOULD post ViewState doRender with correct screen id`() {
        //GIVEN
        every { beagleSdk.config.baseUrl } returns "http://localhost:2020/"

        val screenRequest = ScreenRequest("http://localhost:2020/test")

        //WHEN
        beagleUIViewModel.fetchComponent(screenRequest, null).observeForever(observer)

        //THEN
        verify(exactly = once()) { observer.onChanged(ViewState.DoRender("test", component)) }
    }

    @Test
    fun `GIIVEN a ScreenComponent WHEN fetchComponent called SHOULD use identifier as screenId on ViewState doRender`() {
        //Given
        val screenRequest = ScreenRequest("")
        val component: ScreenComponent = mockk()
        val id = "id"
        val identifier = "identifier"

        every { component.id } returns id
        every { component.identifier } returns identifier

        //WHEN
        beagleUIViewModel.fetchComponent(screenRequest, component).observeForever(observer)

        //THEN
        verify(exactly = once()) { observer.onChanged(ViewState.DoRender(identifier, component)) }

    }

    @Test
    fun `GIVEN a NULL FetchComponentLiveData WHEN isFetchComponent called SHOULD return false`() {
        //Given
        beagleUIViewModel.fetchComponent = null

        //WHEN
        val isFetch = beagleUIViewModel.isFetchComponent()

        //THEN
        assertFalse { isFetch }
    }

    @Test
    fun `GIVEN a NULL Job in FetchComponentLiveData WHEN isFetchComponent called SHOULD return false`() {
        //Given
        val screenRequest = ScreenRequest("")

        beagleUIViewModel.fetchComponent(screenRequest)
        beagleUIViewModel.fetchComponent?.job = null

        // When
        val isFetch = beagleUIViewModel.isFetchComponent()

        //THEN
        assertFalse { isFetch }
    }

    @Test
    fun `GIVEN a Job COMPLETED in FetchComponentLiveData WHEN isFetchComponent called SHOULD return false`() {
        //Given
        val screenRequest = ScreenRequest("")
        val mockJob = Job()
        mockJob.complete()

        beagleUIViewModel.fetchComponent(screenRequest)
        beagleUIViewModel.fetchComponent?.job = mockJob

        // When
        val isFetch = beagleUIViewModel.isFetchComponent()

        //THEN
        assertFalse { isFetch }
    }

    @Test
    fun `GIVEN a Job NOT COMPLETED in FetchComponentLiveData WHEN isFetchComponent called SHOULD post ViewState doCancel and return true`() {
        //Given
        val screenRequest = ScreenRequest("")
        val mockJob = Job()

        beagleUIViewModel.fetchComponent(screenRequest).observeForever(observer)
        beagleUIViewModel.fetchComponent?.job = mockJob

        // When
        val isFetch = beagleUIViewModel.isFetchComponent()

        //THEN
        assertTrue { mockJob.isCancelled }
        verify(exactly = once()) { observer.onChanged(ViewState.DoCancel) }
        assertTrue { isFetch }
    }

}
