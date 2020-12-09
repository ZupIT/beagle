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

package br.com.zup.beagle.automatedTests.cucumber.steps

import android.support.test.rule.ActivityTestRule
import br.com.zup.beagle.automatedTests.activity.MainActivity
import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import br.com.zup.beagle.automatedTests.utils.ActivityFinisher
import br.com.zup.beagle.automatedTests.utils.TestUtils
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import org.junit.Rule

const val CONTAINER_SCREEN_BFF_URL = "http://10.0.2.2:8080/container-test"

class ContainerSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@container")
    fun setup() {
        TestUtils.startActivity(activityTestRule, CONTAINER_SCREEN_BFF_URL)
    }

    @After("@container")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^the Beagle application did launch with the container screen url$")
    fun checkBaseScreen() {
        ScreenRobot()
            .checkViewContainsText("Container Screen", true)
    }

    @Then("^the view that contains the texts with titles (.*) (.*) and (.*) must be displayed$")
    fun checkTextExistsInAView(string1:String, string2:String, string3:String) {
        ScreenRobot()
            .checkViewContainsText(string1, true)
            .checkViewContainsText(string2, true)
            .checkViewContainsText(string3, true)
    }

    @Then("^the views that contains the text (.*) set via context must be displayed$")
    fun checkTextExistsInAViewSetViaContext(string1:String) {
        ScreenRobot()
            .checkViewContainsText(string1, true)
    }
}