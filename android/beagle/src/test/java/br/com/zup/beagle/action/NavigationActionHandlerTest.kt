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

package br.com.zup.beagle.action

import android.content.Context
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.navigation.DeepLinkHandler
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.view.BeagleNavigator
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class NavigationActionHandlerTest {

    @MockK
    private lateinit var context: Context

    @MockK
    private lateinit var deepLinkHandler: DeepLinkHandler

    private lateinit var navigationActionHandler: NavigationActionHandler

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)
        mockkObject(BeagleNavigator)

        navigationActionHandler = NavigationActionHandler()
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleEnvironment)
        unmockkObject(BeagleNavigator)
    }

    @Test
    fun handle_should_call_openNativeRoute_with_null_deepLinkHandler() {
        // Given
        val navigate = Navigate.OpenNativeRoute(RandomData.httpUrl())
        every { BeagleEnvironment.beagleSdk.deepLinkHandler } returns null

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = 0) { deepLinkHandler.getDeepLinkIntent(any(), any(), any()) }
    }

    @Test
    fun handle_should_call_openExternalURL() {
        // Given
        val url = RandomData.httpUrl()
        val navigate = Navigate.OpenExternalURL(url)
        every { BeagleNavigator.openExternalURL(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.openExternalURL(context, url) }
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
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.openNativeRoute(context, route, data, shouldResetApplication) }
    }

    @Test
    fun handle_should_call_resetApplication() {
        // Given
        val route = Route.Remote(RandomData.httpUrl())
        val navigate = Navigate.ResetApplication(route)
        every { BeagleNavigator.resetApplication(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.resetApplication(context, route) }
    }

    @Test
    fun handle_should_call_resetStack() {
        // Given
        val route = Route.Remote(RandomData.httpUrl())
        val navigate = Navigate.ResetStack(route)
        every { BeagleNavigator.resetStack(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.resetStack(context, route) }
    }

    @Test
    fun handle_should_call_pushView() {
        // Given
        val route = Route.Remote(RandomData.httpUrl())
        val navigate = Navigate.PushView(route)
        every { BeagleNavigator.pushView(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.pushView(context, route) }
    }

    @Test
    fun handle_should_call_popStack() {
        // Given
        val navigate = Navigate.PopStack()
        every { BeagleNavigator.popStack(any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.popStack(context) }
    }

    @Test
    fun handle_should_call_popView() {
        // Given
        val navigate = Navigate.PopView()
        every { BeagleNavigator.popView(any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.popView(context) }
    }

    @Test
    fun handle_should_call_popToView() {
        // Given
        val path = RandomData.httpUrl()
        val navigate = Navigate.PopToView(path)
        every { BeagleNavigator.popToView(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.popToView(context, path) }
    }

    @Test
    fun handle_should_call_pushStack() {
        // Given
        val route = Route.Remote(RandomData.httpUrl())
        val navigate = Navigate.PushStack(route)
        every { BeagleNavigator.pushStack(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.pushStack(context, route) }
    }
}
