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

package br.com.zup.beagle.automatedTests.screenshotTests

import androidx.test.rule.ActivityTestRule
import br.com.zup.beagle.automatedTests.activity.AppBeagleActivity
import br.com.zup.beagle.automatedTests.utils.TestUtils
import com.karumi.shot.ScreenshotTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleScreenshotTest: ScreenshotTest {

    @get:Rule
    var activityTestRule: ActivityTestRule<AppBeagleActivity> = ActivityTestRule(
        AppBeagleActivity::class.java)

    @Before
    fun setup() {
        TestUtils.startBeagleActivity(activityTestRule,"http://10.0.2.2:8080/button" )
    }

    @Test
    fun testButtonUrlLoading() {
        compareScreenshot(activityTestRule.activity)
    }

}
