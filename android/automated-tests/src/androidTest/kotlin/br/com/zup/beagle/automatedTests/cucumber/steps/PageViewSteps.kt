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
import cucumber.api.java.en.*
import org.junit.Rule

const val PAGE_VIEW_SCREEN_BFF_URL = "http://10.0.2.2:8080/pageview"

class PageViewScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@pageview")
    fun setup() {
        TestUtils.startActivity(activityTestRule, PAGE_VIEW_SCREEN_BFF_URL)
    }

    @After("@pageview")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the pageview screen$")
    fun checkBaseScreen() {
        ScreenRobot()
            .checkViewContainsText("Beagle PageView", true)
    }

    @When("^I swipe left$")
    fun swipePageViewToLeft(){
        ScreenRobot()
            .swipeLeftOnView()
    }

    @Then("^checks that the text (.*) is on the screen$")
    fun checkThatPageItemIsVisible(string: String) {
        ScreenRobot()
            .checkViewContainsText(string, true)
    }

    @Then("^checks that the text (.*) is not on the screen$")
    fun checkThatTextOnPageViewIsNotVisible(string: String) {
        ScreenRobot()
            .checkViewDoesNotContainsText(string)
    }

    @Then("^checks that the page with text (.*) is not displayed$")
    fun checkPageViewIsNotDisplayed(string:String){
        ScreenRobot()
            .checkViewIsNotDisplayed(string)
        // comment
    }
}