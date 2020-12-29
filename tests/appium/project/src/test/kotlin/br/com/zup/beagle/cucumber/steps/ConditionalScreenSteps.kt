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

class ConditionalScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/conditional"

    @Before("@conditional")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^the Beagle application did launch with the conditional screen url$")
    fun checkBaseScreen() {
        waitForElementWithTextToBeClickable("Conditional Screen", false, false)
    }

    @When("^I click in a conditional button with (.*) title$")
    fun clickOnButton(string: String) {
        waitForElementWithTextToBeClickable(string, false, false).click()
    }

    @Then("^an Alert action should pop up with a (.*) message$")
    fun checkGlobalTextScreen(string2: String) {
        waitForElementWithTextToBeClickable(string2, false, false)
    }

}