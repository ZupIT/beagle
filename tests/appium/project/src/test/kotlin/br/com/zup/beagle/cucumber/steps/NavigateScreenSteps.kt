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

package br.com.zup.beagle.cucumber.steps

import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When


class NavigateScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/navigate-actions"

    @Before("@navigation")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^the Beagle application did launch with the navigation screen url$")
    fun checkBaseScreen() {
        // ScreenRobot().checkViewContainsText("Navigation Screen", true)
        waitForElementWithTextToBeClickable("Navigation Screen", false, false)
    }

    @When("^I press a navigation button (.*)$")
    fun clickOnButton(string: String) {
        // ScreenRobot().clickOnText(string)
        waitForElementWithTextToBeClickable(string, false, false).click()
    }

    @When("^I press a navigation failure button (.*)$")
    fun clickOnButtonFailure(string: String) {
        // ScreenRobot().clickOnText(string)
        waitForElementWithTextToBeClickable(string, false, false).click()
    }

    @And("^I click on (.*) button$")
    fun clickOnPoPButton(string: String) {
        // ScreenRobot().clickOnText(string)
        waitForElementWithTextToBeClickable(string, false, false).click()
    }

    @Then("^the screen should navigate to another screen with the text label (.*)$")
    fun checkGlobalTextScreen(string2: String) {
        // ScreenRobot().checkViewContainsText(string2, true)
        waitForElementWithTextToBeClickable(string2, false, false)
    }

    @Then("^the screen should not navigate to another screen with the text label (.*)$")
    fun checkGlobalTextScreenIsNotOnView(string2: String) {
        // ScreenRobot().checkViewDoesNotContainsText(string2)
        waitForElementWithTextToBeInvisible(string2, false, false)
    }

    @Then("^the app should dismiss the view that contains (.*)$")
    fun checkTextIsNotOnAnyView(string1: String) {
        // ScreenRobot().checkViewDoesNotContainsText(string1)
        //Assert.assertFalse(screenContainsElementWithText(string1, false))
        waitForElementWithTextToBeInvisible(string1,false, false)
    }

    @Then("^the view that contains the (.*) must still exist$")
    fun checkTextExistsInAView(string2: String) {
        // ScreenRobot().checkViewContainsText(string2, true)
        waitForElementWithTextToBeClickable(string2, false, false)
    }

    @Then("^There must be a retry button with text (.*)$")
    fun checkButtonExistsInAView(string2: String) {
        // ScreenRobot().checkViewContainsText(string2, true)
        waitForElementWithTextToBeClickable(string2, false, false)
    }
}