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

const val SCROLL_VIEW_SCREEN_BFF_URL = "http://10.0.2.2:8080/scrollview"

class ScrollViewScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@scrollview")
    fun setup() {
        TestUtils.startActivity(activityTestRule, SCROLL_VIEW_SCREEN_BFF_URL)
    }

    @After("@scrollview")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the scrollview screen$")
    fun checkScrollViewScreen() {
        ScreenRobot()
            .checkViewContainsText(SCROLLVIEW_SCREEN_HEADER, true)
    }

    @When("^I have a vertical scroll configured$")
    fun checkVerticalScrollText() {
        ScreenRobot()
            .checkViewContainsText("Vertical 1")
            .sleep(2)
    }

    @When("^I have a horizontal scroll configured$")
    fun checkHorizontalScrollText() {
        ScreenRobot()
            .checkViewContainsText("Horizontal 1")
            .sleep(2)
    }

    @Then("^scrollview screen should render all text attributes correctly$")
    fun checkScrollViewScreenTexts() {
        ScreenRobot()
            .checkViewContainsText(SCROLLVIEW_TEXT_1)
            .checkViewContainsText(SCROLLVIEW_TEXT_2)
    }

    @Then("^scrollview screen should perform the scroll action vertically$")
    fun validateVerticalScroll() {
        ScreenRobot()
            .scrollTo("Vertical 5")
            .sleep(2)
    }

    @Then("^scrollview screen should perform the scroll action horizontally$")
    fun validateHorizontalScroll() {
        ScreenRobot()
            .scrollTo("Horizontal 5")
            .sleep(2)
    }

}
