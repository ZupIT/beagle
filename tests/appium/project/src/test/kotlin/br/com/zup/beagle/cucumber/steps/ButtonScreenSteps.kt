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


private const val BUTTON_SCREEN_HEADER = "Beagle Button"
private const val BUTTON_DEFAULT_TEXT = "Button"
private const val BUTTON_WITH_STYLE_TEXT = "Button with style"
private const val BUTTON_WITH_APPEARANCE_TEXT = "Button with Appearance"
private const val ACTION_CLICK_HEADER = "Action Click"
private const val ACTION_CLICK_TEXT = "You clicked right"

class ButtonScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/button"

    @Before("@button")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^that I'm on the button screen$")
    fun checkButtonScreen() {
        //ScreenRobot().checkViewContainsText(BUTTON_SCREEN_HEADER, true)
        waitForElementWithTextToBeClickable(BUTTON_SCREEN_HEADER, false, false)
    }

    @When("I click on a component with a valid style attribute configured$")
    fun clickOnButtonWithStyle() {
        //ScreenRobot().clickOnText(BUTTON_WITH_STYLE_TEXT).sleep(2)
        waitForElementWithTextToBeClickable(BUTTON_WITH_STYLE_TEXT, false, false).click()
    }

    @Then("all my button components should render their respective text attributes correctly$")
    fun renderTextAttributeCorrectly() {
        /*ScreenRobot()
            .checkViewContainsText(BUTTON_DEFAULT_TEXT)
            .checkViewContainsText(BUTTON_WITH_STYLE_TEXT)
            .checkViewContainsText(BUTTON_WITH_APPEARANCE_TEXT)
            .sleep(2)*/
        waitForElementWithTextToBeClickable(BUTTON_DEFAULT_TEXT, false, false)
        waitForElementWithTextToBeClickable(BUTTON_WITH_STYLE_TEXT, false, false)
        waitForElementWithTextToBeClickable(BUTTON_WITH_APPEARANCE_TEXT, false, false)
    }

    @Then("component should render the action attribute correctly$")
    fun renderActionAttributeCorrectly() {
        /*ScreenRobot()
            .checkViewContainsText(ACTION_CLICK_HEADER)
            .checkViewContainsText(ACTION_CLICK_TEXT)
            .sleep(2)*/
        waitForElementWithTextToBeClickable(ACTION_CLICK_HEADER, false, false)
        waitForElementWithTextToBeClickable(ACTION_CLICK_TEXT, false, false)
    }


}
