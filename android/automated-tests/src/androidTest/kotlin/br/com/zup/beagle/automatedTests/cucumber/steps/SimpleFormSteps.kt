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

const val SIMPLE_FORM_SCREEN_BFF_URL = "http://10.0.2.2:8080/simpleform"

class SimpleFormScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@simpleform")
    fun setup() {
        TestUtils.startActivity(activityTestRule, SIMPLE_FORM_SCREEN_BFF_URL)
    }

    @After("@simpleform")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the simple form screen$")
    fun checkBaseScreen() {
        ScreenRobot()
            .checkViewContainsText("SimpleForm", true)
    }

    @Then("^checks that the textInput with the place holder (.*) is on the screen$")
    fun checkTextInputEmailInSimpleForm(field: String) {
        ScreenRobot()
            .checkViewContainsHint(field, true)
    }

    @Then("^checks that the button with the title (.*) is on the screen$")
    fun checkButtonOnScreen(textButton: String) {
        ScreenRobot()
            .checkViewContainsText(textButton)
    }

    @When("^I click on textInput for email with (.*) and insert my (.*)$")
    fun insertEmailInTextInput(hint: String, email: String) {
        ScreenRobot()
            .checkViewContainsHint(hint)
            .clickOnInputWithHint(hint)
            .typeText(hint, email)
            .hideKeyboard()
    }

    @When("^I click on textInput for name with (.*) and insert my (.*)$")
    fun insertNameInTextInput(hint: String, name: String) {
        ScreenRobot()
            .checkViewContainsHint(hint)
            .clickOnInputWithHint(hint)
            .typeText(hint, name)
            .hideKeyboard()
    }

    @When("^I click to (.*)$")
    fun sendDataFromTextInputs(submit: String) {
        ScreenRobot()
            .checkViewContainsText(submit)
            .clickOnText(submit)
    }

    @Then("^verify if (.*) is appear correctly$")
    fun checkIfTextInputDataIsEqualAlert(alertData: String) {
        ScreenRobot()
            .checkViewContainsText("Registered data", true)
            .checkViewContainsText(alertData)
    }
}