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
import android.content.Intent
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
import io.mockk.mockk
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
    fun handle_should_call_openDeepLink_with_null_deepLinkHandler() {
        // Given
        val navigate = Navigate(
            type = NavigationType.OPEN_DEEP_LINK,
            path = RandomData.httpUrl()
        )
        every { BeagleEnvironment.beagleSdk.deepLinkHandler } returns null

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = 0) { deepLinkHandler.getDeepLinkIntent(any(), any()) }
    }

    @Test
    fun handle_should_call_openDeepLink_to_startActivity() {
        // Given
        val navigate = Navigate(
            type = NavigationType.OPEN_DEEP_LINK,
            path = RandomData.httpUrl()
        )
        val intent = mockk<Intent>()
        every { context.startActivity(any()) } just Runs
        every { BeagleEnvironment.beagleSdk.deepLinkHandler } returns deepLinkHandler
        every { deepLinkHandler.getDeepLinkIntent(any(), any()) } returns intent

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { context.startActivity(intent) }
    }

    @Test
    fun handle_should_not_try_to_call_deepLinkHandler() {
        // Given
        val navigate = Navigate(
            type = NavigationType.OPEN_DEEP_LINK
        )

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = 0) { deepLinkHandler.getDeepLinkIntent(any(), any()) }
    }

    @Test
    fun handle_should_call_swapView() {
        // Given
        val path = RandomData.httpUrl()
        val navigate = Navigate(
            type = NavigationType.SWAP_VIEW,
            path = path
        )
        every { BeagleNavigator.swapScreen(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.swapScreen(context, path) }
    }

    @Test
    fun handle_should_call__addView() {
        // Given
        val path = RandomData.httpUrl()
        val navigate = Navigate(
            type = NavigationType.ADD_VIEW,
            path = path
        )
        every { BeagleNavigator.addScreen(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.addScreen(context, path) }
    }

    @Test
    fun handle_should_call_finishView() {
        // Given
        val navigate = Navigate(
            type = NavigationType.FINISH_VIEW
        )
        every { BeagleNavigator.finish(any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.finish(context) }
    }

    @Test
    fun handle_should_call_popView() {
        // Given
        val navigate = Navigate(
            type = NavigationType.POP_VIEW
        )
        every { BeagleNavigator.addScreen(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = once()) { BeagleNavigator.pop(context) }
    }

    @Test
    fun handle_should_call_popToView() {
        // Given
        val path = RandomData.httpUrl()
        val navigate = Navigate(
            type = NavigationType.POP_TO_VIEW,
            path = path
        )
        every { BeagleNavigator.popToScreen(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = 1) { BeagleNavigator.popToScreen(context, path) }
    }

    @Test
    fun handle_should_call_popToView_with_null_eventData() {
        // Given
        val navigate = Navigate(
            type = NavigationType.POP_TO_VIEW
        )

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = 0) { BeagleNavigator.popToScreen(any(), any()) }
    }

    @Test
    fun handle_should_call_presentView() {
        // Given
        val path = RandomData.httpUrl()
        val navigate = Navigate(
            type = NavigationType.PRESENT_VIEW,
            path = path
        )
        every { BeagleNavigator.presentScreen(any(), any()) } just Runs

        // When
        navigationActionHandler.handle(context, navigate)

        // Then
        verify(exactly = 1) { BeagleNavigator.presentScreen(context, path) }
    }
}