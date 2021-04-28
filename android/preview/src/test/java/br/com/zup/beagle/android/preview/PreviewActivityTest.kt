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
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.test.rules.BeagleComponentsRule
import io.mockk.mockkConstructor
import io.mockk.verifyOrder
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
class PreviewActivityTest {

    @get:Rule
    val beagleComponentsRule = BeagleComponentsRule()

    lateinit var activityScenario: ActivityScenario<PreviewActivity>

    @Before
    fun setUp() {
        mockkConstructor(BeaglePreview::class)
        activityScenario = ActivityScenario.launch(PreviewActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun `GIVEN a preview Activity WHEN on error called THEN should show correct toast message`() {
        // WHEN
        activityScenario.onActivity {
            it.onError(null)
        }

        // THEN
        assertEquals(ShadowToast.getTextOfLatestToast().toString(), "onError: Closed webSocket trying to reconnect")
    }

    @Test
    fun `GIVEN a preview Activity WHEN on close called THEN should show correct toast message`() {
        // WHEN
        activityScenario.onActivity {
            it.onClose(null)
        }

        // THEN
        assertEquals(ShadowToast.getTextOfLatestToast().toString(), "onClose: Connection closed by remote host")
    }

    @Test
    fun `GIVEN a preview Activity WHEN on message called THEN should show correct toast message`() {

        // WHEN
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
        var activity: PreviewActivity? = null
        var textComponent: TextView? = null
        var incrementButton: Button? = null
        MyBeagleSetup().init(application)

        // GIVEN
        activityScenario.onActivity {
            it.onMessage(
                """
                    {
                       "_beagleComponent_":"beagle:screenComponent",
                       "navigationBar":{
                          "title":"Choose a Component",
                          "showBackButton":true
                       },
                       "child":{
                          "_beagleComponent_":"beagle:container",
                          "children":[
                             {
                                "_beagleComponent_":"beagle:text",
                                "text":"Counter: @{counter}",
                                 "id": "textComponent"
                             },
                             {
                                "_beagleComponent_":"beagle:button",
                                "text":"increment",
                                "id": "incrementButton",
                                "onPress":[
                                   {
                                      "_beagleAction_":"beagle:setContext",
                                      "contextId":"counter",
                                      "value":"@{sum(counter, 1)}"
                                   }
                                ]
                             }
                          ],
                          "context":{
                             "id":"counter",
                             "value":2
                          }
                       }
                    }
                """.trimIndent())

            activityScenario.moveToState(Lifecycle.State.RESUMED)
            activity = it
            textComponent = it.findViewById("textComponent".toAndroidId())
            incrementButton = it.findViewById("incrementButton".toAndroidId())
        }

        incrementButton?.performClick()

        // THEN
        assertNotNull(textComponent)
        assertEquals("Counter: 3", textComponent?.text)
        assertNotNull(incrementButton)
        assertEquals("increment", incrementButton?.text)
    }

    @Test
    fun `GIVEN a preview Activity WHEN move to state to destroy THEN should call on destroy`() {
        // WHEN
        activityScenario.moveToState(Lifecycle.State.DESTROYED)

        // THEN
        verifyOrder {
            anyConstructed<BeaglePreview>().closeWebSocket()
            anyConstructed<BeaglePreview>().doNotReconnect()
        }
    }

}
