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
import cucumber.api.java.en.*
import org.junit.Rule

const val TOUCHABLE_SCREEN_SCREEN_BFF_URL = "http://10.0.2.2:8080/touchable"

class TouchableScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@touchable")
    fun setup() {
        TestUtils.startActivity(activityTestRule, TOUCHABLE_SCREEN_SCREEN_BFF_URL)
    }

    @After("@touchable")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the touchable screen$")
    fun checkImageScreen() {
        ScreenRobot()
            .checkViewContainsText(TOUCHABLE_SCREEN_HEADER, true)
    }

    @And("^I have a text with touchable configured$")
    fun checkTextWithTouchable() {
        ScreenRobot()
            .checkViewContainsText(TOUCHABLE_TEXT_1)
            .checkViewContainsText(TOUCHABLE_TEXT_2)
            .sleep(2)
    }

    @And("^I have an image with touchable configured$")
    fun checkImageWithTouchable() {
        ScreenRobot()
            .checkViewContainsText(TOUCHABLE_TEXT_3)
            .sleep(2)
    }

    @When("^I click on touchable text (.*)$")
    fun clickOnTouchableText(string1: String?) {
        ScreenRobot()
            .clickOnText(string1)
    }

    @When("^I click on touchable image$")
    fun clickOnTouchableImage() {
        ScreenRobot()
            .clickOnTouchableImage()
    }

    @Then("^touchable screen should render all text attributes correctly$")
    fun checkTouchableScreenTexts() {
        ScreenRobot()
            .checkViewContainsText(TOUCHABLE_TEXT_1)
            .checkViewContainsText(TOUCHABLE_TEXT_2)
            .checkViewContainsText(TOUCHABLE_TEXT_3)
            .checkViewContainsText(TOUCHABLE_TEXT_4)
            .scrollViewDown()
    }
}