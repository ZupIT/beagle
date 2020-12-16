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
import org.junit.Assert

class TextInputScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/textinput"

    @Before("@textinput")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^the Beagle application did launch with the textInput on screen$")
    fun checkBaseScreen() {
        // ScreenRobot().checkViewContainsText("Beagle Text Input", true)
        waitForElementWithValueToBeClickable("Beagle Text Input", false, false)
    }

    @Then("^I must check if the textInput value (.*) appears on the screen$")
    fun checkTextInput(string: String) {
        // ScreenRobot().checkViewContainsText(string, true)
        waitForElementWithValueToBeClickable(string, false, false)
    }

    @Then("^I must check if the textInput placeholder (.*) appears on the screen$")
    fun checkTextInputPlaceholder(string: String) {
        // ScreenRobot().checkViewContainsHint(string, true)
        waitForElementWithValueToBeClickable(string, false, false)
    }

    @When("^the disabled textInput (.*) is visible$")
    fun checkIfTextInputIsDisabled(string: String) {
        // ScreenRobot().checkViewContainsHint(string)
        waitForElementWithValueToBeDisabled(string, false, false)
    }

    @Then("^verify if its (.*) is disabled$")
    fun checkDisabledField(string: String) {
        // ScreenRobot().disabledFieldHint(string)
        waitForElementWithValueToBeDisabled(string, false, false)


    }

    @When("^the value (.*) of the readOnly field is on the screen$")
    fun checkIfTextInputIsReadOnly(string: String) {
        // ScreenRobot().checkViewContainsText(string)
        waitForElementWithValueToBeDisabled(string, false, false)
    }

    @Then("^verify if the field with the value (.*) is read only$")
    fun checkReadOnlyField(string: String) {
        // ScreenRobot().disabledFieldText(string)
        waitForElementWithValueToBeDisabled(string, false, false)
    }

    @When("^I click in the textInput with the placeholder (.*)$")
    fun checkTextInputInSecondPlan(string: String) {
        /*
        ScreenRobot()
            .checkViewContainsHint(string)
            .clickOnInputWithHint(string)
            */
        waitForElementWithValueToBeClickable(string, false, false).click()
    }

    @Then("^verify if the text (.*) is in the second plan$")
    fun checkKeyboardFocus(string: String) {
        /*
        ScreenRobot()
            .hintInSecondPlan(string)
            */
        val mobileElement = waitForElementWithValueToBeClickable(string, false, false)
        Assert.assertTrue(string.equals(mobileElement.text))
        mobileElement.sendKeys("a")
        Assert.assertFalse(string.equals(mobileElement.text))
        mobileElement.clear()
        Assert.assertTrue(string.equals(mobileElement.text))
    }

    @Then("^validate that a textInput (.*) of type number is set$")
    fun validateTextsInputNumberType(string: String) {
        /*ScreenRobot()
            .checkInputTypeNumber(string)
            */
        Assert.assertTrue(isTextFieldNumeric(string))
    }

    @And("^I click the textInput with the placeholder (.*)$")
    fun textInputWithActionOfOnFocusAndOnFocus(string: String) {
        /*ScreenRobot()
            .scrollToWithHint(string)
            .clickOnInputWithHint(string)*/
        swipeUp()
        waitForElementWithValueToBeClickable(string, false, false).click()
    }

    @Then("^the textInput with the placeholder \"Ordered actions\" should have value (.*)$")
    fun checkOrderedActions(string: String) {
        // ScreenRobot().checkViewContainsText(string)
        waitForElementWithValueToBeDisabled(string, false, false)
    }

    @Then("^the textInput with the placeholder \"Unordered actions\" will change its value to (.*)$")
    fun textInputWithActionOfOnBlur(string: String) {
        // ScreenRobot().checkViewContainsText(string)
        waitForElementWithValueToBeDisabled(string, false, false)

    }

    @And("^I type anything on textInput with the placeholder (.*)$")
    fun triggersOnChangeMethodAndCheckChanges(string: String) {
        // ScreenRobot().typeText(string, "a")
        waitForElementWithValueToBeClickable(string, false, false).sendKeys("a")
    }

    @Then("^The hidden input fields (.*) should not be visible$")
    fun checkInputTextIsHidden(string: String) {
        /* ScreenRobot()
            .scrollTo("There are two hidden input fields above")
            .checkViewIsNotDisplayed(string)
            */
        swipeUp()
        waitForElementWithValueToBeClickable("There are two hidden input fields above", false, false)
        waitForElementWithValueToBeInvisible(string, false, false)
    }
}