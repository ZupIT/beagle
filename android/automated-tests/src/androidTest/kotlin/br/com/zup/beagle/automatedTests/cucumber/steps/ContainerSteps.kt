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

import android.util.Log
import androidx.test.rule.ActivityTestRule
import br.com.zup.beagle.android.utils.toAndroidId
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

    @Before
    fun setup() {
        TestUtils.startActivity(activityTestRule, CONTAINER_SCREEN_BFF_URL)
    }

    @After
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




//
//    @When("^I press a navigation button (.*)$")
//    fun clickOnButton(string:String) {
//        ScreenRobot()
//            .clickOnText(string)
//    }
//
//    @When("^I press a navigation failure button (.*)$")
//    fun clickOnButtonFailure(string:String) {
//        ScreenRobot()
//            .clickOnText(string)
//    }
//
//    @And("^I click on (.*) button$")
//    fun clickOnPoPButton(string:String) {
//        ScreenRobot()
//            .clickOnText(string)
//    }
//
//    @Then("^the screen should navigate to another screen with the text label (.*)$")
//    fun checkGlobalTextScreen(string2:String) {
//        ScreenRobot()
//            .checkViewContainsText(string2, true)
//    }
//
//    @Then("^the screen should not navigate to another screen with the text label (.*)$")
//    fun checkGlobalTextScreenIsNotOnView(string2:String) {
//        ScreenRobot()
//            .checkViewDoesNotContainsText(string2)
//    }
//
//    @Then("^the app should dismiss the view that contains (.*)$")
//    fun checkTextIsNotOnAnyView(string1:String){
//        ScreenRobot()
//            .checkViewDoesNotContainsText(string1)
//    }
//
//
//
//    @Then ("^There must be a retry button with text (.*)$")
//    fun checkButtonExistsInAView(string2:String) {
//        ScreenRobot()
//            .checkViewContainsText(string2, true)
//    }
}