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

package br.com.zup.beagle.android.view.custom

import android.app.Application
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.MyBeagleSetup
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.setup.BeagleSdk
import br.com.zup.beagle.android.view.ApplicationTest
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.android.view.ServerDrivenActivity
import br.com.zup.beagle.android.view.viewmodel.AnalyticsViewModel
import br.com.zup.beagle.android.view.viewmodel.BeagleViewModel
import br.com.zup.beagle.android.view.viewmodel.ViewState
import com.facebook.yoga.YogaNode
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@Config(application = ApplicationTest::class)
@RunWith(AndroidJUnit4::class)
internal class BeagleViewTest : BaseTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val url = "/url"
    private lateinit var viewModel: BeagleViewModel
    private val component = Text("Test component")
    private val analyticsViewModel = mockk<AnalyticsViewModel>()
    private val localScreenSlot = slot<Boolean>()
    private val screenIdentifierSlot = slot<String>()
    private lateinit var beagleView: BeagleView
    private val mutableLiveData = MutableLiveData<ViewState>()

    @Before
    fun setup() {
        val application: Application = ApplicationProvider.getApplicationContext() as Application
        mockYoga(application)
        BeagleSdk.setInTestMode()
        MyBeagleSetup().init(application)
        viewModel = mockk()
        prepareViewModelMock(analyticsViewModel)
        every { analyticsViewModel.createScreenReport(capture(localScreenSlot), capture(screenIdentifierSlot)) } just Runs
        every { viewModel.fetchComponent(any(), any()) } returns mutableLiveData
        val activityScenario: ActivityScenario<ServerDrivenActivity> = ActivityScenario.launch(ServerDrivenActivity::class.java)
        activityScenario.onActivity {
            val rootView = ActivityRootView(it, 10, "")
            beagleView = BeagleView(rootView, viewModel)
        }
    }

    @After
    fun teardown() {
        BeagleSdk.deinitForTest()
        unmockkAll()
    }

    private fun mockYoga(application: Application) {
        val yogaNode = mockk<YogaNode>(relaxed = true, relaxUnitFun = true)
        val view = View(application)
        mockkStatic(YogaNode::class)
        every { YogaNode.create() } returns yogaNode
        every { yogaNode.data } returns view
    }

    @Test
    fun `Given a DoRenderState with isLocalScreen and ScreenIdentifier not null When loadView Then Should ReportScreen`() {
        //Given
        mutableLiveData.postValue(ViewState.DoRender(url, component, false))

        //when
        beagleView.loadView(ScreenRequest(url))

        //Then
        Assert.assertEquals(false, localScreenSlot.captured)
        Assert.assertEquals(url, screenIdentifierSlot.captured)
    }

    @Test
    fun `Given a DoRenderState with screen id null When loadView Then Should not ReportScreen`() {
        //Given
        mutableLiveData.postValue(ViewState.DoRender(null, component, false))

        //when
        beagleView.loadView(ScreenRequest(url))

        //Then
        Assert.assertEquals(false, localScreenSlot.isCaptured)
        Assert.assertEquals(false, screenIdentifierSlot.isCaptured)
    }
}