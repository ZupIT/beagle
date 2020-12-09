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
import cucumber.api.java.en.When
import org.junit.Rule

const val SEND_REQUEST_BFF_URL = "http://10.0.2.2:8080/send-request"

class SendRequestSteps {
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@sendrequest")
    fun setup() {
        TestUtils.startActivity(activityTestRule, SEND_REQUEST_BFF_URL)
    }

    @Given("^the Beagle application did launch with the send request screen url$")
    fun checkTitleScreen() {
        ScreenRobot()
            .checkViewContainsText("Send Request Screen", true)
    }

    @When("^I press the (.*) button$")
    fun clickOnButtonSendRequestSuccess(string: String) {
        ScreenRobot()
            .clickOnText(string)
            .sleep(2)
    }

    @Then("^the screen should show some alert with (.*) title$")
    fun verifyAlertTitle(string: String) {
        ScreenRobot()
            .checkViewContainsText(string, true)
            .sleep(2)
    }

    @When("^I click on sendRequestError button (.*)")
    fun clickOnButtonSendRequestError(string: String) {
        ScreenRobot()
            .clickOnText(string)
            .sleep(2)
    }

    @Then("^the pressed button changes it's (.*) title to didFinish$")
    fun verifyChangeTitle(string: String) {
        ScreenRobot()
            .checkViewDoesNotContainsText(string)
            .sleep(2)
    }

    @After("@sendrequest")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }
}
