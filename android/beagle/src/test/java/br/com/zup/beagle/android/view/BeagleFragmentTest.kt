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

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.R
import br.com.zup.beagle.android.BaseSoLoaderTest
import br.com.zup.beagle.android.view.viewmodel.AnalyticsViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(application = ApplicationTest::class)
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class BeagleFragmentTest : BaseSoLoaderTest() {

    private val analyticsViewModel = mockk<AnalyticsViewModel>()
    private val localScreenSlot = slot<Boolean>()
    private val screenIdentifierSlot = slot<String>()
    private val json = """{
                "_beagleComponent_" : "beagle:screenComponent",
                "child" : {
                "_beagleComponent_" : "beagle:text",
                "text" : "hello"
            }
            }"""
    private val url = "/url"
    private var activity: ServerDrivenActivity? = null

    @Before
    fun mockBeforeTest() {
        prepareViewModelMock(analyticsViewModel)
        every { analyticsViewModel.createScreenReport(capture(localScreenSlot), capture(screenIdentifierSlot)) } just Runs
        val activityScenario: ActivityScenario<ServerDrivenActivity> = ActivityScenario.launch(ServerDrivenActivity::class.java)
        activityScenario.onActivity {
            activityScenario.moveToState(Lifecycle.State.RESUMED)
            activity = it
        }
    }

    @Test
    fun `Given  a BeagleFragment with local screen and screen identifier When BeagleFragment is resumed Then should report screen`() {
        //When
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.server_driven_container, BeagleFragment.newInstance(json, false, url)
        )?.commit()

        //Then
        assertEquals(false, localScreenSlot.captured)
        assertEquals(url, screenIdentifierSlot.captured)
    }

    @Test
    fun `Given  a BeagleFragment without local screen When BeagleFragment is resumed Then should not report screen`() {
        //When
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.server_driven_container,
            BeagleFragment.newInstance(json, null, url)
        )?.commit()

        //Then
        assertEquals(false, localScreenSlot.isCaptured)
        assertEquals(false, screenIdentifierSlot.isCaptured)
    }

    @Test
    fun `Given  a BeagleFragment without screen identifier When BeagleFragment is resumed Then should not report screen`() {
        //When
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.server_driven_container,
            BeagleFragment.newInstance(json, false, null)
        )?.commit()

        //then
        assertEquals(false, localScreenSlot.isCaptured)
        assertEquals(false, screenIdentifierSlot.isCaptured)
    }

    @Test
    fun `Given  a BeagleFragment without local screen and screen identifier When BeagleFragment is resumed Then should not report screen`() {
        //When
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.server_driven_container,
            BeagleFragment.newInstance(json)
        )?.commit()

        //Then
        assertEquals(false, localScreenSlot.isCaptured)
        assertEquals(false, screenIdentifierSlot.isCaptured)
    }
}