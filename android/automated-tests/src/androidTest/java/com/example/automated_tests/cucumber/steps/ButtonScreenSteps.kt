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

package com.example.automated_tests.cucumber.steps


import androidx.test.rule.ActivityTestRule
import com.example.automated_tests.MainActivity
import com.example.automated_tests.cucumber.elements.*
import com.example.automated_tests.cucumber.robots.ScreenRobot
import com.example.automated_tests.utils.ActivityFinisher
import com.example.automated_tests.utils.TestUtils
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.*
import org.junit.Rule


class ButtonScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@button")
    fun setup() {
        TestUtils.startActivity(activityTestRule, "http://10.0.2.2:8080/button")
    }

    @After("@button")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the button screen$")
    fun checkButtonScreen() {
        ScreenRobot()
            .checkViewContainsText(MAIN_HEADER)
            .checkViewContainsText(BUTTON_SCREEN_HEADER)
            .sleep(2)
    }

    @When("I click on a component with a valid style attribute configured$")
    fun clickOnButtonWithStyle() {
        ScreenRobot()
            .clickOnText(BUTTON_WITH_STYLE_TEXT)
            .sleep(2)
    }

    @When("^I click on button (.*)$")
    fun clickOnButton(string1: String?) {
        ScreenRobot()
            .clickOnText(string1)
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
            .checkViewContainsText(MAIN_HEADER)
            .checkViewContainsText(ACTION_CLICK_HEADER)
            .checkViewContainsText(ACTION_CLICK_TEXT)
            .sleep(2)
    }
}