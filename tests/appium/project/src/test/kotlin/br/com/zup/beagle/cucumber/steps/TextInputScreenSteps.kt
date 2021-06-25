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
        loadBffScreen()
    }

    @Given("^the Beagle application did launch with the textInput on screen$")
    fun checkBaseScreen() {
        waitForElementWithValueToBeClickable("Beagle Text Input", false, false)
    }

    @Then("^I must check if the textInput value (.*) appears on the screen$")
    fun checkTextInput(string: String) {
        waitForElementWithValueToBeClickable(string, false, false)
    }

    @Then("^I must check if the textInput placeholder (.*) appears on the screen$")
    fun checkTextInputPlaceholder(string: String) {
        waitForElementWithValueToBeClickable(string, false, false)
    }

    @When("^the disabled textInput (.*) is visible$")
    fun checkIfTextInputIsDisabled(string: String) {
        waitForElementWithValueToBeDisabled(string, false, false)
    }

    @Then("^verify if (.*) is disabled$")
    fun checkDisabledField(string: String) {
        waitForElementWithValueToBeDisabled(string, false, false)
    }

    @When("^the value (.*) of the readOnly field is on the screen$")
    fun checkIfTextInputIsReadOnly(string: String) {
        waitForElementWithValueToBeDisabled(string, false, false)
    }

    @Then("^verify if the field with the value (.*) is read only$")
    fun checkReadOnlyField(string: String) {
        waitForElementWithValueToBeDisabled(string, false, false)
    }

    @When("^I click in the textInput with the placeholder (.*)$")
    fun checkTextInputInSecondPlan(string: String) {
        waitForElementWithValueToBeClickable(string, false, false).click()
    }

    @Then("^verify if the text (.*) is in the second plan$")
    fun checkKeyboardFocus(elementText: String) {
        val mobileElement = waitForElementWithValueToBeClickable(elementText,
            likeSearch = false,
            ignoreCase = false,
            nativeLocator = false
        )
        Assert.assertTrue(elementText.equals(mobileElement.text))
        mobileElement.sendKeys("a")
        Assert.assertFalse(elementText.equals(mobileElement.text))
        mobileElement.clear()
        Assert.assertTrue(elementText.equals(mobileElement.text))
    }

    @Then("^validate that the value of the text input component (.*) of type \"date\" is shown correctly$")
    fun checkDateWriting(string: String) {
        val mobileElement = waitForElementWithValueToBeClickable(string,
            likeSearch = false,
            ignoreCase = false,
            nativeLocator = false
        )
        mobileElement.sendKeys("22/04/1500")
        waitForElementWithValueToBeClickable("22/04/1500", false, false)
    }

    @Then("^validate that the value of the text input component (.*) of type \"email\" is shown correctly$")
    fun checkEmailWriting(string: String) {
        val mobileElement = scrollUpToElementWithValue(string, false, false)
        mobileElement.sendKeys("test@abc.com")
        waitForElementWithValueToBeClickable("test@abc.com", false, false)
    }

    @Then("^validate that the value of the text input component (.*) of type \"password\" is shown correctly$")
    fun checkPasswordWriting(textElement: String) {
        val mobileElement = scrollDownToElementWithValue(textElement, false, false)
        mobileElement.sendKeys("123")
        Assert.assertTrue("123" != mobileElement.text) // validates text is in password format
        Assert.assertTrue(mobileElement.text.length == 3)
        Assert.assertTrue(textElement != mobileElement.text)
    }

    @Then("^validate that the value of the text input component (.*) of type \"number\" is shown correctly$")
    fun checkNumberWriting(string: String) {
        val mobileElement = scrollDownToElementWithValue(string, false, false)
        mobileElement.sendKeys("12345678")
        waitForElementWithValueToBeClickable("12345678", false, false)
    }

    @Then("^validate that the value of the text input component (.*) of type \"text\" is shown correctly$")
    fun checkTextWriting(string: String) {
        val mobileElement = scrollDownToElementWithValue(string, false, false)
        mobileElement.sendKeys("This is a test!")
        waitForElementWithValueToBeClickable("This is a test!", false, false)
    }

    @Then("^validate attribute of \"type number\" of textInput component (.*)$")
    fun validateTextsInputNumberType(string: String) {
        scrollDownToElementWithValue(string, false, false)
        Assert.assertTrue(isTextFieldNumeric(string))
    }

    @And("^I click the textInput with the placeholder (.*)$")
    fun textInputWithActionOfOnFocus(string: String) {
        scrollDownToElementWithValue(string, false, false).click()
    }

    @Then("^the textInput with the placeholder \"Ordered actions\" should have value (.*)$")
    fun checkOrderedActions(string: String) {
        waitForElementWithValueToBePresent(string, false, false)
    }

    @Then("^the textInput with the placeholder \"Unordered actions\" will change its value to (.*)$")
    fun textInputWithActionOfOnBlur(string: String) {
        waitForElementWithValueToBePresent(string, false, false)
    }

    @And("^I type anything on textInput with the placeholder (.*)$")
    fun triggersOnChangeMethodAndCheckChanges(string: String) {
        waitForElementWithValueToBeClickable(string, false, false).sendKeys("a")
    }

    @When("^I click to textInput (.*) then change to (.*) and to (.*)$")
    fun textInoutWithActionOfOnFocusAndOnChange(elementText1: String, elementText2: String, elementText3: String) {
        val mobileElement = scrollUpToElementWithValue(elementText1, false, false)
        waitForElementWithValueToBeClickable(elementText2, false, false, false)
        mobileElement.sendKeys("a") // TODO: validar a'?
        waitForElementWithValueToBeClickable(elementText3, false, false)
    }

    @Then("^the text (.*) should be appear in the correctly order$")
    fun textInoutWithActionOfOnBlurCorrectlyOrder(string: String) {
        val mobileElement = waitForElementWithValueToBeClickable("is textInput type number", false, false)
        waitForElementWithValueToBeClickable(string, false, false)
        Assert.assertFalse("DidOnFocus" == mobileElement.text)
        Assert.assertFalse("DidOnFocusDidOnChange" == mobileElement.text)
    }

    @Then("^The hidden input fields (.*) should not be visible$")
    fun checkInputTextIsHidden(string: String) {
        swipeUp()
        swipeUp()
        waitForElementWithValueToBeInvisible(string, false, false)
    }
}