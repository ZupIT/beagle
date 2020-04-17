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

package br.com.zup.beagle.view

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import br.com.zup.beagle.R
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.testutil.RandomData
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

private val URL = RandomData.httpUrl()

class BeagleNavigatorTest {

    @MockK
    private lateinit var context: BeagleActivity
    @MockK
    private lateinit var fragmentTransaction: FragmentTransaction
    @MockK
    private lateinit var fragment: BeagleFragment
    @MockK(relaxed = true)
    private lateinit var intent: Intent

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk.config.baseUrl } returns RandomData.httpUrl()

        mockkObject(BeagleFragment.Companion)
        mockkObject(BeagleActivity.Companion)

        every { BeagleActivity.newIntent(any(), any(), any()) } returns intent

        val supportFragmentManager = mockk<FragmentManager>()
        every { context.supportFragmentManager } returns supportFragmentManager
        every { supportFragmentManager.fragments } returns mutableListOf<Fragment>()
        every { supportFragmentManager.beginTransaction() } returns fragmentTransaction
        every {
            fragmentTransaction.setCustomAnimations(
                any(),
                any(),
                any(),
                any()
            )
        } returns fragmentTransaction
        every { fragmentTransaction.replace(any(), any()) } returns fragmentTransaction
        every { fragmentTransaction.addToBackStack(any()) } returns fragmentTransaction
        every { fragmentTransaction.commit() } returns 0
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleFragment.Companion)
        unmockkObject(BeagleActivity.Companion)
        unmockkObject(BeagleEnvironment)
    }

    @Test
    fun finish_should_call_finish_activity() {
        // Given
        every { context.finish() } just Runs

        // When
        BeagleNavigator.finish(context)

        // Then
        verify(exactly = once()) { context.finish() }
    }

    @Test
    fun pop_should_call_activity_onBackPressed() {
        // Given
        every { context.onBackPressed() } just Runs

        // When
        BeagleNavigator.pop(context)

        // Then
        verify(exactly = once()) { context.onBackPressed() }
    }

    @Test
    fun addScreen_should_call_BeagleActivity_navigateTo() {
        // Given
        val screenRequest = ScreenRequest(URL)
        every { context.navigateTo(screenRequest, null) } just Runs

        // When
        BeagleNavigator.addScreen(context, URL)

        // Then
        verify(exactly = once()) { context.navigateTo(screenRequest, null) }
    }

    @Test
    fun addScreen_should_start_BeagleActivity() {
        // Given
        val context = mockk<Activity>()
        every { context.startActivity(any()) } just Runs

        // When
        BeagleNavigator.addScreen(context, URL)

        // Then
        verify(exactly = once()) { context.startActivity(intent) }
    }

    @Test
    fun swapScreen_should_start_BeagleActivity_and_clear_stack() {
        // Given
        every { context.startActivity(any()) } just Runs
        val flagSlot = slot<Int>()
        every { intent.addFlags(capture(flagSlot)) } returns intent

        // When
        BeagleNavigator.swapScreen(context, URL)

        // Then
        verify(exactly = once()) { context.startActivity(intent) }
        assertEquals(
            flagSlot.captured,
            (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    @Test
    fun popToScreen_should_call_popToBackStack() {
        // Given
        every { context.supportFragmentManager.popBackStack(any<String>(), any()) } just Runs

        // When
        BeagleNavigator.popToScreen(context, URL)

        // Then
        verify(exactly = 1) { context.supportFragmentManager.popBackStack(URL, 0) }
    }

    @Test
    fun presentScreen_should_start_BeagleActivity() {
        // Given
        val context = mockk<Activity>()
        every { context.startActivity(any()) } just Runs

        // When
        BeagleNavigator.presentScreen(context, URL)

        // Then
        verify(exactly = once()) { context.startActivity(intent) }
    }

    @Test
    fun pop_should_call_dialog_dismiss() {
        // Given
        every { context.onBackPressed() } just Runs
        val supportFragmentManager = mockk<FragmentManager>()
        val dialogFragment = mockk<DialogFragment>()
        every { context.supportFragmentManager } returns supportFragmentManager
        every { supportFragmentManager.fragments } returns listOf(dialogFragment)
        every { dialogFragment.dismiss() } just Runs

        // When
        BeagleNavigator.pop(context)

        // Then
        verify(exactly = once()) { dialogFragment.dismiss() }
    }
}