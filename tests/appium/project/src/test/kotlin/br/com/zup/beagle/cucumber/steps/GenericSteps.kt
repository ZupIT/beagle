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
    fun clickOnButton(string1: String) {
        waitForElementWithTextToBeClickable(string1, false, true).click()
    }

    @When("^Scroll to (.*)$")
    fun scrollTo(string1: String) {
        scrollDownToElementWithText(string1, false, true).click()
    }

    @Then("^The Text should show (.*)$")
    fun textShouldShow(string1: String) {
        waitForElementWithTextToBeClickable(string1, false, true)
    }

    @When("^I click on text (.*)$")
    fun clickOnText(string1: String) {
        waitForElementWithTextToBeClickable(string1, false, true).click()
    }

    @When("^I click on input with hint (.*)$")
    fun clickOnInputWithHint(hint: String) {
        waitForElementWithTextToBeClickable(hint, false, true)
    }

    @When("hide keyboard")
    fun hideKeyboardStep() {
        hideKeyboard()
    }

    @Then("^a dialog should appear on the screen with text (.*)$")
    fun checkDialog(string : String){
        waitForElementWithTextToBeClickable(string, true, true)
    }
}