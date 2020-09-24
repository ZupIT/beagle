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

package br.com.zup.beagle.sample.snapshot

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import br.com.zup.beagle.sample.activities.CucumberActivity
import br.com.zup.beagle.sample.cucumber.elements.BUTTON_SCREEN_HEADER
import br.com.zup.beagle.sample.cucumber.robots.ScreenRobot
import br.com.zup.beagle.sample.utils.ActivityFinisher
import br.com.zup.beagle.sample.utils.TestUtils
import com.facebook.testing.screenshot.Screenshot
import cucumber.api.java.After
import cucumber.api.java.Before
import org.junit.Rule
import org.junit.Test


class CucumberActivityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(CucumberActivity::class.java, false, false)

    @After("@button")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Test
    fun testSnapshot() {
        TestUtils.startActivity(rule, "http://10.0.2.2:8080/button")
        ScreenRobot().checkViewContainsText(BUTTON_SCREEN_HEADER,true)
        Screenshot.snapActivity(rule.activity)
            .record()
    }

}