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

import androidx.lifecycle.Observer
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.data.ActionRequester
import br.com.zup.beagle.android.data.ComponentRequester
import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.testutil.CoroutinesTestExtension
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import br.com.zup.beagle.android.testutil.RandomData
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@DisplayName("Given a BeagleViewModel")
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class BeagleViewModelTest : BaseTest() {

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

    @BeforeEach
    override fun setUp() {
        super.setUp()

        beagleUIViewModel = BeagleViewModel(componentRequester = componentRequester)

        coEvery { componentRequester.fetchComponent(any()) } returns component
        coEvery { actionRequester.fetchAction(any()) } returns action
        every { observer.onChanged(any()) } just Runs
        coEvery { observer.onChanged(capture(slotViewState)) } just Runs
    }

    @DisplayName("When FetchComponent")
    @Nested
    inner class FetchComponent {
        @DisplayName("Then should post render ViewState")
        @Test
        @Suppress("UNCHECKED_CAST")
        fun testGivenAScreenRequestWhenFetchComponentShouldPostRenderViewState() {
            // Given
            val screenRequest = RequestData(url = RandomData.httpUrl())

            // When
            beagleUIViewModel.fetchComponent(screenRequest).observeForever(observer)

            // Then
            coVerifyOrder {
                observer.onChanged(ViewState.Loading(true))
                observer.onChanged(ViewState.DoRender(screenRequest.url, component))
                observer.onChanged(ViewState.Loading(false))
            }
        }

        @DisplayName("Then should post a error ViewState")
        @Test
        fun testGivenAScreenRequestThrowsExceptionWhenFetchShouldPostAErrorViewState() {
            // Given
            val screenRequest = RequestData(url = RandomData.httpUrl())
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

        @DisplayName("Then should post a error ViewState retry")
        @Test
        fun testGivenAScreenRequestThrowsExceptionWhenFetchShouldPostAErrorViewStateRetry() {
            // Given
            val screenRequest = RequestData(url = RandomData.httpUrl())
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

        @DisplayName("Then should post a ViewState doRender")
        @Test
        fun testGivenAServerDrivenComponentWhenFetchComponentsCalledShouldPostViewStateDoRender() {
            //GIVEN
            val screenRequest = RequestData(url = "")

            //WHEN
            beagleUIViewModel.fetchComponent(screenRequest, component).observeForever(observer)

            //THEN
            verify(exactly = once()) { observer.onChanged(ViewState.DoRender(null, component)) }
        }

        @DisplayName("Then should post a ViewStateDoRender")
        @Test
        fun testGivenAIdentifierComponentWhenFetchComponentsCalledShouldPostViewStateDoRender() {
            //GIVEN
            val screenRequest = RequestData(url = "")
            val component: IdentifierComponent = mockk()
            val id = "id"
            every { component.id } returns id

            //WHEN
            beagleUIViewModel.fetchComponent(screenRequest, component).observeForever(observer)

            //THEN
            verify(exactly = once()) { observer.onChanged(ViewState.DoRender(id, component)) }
        }

        @DisplayName("Then should post ViewState doRender")
        @Test
        fun testGivenANullScreenComponentWhenFetchComponentsCalledShouldPostViewStateDoRender() {
            //GIVEN
            val screenRequest = RequestData(url = "url")


            //WHEN
            beagleUIViewModel.fetchComponent(screenRequest, null).observeForever(observer)

            //THEN
            verify(exactly = once()) { observer.onChanged(ViewState.DoRender(screenRequest.url, component)) }
        }

        @DisplayName("Then should post ViewState doRender with correct screenId")
        @Test
        fun testGivenScreenWithFullPathWhenFetchComponentsCalledShouldPostViewStateDoRenderWithCorrectScreenId() {
            //GIVEN
            every { beagleSdk.config.baseUrl } returns "http://localhost:2020/"

            val screenRequest = RequestData(url = "http://localhost:2020/test")

            //WHEN
            beagleUIViewModel.fetchComponent(screenRequest, null).observeForever(observer)

            //THEN
            verify(exactly = once()) { observer.onChanged(ViewState.DoRender(screenRequest.url, component)) }
        }

        @DisplayName("Then should post ViewState doRender using identifier as screenId")
        @Test
        fun testGivenAScreenComponentWhenFetchComponentCalledShouldPostViewStateDoRenderUsingIdentifierAsScreenId() {
            //Given
            val screenRequest = RequestData(url = "")
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
    }

    @DisplayName("When isFetchComponent")
    @Nested
    inner class IsFetchCompoenent {

        @DisplayName("Then should return false")
        @Test
        fun testGivenANullFetchComponentLiveDataWhenIsFetchComponentCalledShouldReturnFalse() {
            //Given
            beagleUIViewModel.fetchComponent = null

            //WHEN
            val isFetch = beagleUIViewModel.isFetchComponent()

            //THEN
            assertFalse { isFetch }
        }

        @DisplayName("Then should return false")
        @Test
        fun testGivenANullJobInFetchComponentLiveDataWhenIsFetchComponentCalledShouldReturnFalse() {
            //Given
            val screenRequest = RequestData(url = "")

            beagleUIViewModel.fetchComponent(screenRequest)
            beagleUIViewModel.fetchComponent?.job = null

            // When
            val isFetch = beagleUIViewModel.isFetchComponent()

            //THEN
            assertFalse { isFetch }
        }

        @DisplayName("Then should return false")
        @Test
        fun testGivenAJobCompletedInFetchComponentLiveDataWhenIsFetchComponentCalledShouldReturnFalse() {
            //Given
            val screenRequest = RequestData(url = "")
            val mockJob = Job()
            mockJob.complete()

            beagleUIViewModel.fetchComponent(screenRequest)
            beagleUIViewModel.fetchComponent?.job = mockJob

            // When
            val isFetch = beagleUIViewModel.isFetchComponent()

            //THEN
            assertFalse { isFetch }
        }

        @DisplayName("Then should post ViewState doCancel and return true")
        @Test
        fun testGivenAJobNotCompletedInFetchComponentLiveDataWhenIsFetchComponentCalledShouldPostViewStateDoCancelAndReturnTrue() {
            //Given
            val screenRequest = RequestData(url = "")
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
}
