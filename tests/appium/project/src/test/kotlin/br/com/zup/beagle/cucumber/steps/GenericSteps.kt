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

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert


class GenericSteps : AbstractStep() {

    override var bffRelativeUrlPath = ""

    @When("^I click on button (.*)$")
    fun clickOnButton(buttonText: String) {
        safeClickOnElement(waitForElementWithTextToBeClickable(buttonText, likeSearch = false, ignoreCase = true))
    }

    @Then("^The Text should show (.*)$")
    fun textShouldShow(text: String) {
        waitForElementWithTextToBeClickable(text, likeSearch = false, ignoreCase = true)
    }

    @When("^I click on text (.*)$")
    fun clickOnText(text: String) {
        safeClickOnElement(waitForElementWithTextToBeClickable(text, likeSearch = false, ignoreCase = true))
    }

    @When("^I click on the input with place holder \"(.*)\" and insert \"(.*)\"$")
    fun clickOnInputWithHint(placeHolder: String, value: String) {
        val element = waitForElementWithValueToBeClickable(placeHolder, likeSearch = false, ignoreCase = true)
        safeClickOnElement(element)
        element.sendKeys(value)
    }

    @When("hide keyboard")
    fun hideKeyboardStep() {
        hideKeyboard()
    }

    @Then("^a dialog should appear on the screen with text (.*)$")
    fun checkDialog(text: String) {
        waitForElementWithTextToBeClickable(text, likeSearch = true, ignoreCase = true)
    }

    @Then("^the screen should show an element with the place holder (.*)$")
    fun checkElementByPlaceHolder(placeHolderText: String) {
        waitForElementWithValueToBeClickable(placeHolderText, likeSearch = false, ignoreCase = false)
    }

    @Then("^the screen should show an element with the title (.*)$")
    fun checkElementByTitle(titleText: String) {
        waitForElementWithTextToBeClickable(titleText, likeSearch = false, ignoreCase = true)
    }

    @Then("^the screen should not show an element with the title (.*)$")
    fun checkElementNotVisibleByTitle(titleText: String) {
        waitForElementWithTextToBeInvisible(titleText, likeSearch = false, ignoreCase = false)
    }
}