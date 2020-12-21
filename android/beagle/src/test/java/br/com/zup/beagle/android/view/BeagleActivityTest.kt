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
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.R
import br.com.zup.beagle.android.MyBeagleSetup
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.data.ComponentRequester
import br.com.zup.beagle.android.setup.BeagleSdk
import br.com.zup.beagle.android.testutil.CoroutinesTestExtension
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import br.com.zup.beagle.android.view.viewmodel.BeagleScreenViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(application = ApplicationTest::class)
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class BeagleActivityTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val beagleFragment: BeagleFragment = mockk()
    private val component = Text("Test component")
    private val url = "/url"
    private val componentRequester: ComponentRequester = mockk()
    private lateinit var beagleViewModel: BeagleScreenViewModel
    private var activity: ServerDrivenActivity? = null

    @BeforeEach
    fun setup() {
        mockkObject(BeagleFragment)
        every { BeagleFragment.newInstance(component = component, any(), any()) } returns beagleFragment
        beagleViewModel = BeagleScreenViewModel(ioDispatcher = TestCoroutineDispatcher(), componentRequester)
        mockkConstructor(ViewModelProvider::class)
        every { anyConstructed<ViewModelProvider>().get(beagleViewModel::class.java) } returns beagleViewModel
        coEvery { componentRequester.fetchComponent(any()) } returns component
        BeagleSdk.setInTestMode()
        val application = ApplicationProvider.getApplicationContext() as Application
        MyBeagleSetup().init(application)
        val activityScenario: ActivityScenario<ServerDrivenActivity> = ActivityScenario.launch(ServerDrivenActivity::class.java)
        activityScenario.onActivity {
            activityScenario.moveToState(Lifecycle.State.RESUMED)
            activity = it
        }

    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `GIVEN a screen request WHEN navigate to THEN should call BeagleFragment newInstance with right parameters`() = runBlockingTest {
        // Given
        val screenRequest = ScreenRequest(url)

        //When
        activity?.navigateTo(screenRequest, null)

        // THEN
        verify(exactly = 1) { BeagleFragment.newInstance(component = component, false, screenRequest.url) }
        assertThrows<NullPointerException> { print("teste") }

    }

    @Test
    fun `GIVEN a screen WHEN navigate to THEN should call BeagleFragment newInstance with right parameters`() = runBlockingTest {
        // Given
        val screenRequest = ScreenRequest("")
        val screenId = "myScreen"
        val screen = Screen(id = screenId, child = component)

        //When
        activity?.navigateTo(screenRequest, screen)

        // THEN
        verify(exactly = 1) { BeagleFragment.newInstance(component = screen, false, screenId) }

    }

}

class ApplicationTest : Application() {
    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.Theme_AppCompat)
    }
}

