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

package br.com.zup.beagle.android.action

import android.view.View
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.navigation.DeepLinkHandler
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.custom.BeagleNavigator
import br.com.zup.beagle.android.widget.RootView
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Test

class NavigateTest {

    @RelaxedMockK
    private lateinit var rootView: RootView

    @MockK
    private lateinit var deepLinkHandler: DeepLinkHandler

    private val view: View = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)
        mockkObject(BeagleNavigator)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun handle_should_call_openNativeRoute_with_null_deepLinkHandler() {
        // Given
        val navigate = Navigate.OpenNativeRoute(RandomData.httpUrl())
        every { BeagleEnvironment.beagleSdk.deepLinkHandler } returns null

        // When
        navigate.execute(rootView, view)

        // Then
        verify(exactly = 0) { deepLinkHandler.getDeepLinkIntent(any(), any(), any(), any()) }
    }

    @Test
    fun handle_should_call_openExternalURL() {
        // Given
        val url = RandomData.httpUrl()
        val navigate = Navigate.OpenExternalURL(url)
        every { BeagleNavigator.openExternalURL(any(), any()) } just Runs

        // When
        navigate.execute(rootView, view)

        // Then
        verify(exactly = once()) { BeagleNavigator.openExternalURL(rootView.getContext(), url) }
    }

    @Test
    fun handle_should_call_openNativeRoute() {
        // Given
        val route = RandomData.httpUrl()
        val data = mapOf("keyStub" to "valueStub")
        val shouldResetApplication = true
        val navigate = Navigate.OpenNativeRoute(route, shouldResetApplication, data)
        every { BeagleNavigator.openNativeRoute(any(), any(), any(), any()) } just Runs

        // When
        navigate.execute(rootView, view)

        // Then
        verify(exactly = once()) { BeagleNavigator.openNativeRoute(rootView, route, data, shouldResetApplication) }
    }

    @Test
    fun handle_should_call_resetApplication() {
        // Given
        val route = Route.Remote(RandomData.httpUrl())
        val navigate = Navigate.ResetApplication(route)
        every { BeagleNavigator.resetApplication(any(), any(), any()) } just Runs

        // When
        navigate.execute(rootView, view)

        // Then
        verify(exactly = once()) { BeagleNavigator.resetApplication(rootView.getContext(), route, null) }
    }

    @Test
    fun handle_should_call_resetStack() {
        // Given
        val route = Route.Remote(RandomData.httpUrl())
        val navigate = Navigate.ResetStack(route)
        every { BeagleNavigator.resetStack(any(), any(), any()) } just Runs

        // When
        navigate.execute(rootView, view)

        // Then
        verify(exactly = once()) { BeagleNavigator.resetStack(rootView.getContext(), route, null) }
    }

    @Test
    fun handle_should_call_pushView() {
        // Given
        val route = Route.Remote(RandomData.httpUrl())
        val navigate = Navigate.PushView(route)
        every { BeagleNavigator.pushView(any(), any()) } just Runs

        // When
        navigate.execute(rootView, view)

        // Then
        verify(exactly = once()) { BeagleNavigator.pushView(rootView.getContext(), route) }
    }

    @Test
    fun handle_should_call_popStack() {
        // Given
        val navigate = Navigate.PopStack()
        every { BeagleNavigator.popStack(any()) } just Runs

        // When
        navigate.execute(rootView, view)

        // Then
        verify(exactly = once()) { BeagleNavigator.popStack(rootView.getContext()) }
    }

    @Test
    fun handle_should_call_popView() {
        // Given
        val navigate = Navigate.PopView()
        every { BeagleNavigator.popView(any()) } just Runs

        // When
        navigate.execute(rootView, view)

        // Then
        verify(exactly = once()) { BeagleNavigator.popView(rootView.getContext()) }
    }

    @Test
    fun handle_should_call_popToView() {
        // Given
        val path = RandomData.httpUrl()
        val navigate = Navigate.PopToView(path)
        every { BeagleNavigator.popToView(any(), any()) } just Runs

        // When
        navigate.execute(rootView, view)

        // Then
        verify(exactly = once()) { BeagleNavigator.popToView(rootView.getContext(), path) }
    }

    @Test
    fun handle_should_call_pushStack() {
        // Given
        val route = Route.Remote(RandomData.httpUrl())
        val navigate = Navigate.PushStack(route)
        every { BeagleNavigator.pushStack(any(), any(), any()) } just Runs

        // When
        navigate.execute(rootView, view)

        // Then
        verify(exactly = once()) { BeagleNavigator.pushStack(rootView.getContext(), route, null) }
    }
}
