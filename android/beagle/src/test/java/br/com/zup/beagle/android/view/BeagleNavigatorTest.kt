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

package br.com.zup.beagle.android.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import br.com.zup.beagle.action.Route
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.logger.BeagleLogger
import br.com.zup.beagle.android.navigation.DeepLinkHandler
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.widget.ui.Text
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import kotlin.test.assertEquals

private val route = Route.Remote(RandomData.httpUrl())
private val url = RandomData.httpUrl()

class BeagleNavigatorTest {

    @MockK
    private lateinit var context: BeagleActivity
    @MockK
    private lateinit var fragmentTransaction: FragmentTransaction
    @MockK(relaxed = true)
    private lateinit var intent: Intent

    @MockK
    private lateinit var deepLinkHandler: DeepLinkHandler

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(BeagleEnvironment)
        mockkObject(BeagleLogger)
        mockkStatic("android.net.Uri")

        every { BeagleEnvironment.beagleSdk.config.baseUrl } returns RandomData.httpUrl()

        mockkObject(BeagleFragment.Companion)
        mockkObject(BeagleActivity.Companion)

        every { BeagleActivity.newIntent(any(), any(), any()) } returns intent

        val supportFragmentManager = mockk<FragmentManager>()
        every { context.supportFragmentManager } returns supportFragmentManager
        every { supportFragmentManager.fragments } returns mutableListOf<Fragment>()
        every { supportFragmentManager.beginTransaction() } returns fragmentTransaction
        every {
            fragmentTransaction.setCustomAnimations(any(), any(), any(), any())
        } returns fragmentTransaction
        every { fragmentTransaction.replace(any(), any()) } returns fragmentTransaction
        every { fragmentTransaction.addToBackStack(any()) } returns fragmentTransaction
        every { fragmentTransaction.commit() } returns 0
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun openExternalURL_should_call_intent() {
        // Given
        val webPage: Uri = mockk()
        val url = RandomData.httpUrl()
        every { context.startActivity(any()) } just Runs
        every { Uri.parse(url) } returns webPage

        // When
        BeagleNavigator.openExternalURL(context, url)

        // Then
        verify(exactly = once()) { Uri.parse(url) }
        verify(exactly = once()) { context.startActivity(any()) }
    }

    @Test
    fun openExternalURL_should_catch_when_url_is_invalid() {
        // Given
        val webPage: Uri = mockk()
        val url = "invalid url"
        every { context.startActivity(any()) } throws Exception()
        every { Uri.parse(url) } returns webPage
        every { BeagleLogger.error(any()) } just Runs

        // When
        BeagleNavigator.openExternalURL(context, url)

        // Then
        verify(exactly = once()) { BeagleLogger.error(any()) }
    }

    @Test
    fun handle_should_call_OpenNativeRoute_to_startActivity() {
        // Given
        val map = mapOf("keyStub" to "valueStub")
        val intent = mockk<Intent>()
        every { context.startActivity(any()) } just Runs
        every { BeagleEnvironment.beagleSdk.deepLinkHandler } returns deepLinkHandler
        every { deepLinkHandler.getDeepLinkIntent(any(), any(), any()) } returns intent

        // When
        BeagleNavigator.openNativeRoute(context, url, map, false)

        // Then
        verify(exactly = once()) { context.startActivity(intent) }
    }

    @Test
    fun popStack_should_call_finish_activity() {
        // Given
        every { context.finish() } just Runs

        // When
        BeagleNavigator.popStack(context)

        // Then
        verify(exactly = once()) { context.finish() }
    }

    @Test
    fun popView_should_call_activity_onBackPressed() {
        // Given
        every { context.onBackPressed() } just Runs

        // When
        BeagleNavigator.popView(context)

        // Then
        verify(exactly = once()) { context.onBackPressed() }
    }

    @Test
    fun pushView_should_call_BeagleActivity_navigateTo() {
        // Given
        val screenRequest = ScreenRequest(route.route)
        every { context.navigateTo(screenRequest, null) } just Runs

        // When
        BeagleNavigator.pushView(context, route)

        // Then
        verify(exactly = once()) { context.navigateTo(screenRequest, null) }
    }

    @Test
    fun pushView_should_start_BeagleActivity() {
        // Given
        val context = mockk<Activity>()
        val route = Route.Local(Screen(child = Text("stub")))
        every { context.startActivity(any()) } just Runs

        // When
        BeagleNavigator.pushView(context, route)

        // Then
        verify(exactly = once()) { context.startActivity(intent) }
    }

    @Test
    fun resetApplication_should_start_BeagleActivity_and_clear_stack() {
        // Given
        every { context.startActivity(any()) } just Runs
        val flagSlot = slot<Int>()
        every { intent.addFlags(capture(flagSlot)) } returns intent

        // When
        BeagleNavigator.resetApplication(context, route)

        // Then
        verify(exactly = once()) { context.startActivity(intent) }
        assertEquals(
            flagSlot.captured,
            (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    @Test
    fun resetStack_should_start_BeagleActivity_and_clear_stack() {
        // Given
        every { context.finish() } just Runs
        every { context.startActivity(any()) } just Runs

        // When
        BeagleNavigator.resetStack(context, route)

        // Then
        verify(exactly = once()) { BeagleNavigator.popStack(context) }
        verify(exactly = once()) { context.startActivity(intent) }
    }

    @Test
    fun popToView_should_call_popToBackStack() {
        // Given
        every { context.supportFragmentManager.popBackStack(any<String>(), any()) } just Runs

        // When
        BeagleNavigator.popToView(context, url)

        // Then
        verify(exactly = once()) { context.supportFragmentManager.popBackStack(url, 0) }
    }

    @Test
    fun pushStack_should_start_BeagleActivity() {
        // Given
        val context = mockk<Activity>()
        every { context.startActivity(any()) } just Runs

        // When
        BeagleNavigator.pushStack(context, route)

        // Then
        verify(exactly = once()) { context.startActivity(intent) }
    }

    @Test
    fun popView_should_call_dialog_dismiss() {
        // Given
        every { context.onBackPressed() } just Runs
        val supportFragmentManager = mockk<FragmentManager>()
        val dialogFragment = mockk<DialogFragment>()
        every { context.supportFragmentManager } returns supportFragmentManager
        every { supportFragmentManager.fragments } returns listOf(dialogFragment)
        every { dialogFragment.dismiss() } just Runs

        // When
        BeagleNavigator.popView(context)

        // Then
        verify(exactly = once()) { dialogFragment.dismiss() }
    }
}