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


import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import cucumber.api.java.en.*


class GenericSteps {

    @When("^I click on button (.*)$")
    fun clickOnButton(string1: String?) {
        ScreenRobot()
            .clickOnText(string1)
    }

    @When("^Scroll to (.*)$")
    fun scrollTo(string1: String?) {
        ScreenRobot().scrollTo(string1)
    }

    @Then("^The Text should show (.*)$")
    fun textShouldShow(string1: String?) {
        ScreenRobot().checkViewContainsText(string1)
    }

    @When("^I click on text (.*)$")
    fun clickOnText(string1: String?) {
        ScreenRobot()
            .clickOnText(string1)
    }

    @When("^I click on input with hint (.*)$")
    fun clickOnInputWithHint(hint: String?) {
        ScreenRobot()
            .clickOnInputWithHint(hint)
    }

    @When("hide keyboard")
    fun hideKeyboard() {
        ScreenRobot()
            .hideKeyboard()
    }

}