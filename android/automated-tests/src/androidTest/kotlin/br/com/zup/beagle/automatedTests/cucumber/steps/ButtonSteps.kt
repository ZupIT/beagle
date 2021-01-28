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
import br.com.zup.beagle.automatedTests.cucumber.elements.*
import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import br.com.zup.beagle.automatedTests.utils.ActivityFinisher
import br.com.zup.beagle.automatedTests.utils.TestUtils
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.junit.Rule

val BUTTON_SCREEN_BFF_URL = "http://10.0.2.2:8080/button"

class ButtonScreen {
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@button")
    fun setup() {
        TestUtils.startActivity(activityTestRule, BUTTON_SCREEN_BFF_URL)
    }

    @Given("^that I'm on the button screen$")
    fun checkButtonScreen() {
        ScreenRobot()
            .checkViewContainsText(BUTTON_SCREEN_HEADER, true)
    }

    @When("I click on a component with a valid style attribute configured$")
    fun clickOnButtonWithStyle() {
        ScreenRobot()
            .clickOnText(BUTTON_WITH_STYLE_TEXT)
            .sleep(2)
    }

    @Then("all my button components should render their respective text attributes correctly$")
    fun renderTextAttributeCorrectly() {
        ScreenRobot()
            .checkViewContainsText(BUTTON_DEFAULT_TEXT)
            .checkViewContainsText(BUTTON_WITH_STYLE_TEXT)
            .checkViewContainsText(BUTTON_WITH_APPEARANCE_TEXT)
            .sleep(2)
    }

    @Then("component should render the action attribute correctly$")
    fun renderActionAttributeCorrectly() {
        ScreenRobot()
            .checkViewContainsText(ACTION_CLICK_HEADER)
            .checkViewContainsText(ACTION_CLICK_TEXT)
            .sleep(2)
    }

    @Then("the alert with message (.*) should not appear")
    fun checkAlertMessage(message :String) {
        ScreenRobot().checkViewDoesNotContainsText(message)
    }

    @After("@button")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }
}
