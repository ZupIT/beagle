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

class ConfirmScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/confirm"

    @Before("@confirm")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^the Beagle application did launch with the confirm screen url$")
    fun checkBaseScreen() {
        waitForElementWithTextToBeClickable("Confirm Screen", false, false)
    }

    @When("^I press a confirm button with the (.*) title$")
    fun clickOnButton(string: String) {
        waitForElementWithTextToBeClickable(string, false, false).click()
    }

    @Then("^a confirm with the (.*) message should appear on the screen$")
    fun checkConfirmMessage(string: String) {
        waitForElementWithTextToBeClickable(string, false, false)
    }

    @Then("^a confirm with the (.*) and (.*) should appear on the screen$")
    fun checkConfirmMessageAndTitle(string: String, string2: String) {
        waitForElementWithTextToBeClickable(string, false, true)
        waitForElementWithTextToBeClickable(string2, false, true)
    }

    @Then("^I press the confirmation (.*) button on the confirm component$")
    fun clickOnTheConfirmationActionButtonWithText(string: String) {
        waitForElementWithTextToBeClickable(string, false, true).click()
    }

    @Then("^a confirm with a button with (.*) label should appear$")
    fun checkConfirmationButtonLabelIsSetWithText(string: String) {
        waitForElementWithTextToBeClickable(string, false, false)
    }
}