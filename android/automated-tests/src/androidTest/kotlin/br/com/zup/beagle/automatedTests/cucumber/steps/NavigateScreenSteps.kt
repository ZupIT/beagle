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
import br.com.zup.beagle.automatedTests.cucumber.elements.NAVIGATION_SCREEN_TITLE
import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import br.com.zup.beagle.automatedTests.utils.ActivityFinisher
import br.com.zup.beagle.automatedTests.utils.TestUtils
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.junit.Rule


class NavigateScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@navigation")
    fun setup() {
        TestUtils.startActivity(activityTestRule, Constants.navigateActionsBffUrl)
    }

    @After("@navigation")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^the Beagle application did launch with the navigation screen url$")
    fun checkBaseScreen() {
        ScreenRobot()
            .checkViewContainsText(NAVIGATION_SCREEN_TITLE, true)
    }

    @When("^I press a navigation button (.*)$")
    fun clickOnButton(string:String) {
        ScreenRobot()
            .clickOnText(string)
    }

    @And("^I click on (.*) button$")
    fun clickOnPoPButton(string:String) {
        ScreenRobot()
            .clickOnText(string)
    }

    @Then("^the screen should navigate to another screen with the text label (.*)$")
    fun checkGlobalTextScreen(string2:String) {
        ScreenRobot()
            .checkViewContainsText(string2, true)
    }

    @Then("^the screen should not navigate to another screen with the text label (.*)$")
    fun checkGlobalTextScreenIsNotOnView(string2:String) {
        ScreenRobot()
            .checkViewDoesNotContainsText(string2)
    }

    @Then("^the app should dismiss the view that contains (.*)$")
    fun checkTextIsNotOnAnyView(string1:String){
        ScreenRobot()
            .checkViewDoesNotContainsText(string1)
            .sleep(3)
    }

    @Then("^the view that contains the (.*) must still exist$")
    fun checkTextExistsInAView(string2:String) {
        ScreenRobot()
            .checkViewContainsText(string2, true)
    }
}