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
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class SetContextScreenSteps: AbstractStep() {
    override var bffRelativeUrlPath = "/set-context"

    @Before("@setcontext")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^the Beagle application did launch with the SetContext screen url$")
    fun checkBaseScreen() {
        // ScreenRobot().checkViewContainsText("SetContext Screen", true)
        waitForElementWithTextToBeClickable("SetContext Screen", false,false)
    }

    @When("^I press a SetContext button with the (.*) title$")
    fun clickOnButton(string:String) {
        // ScreenRobot().clickOnText(string)
        waitForElementWithTextToBeClickable(string, false, false).click()
    }

    @Then("^a text with the (.*) message should appear on the screen$")
    fun checkAlertMessage(string:String) {
        // ScreenRobot().checkViewContainsText(string, true)
        waitForElementWithTextToBeClickable(string, false,false)
    }
}