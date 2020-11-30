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

package br.com.zup.beagle.android.preview

import android.app.Application
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.android.setup.BeagleSdk
import com.facebook.yoga.YogaNode
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.verifyOrder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
class PreviewActivityTest {

    @Test
    fun `GIVEN a preview Activity WHEN on error called THEN should show correct toast message`() {
        // WHEN
        val activityScenario: ActivityScenario<PreviewActivity> = ActivityScenario.launch(PreviewActivity::class.java)
        activityScenario.onActivity {
            it.onError(null)
        }

        // THEN
        assertEquals(ShadowToast.getTextOfLatestToast().toString(), "onError: Closed webSocket trying to reconnect")
    }

    @Test
    fun `GIVEN a preview Activity WHEN on close called THEN should show correct toast message`() {
        // WHEN
        val activityScenario: ActivityScenario<PreviewActivity> = ActivityScenario.launch(PreviewActivity::class.java)
        activityScenario.onActivity {
            it.onClose(null)
        }

        // THEN
        assertEquals(ShadowToast.getTextOfLatestToast().toString(), "onClose: Connection closed by remote host")
    }

    @Test
    fun `GIVEN a preview Activity WHEN on message called THEN should show correct toast message`() {

        // WHEN
        val activityScenario: ActivityScenario<PreviewActivity> = ActivityScenario.launch(PreviewActivity::class.java)
        activityScenario.onActivity {
            it.onMessage("Welcome: test")
        }

        // THEN
        assertEquals(ShadowToast.getTextOfLatestToast().toString(), "Welcome: test")
    }

    @Test
    fun `GIVEN a preview Activity WHEN on message called THEN should show screen`() {
        // WHEN
        val application = ApplicationProvider.getApplicationContext() as Application
        val yogaNode = mockk<YogaNode>(relaxed = true, relaxUnitFun = true)
        val view = View(application)
        var activity: PreviewActivity? = null

        mockkStatic(YogaNode::class)

        every { YogaNode.create() } returns yogaNode
        every { yogaNode.data } returns view

        BeagleSdk.setInTestMode()
        MyBeagleSetup().init(application)

        // GIVEN
        val activityScenario: ActivityScenario<PreviewActivity> = ActivityScenario.launch(PreviewActivity::class.java)
        activityScenario.onActivity {
            it.onMessage(
                """
                    {
                        "_beagleComponent_": "beagle:container",
                        "children": [
                            {
                                "_beagleComponent_": "beagle:text",
                                "text": "Simple text"
                            }
                        ]
                    }
                """)

            activityScenario.moveToState(Lifecycle.State.RESUMED)
            activity = it
        }

        // THEN
        assertNotNull(activity!!.supportFragmentManager.fragments.first().view)
    }

    @Test
    fun `GIVEN a preview Activity WHEN move to state to destroy THEN should call on destroy`() {
        // WHEN
        mockkConstructor(BeaglePreview::class)

        val activityScenario: ActivityScenario<PreviewActivity> = ActivityScenario.launch(PreviewActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.DESTROYED)

        // THEN
        verifyOrder {
            anyConstructed<BeaglePreview>().closeWebSocket()
            anyConstructed<BeaglePreview>().doNotReconnect()
        }
    }

}