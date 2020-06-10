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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.beagle.action.Action
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.CoroutineDispatchers
import br.com.zup.beagle.android.view.ScreenRequest
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class BeagleViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var component: ServerDrivenComponent
    @MockK
    private lateinit var action: Action
    @MockK
    private lateinit var componentRequester: ComponentRequester
    @MockK
    private lateinit var actionRequester: ActionRequester

    @InjectMockKs
    private lateinit var beagleUIViewModel: BeagleViewModel

    private val viewStateResult: (t: ViewState) -> Unit = {
        viewModelStates.add(it)
    }

    private var viewModelStates: MutableList<ViewState> = mutableListOf()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        CoroutineDispatchers.Main = Dispatchers.Unconfined
        CoroutineDispatchers.Default = Dispatchers.Unconfined

        coEvery { componentRequester.fetchComponent(any()) } returns component
        coEvery { actionRequester.fetchAction(any()) } returns action

        viewModelStates.clear()

        beagleUIViewModel.state.observeForever(viewStateResult)
    }

    @After
    fun tearDown() {
        beagleUIViewModel.state.removeObserver(viewStateResult)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun fetch_should_return_render_ViewState() {
        // Given
        val screenRequest = ScreenRequest(RandomData.httpUrl())

        // When
        beagleUIViewModel.fetchComponent(screenRequest)

        // Then
        assertLoading(viewModelStates[0], true)
        assertEquals(
            component,
            (viewModelStates[1] as ViewState.DoRender).component
        )
        assertLoading(viewModelStates[2], false)
    }

    @Test
    fun fetch_should_return_a_error_ViewState() {
        // Given
        val screenRequest = ScreenRequest(RandomData.httpUrl())
        val exception = BeagleException("Error")
        coEvery { componentRequester.fetchComponent(any()) } throws exception

        // When
        beagleUIViewModel.fetchComponent(screenRequest)

        // Then
        assertLoading(viewModelStates[0], true)
        assertEquals(ViewState.Error(exception), viewModelStates[1])
        assertLoading(viewModelStates[2], false)
    }

    @Test
    fun fetchComponent_with_screen_should_render_ViewState() {
        // Given
        val screenRequest = ScreenRequest("")
        val screen = mockk<ScreenComponent>()
        every { screen.identifier } returns "stub"

        // When
        beagleUIViewModel.fetchComponent(screenRequest, screen)

        // Then
        assertEquals(screen, (viewModelStates[0] as ViewState.DoRender).component)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun fetchAction_should_return_render_ViewState() {
        // Given
        val url = RandomData.httpUrl()

        // When
        beagleUIViewModel.fetchAction(url)

        // Then
        assertLoading(viewModelStates[0], true)
        assertEquals(action, (viewModelStates[1] as ViewState.DoAction).action)
        assertLoading(viewModelStates[2], false)
    }

    @Test
    fun fetchAction_should_return_a_error_ViewState() {
        // Given
        val url = RandomData.httpUrl()
        val exception = BeagleException("Error")
        coEvery { actionRequester.fetchAction(any()) } throws exception

        // When
        beagleUIViewModel.fetchAction(url)

        // Then
        assertLoading(viewModelStates[0], true)
        assertEquals(ViewState.Error(exception), viewModelStates[1])
        assertLoading(viewModelStates[2], false)
    }

    @Test
    fun onCleared_should_call_cancel() {
        // Given
        val viewModelSpy = spyk(beagleUIViewModel)
        every { viewModelSpy.cancel() } just Runs

        // When
        viewModelSpy.onCleared()

        // Then
        verify(exactly = once()) { viewModelSpy.cancel() }
    }

    private fun assertLoading(viewState: ViewState, expected: Boolean) {
        assertTrue(viewState is ViewState.Loading)
        assertEquals(expected, viewState.value)
    }
}
