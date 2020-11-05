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

import androidx.test.rule.ActivityTestRule
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

const val ALERT_BFF_URL = "http://10.0.2.2:8080/alert"

class AlertScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@alert")
    fun setup() {
        TestUtils.startActivity(activityTestRule, ALERT_BFF_URL)
    }

    @After("@alert")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^the Beagle application did launch with the alert screen url$")
    fun checkBaseScreen() {
        ScreenRobot()
            .checkViewContainsText("Alert Screen", true)
    }

    @When("^I press an alert button with the (.*) title$")
    fun clickOnButton(string:String) {
        ScreenRobot()
            .clickOnText(string)
    }

    @Then("^an alert with the (.*) message should appear on the screen$")
    fun checkAlertMessage(string:String) {
        ScreenRobot()
            .checkViewContainsText(string, true)
    }

    @Then("^an alert with the (.*) and (.*) should appear on the screen$")
    fun checkAlertMessageAndTitle(string:String, string2:String) {
        ScreenRobot()
            .checkViewContainsText(string, true)
            .checkViewContainsText(string2, true)
    }

    @Then("^I press the confirmation (.*) button on the alert$")
    fun clickOnTheConfirmationActionButtonWithText(string:String){
        ScreenRobot().clickOnText(string)
    }

    @Then("^an alert with a confirmation button with (.*) label should appear$")
    fun checkAlertConfirmationButtonLabelIsSetWithText(string:String){
        ScreenRobot()
            .checkViewContainsText(string)
    }
}