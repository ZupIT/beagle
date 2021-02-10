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

import android.app.Application
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.R
import br.com.zup.beagle.android.BaseSoLoaderTest
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.MyBeagleSetup
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.data.ComponentRequester
import br.com.zup.beagle.android.setup.BeagleSdk
import br.com.zup.beagle.android.testutil.CoroutinesTestExtension
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import br.com.zup.beagle.android.view.viewmodel.AnalyticsViewModel
import br.com.zup.beagle.android.view.viewmodel.BeagleScreenViewModel
import com.facebook.yoga.YogaNode
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(application = ApplicationTest::class)
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class BeagleActivityTest : BaseSoLoaderTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val component = Text("Test component")
    private val componentRequester: ComponentRequester = mockk()
    private lateinit var beagleViewModel: BeagleScreenViewModel
    private var activity: ServerDrivenActivity? = null
    private val analyticsViewModel = mockk<AnalyticsViewModel>()
    private val localScreenSlot = slot<Boolean>()
    private val screenIdentifierSlot = slot<String>()

    @Before
    fun mockBeforeTest() {
        coEvery { componentRequester.fetchComponent(ScreenRequest("/url")) } returns component
        beagleViewModel = BeagleScreenViewModel(ioDispatcher = TestCoroutineDispatcher(), componentRequester)
        prepareViewModelMock(beagleViewModel)
        val activityScenario: ActivityScenario<ServerDrivenActivity> = ActivityScenario.launch(ServerDrivenActivity::class.java)
        activityScenario.onActivity {
            activityScenario.moveToState(Lifecycle.State.RESUMED)
            activity = it
        }
    }

    @Test
    fun `Given a screen request When navigate to Then should call BeagleFragment newInstance with right parameters`() = runBlockingTest {
        // Given
        val url = "/url"
        val screenRequest = ScreenRequest(url)
        prepareViewModelMock(analyticsViewModel)
        every { analyticsViewModel.createScreenReport(capture(localScreenSlot), capture(screenIdentifierSlot)) } just Runs

        //When
        activity?.navigateTo(screenRequest, null)

        //Then
        assertEquals(false, localScreenSlot.captured)
        assertEquals(url, screenIdentifierSlot.captured)
    }


    @Test
    fun `Given a screen with id When navigate to Then should call BeagleFragment newInstance with right parameters`() = runBlockingTest {
        // Given
        val screenRequest = ScreenRequest("")
        val screenId = "myScreen"
        val screen = Screen(id = screenId, child = component)


        prepareViewModelMock(analyticsViewModel)
        every { analyticsViewModel.createScreenReport(capture(localScreenSlot), capture(screenIdentifierSlot)) } just Runs


        //When
        activity?.navigateTo(screenRequest, screen)

        // THEN
        assertEquals(true, localScreenSlot.captured)
        assertEquals(screenId, screenIdentifierSlot.captured)
    }

    @Test
    fun `Given a screen with identifier When navigate to Then should call BeagleFragment newInstance with right parameters`() = runBlockingTest {
        // Given
        val screenRequest = ScreenRequest("")
        val screenIdentifier = "myScreen"
        val screen = Screen(identifier = screenIdentifier, child = component)


        prepareViewModelMock(analyticsViewModel)
        every { analyticsViewModel.createScreenReport(capture(localScreenSlot), capture(screenIdentifierSlot)) } just Runs


        //When
        activity?.navigateTo(screenRequest, screen)

        // THEN
        assertEquals(true, localScreenSlot.captured)
        assertEquals(screenIdentifier, screenIdentifierSlot.captured)
    }

}

class ApplicationTest : Application() {
    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.Theme_AppCompat)
    }
}

