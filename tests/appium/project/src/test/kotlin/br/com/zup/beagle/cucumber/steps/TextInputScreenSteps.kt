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

import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert

class TextInputScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/textinput"

    @Before("@textInput")
    fun setup() {
        loadBffScreen()
    }

    @Given("^the Beagle application did launch with the textInput on screen$")
    fun checkBaseScreen() {
        waitForElementWithValueToBeClickable("Beagle Text Input", false, false)
    }

    @Then("^validate place holders and visibility:$")
    fun validatePlaceHoldersAndVisibility(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var placeHolder = columns[0]!!
            var isVisible = columns[1]!!.toString().toBoolean()
            var isEnabled = columns[2]!!.toString().toBoolean()

            if (!isVisible) {
                waitForElementWithValueToBeInvisible(placeHolder, likeSearch = false, ignoreCase = true)
            } else {
                val element = waitForElementWithValueToBePresent(placeHolder, likeSearch = false, ignoreCase = true)
                Assert.assertEquals(element.isEnabled, isEnabled)
            }
        }
    }

    @Then("^validate clicks and input types:$")
    fun validateClicksAndInputTypes(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var placeHolder = columns[0]!!
            var validationAction = columns[1]!!

            when (validationAction) {
                "place holder keeps showing after click" -> {

                    val element = waitForElementWithValueToBeClickable(
                        placeHolder,
                        likeSearch = false,
                        ignoreCase = false,
                        nativeLocator = false
                    )
                    element.sendKeys("randomValue")
                    Assert.assertFalse(placeHolder == element.text)
                    element.clear()
                    Assert.assertTrue(placeHolder == element.text)

                }
                "validate typed text" -> {
                    if (placeHolder.contains("writing date")) {

                        val mobileElement = waitForElementWithValueToBeClickable(
                            placeHolder,
                            likeSearch = false,
                            ignoreCase = false,
                            nativeLocator = false
                        )
                        mobileElement.sendKeys("22/04/1500")
                        waitForElementWithValueToBeClickable("22/04/1500", likeSearch = false, ignoreCase = false)

                    } else if (placeHolder.contains("writing e-mail")) {

                        val element = scrollUpToElementWithValue(placeHolder, likeSearch = false, ignoreCase = false)
                        element.sendKeys("test@abc.com")
                        waitForElementWithValueToBeClickable("test@abc.com", likeSearch = false, ignoreCase = false)

                    } else if (placeHolder.contains("writing password")) {

                        val element = scrollDownToElementWithValue(placeHolder, likeSearch = false, ignoreCase = false)
                        element.sendKeys("1234")
                        Assert.assertTrue("1234" != element.text) // validates text is in password format
                        Assert.assertTrue(element.text.length == 4)
                        Assert.assertTrue(placeHolder != element.text)

                    } else if (placeHolder.contains("writing number")) {

                        val element = scrollDownToElementWithValue(placeHolder, likeSearch = false, ignoreCase = false)
                        element.sendKeys("12345678")
                        waitForElementWithValueToBeClickable("12345678", likeSearch = false, ignoreCase = false)

                    } else if (placeHolder.contains("writing text")) {

                        val element = scrollDownToElementWithValue(placeHolder, likeSearch = false, ignoreCase = false)
                        element.sendKeys("This is a test!")
                        waitForElementWithValueToBeClickable("This is a test!", likeSearch = false, ignoreCase = false)

                    } else {
                        throw Exception("Wrong place holder: $placeHolder")
                    }
                }
                "validate is number only textInput" -> {

                    scrollDownToElementWithValue(placeHolder, likeSearch = false, ignoreCase = false)
                    Assert.assertTrue(isTextFieldNumeric(placeHolder))

                }
                else -> {
                    throw Exception("Wrong validation action: $validationAction")
                }
            }
        }
    }

    @Then("^validate textInput events:$")
    fun validateEvents(dataTable: DataTable) {

        swipeUp()

        val actionValidationTextInputElement = waitForElementWithValueToBeClickable(
            "action validation",
            likeSearch = false, ignoreCase = false, nativeLocator = false
        )

        val unorderedActionsTextInputElement = waitForElementWithValueToBeClickable(
            "Unordered actions",
            likeSearch = false, ignoreCase = false, nativeLocator = false
        )

        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var event = columns[1]!!

            when (event) {
                "DidOnFocus" -> {
                    safeClickOnElement(actionValidationTextInputElement)
                    Assert.assertEquals(event, unorderedActionsTextInputElement.text)
                    hideKeyboard()
                }
                "DidOnChange" -> {
                    safeClickOnElement(actionValidationTextInputElement)
                    actionValidationTextInputElement.sendKeys("anyText")
                    Assert.assertEquals(event, unorderedActionsTextInputElement.text)
                    hideKeyboard()
                    actionValidationTextInputElement.clear()
                }
                "DidOnBlur" -> {
                    safeClickOnElement(actionValidationTextInputElement)
                    safeClickOnElement(waitForElementWithValueToBeClickable("textInput type number", ignoreCase = false, likeSearch = false))
                    Assert.assertEquals(event, unorderedActionsTextInputElement.text)
                    hideKeyboard()
                }
                "DidOnFocusDidOnChangeDidOnBlur" -> {

                    // validate the actions of the textInput when they're executed in sequence
                    val orderedActionsElement = waitForElementWithValueToBeClickable("Ordered actions", ignoreCase = false, likeSearch = false)
                    safeClickOnElement(actionValidationTextInputElement)
                    actionValidationTextInputElement.sendKeys("anyText")
                    safeClickOnElement(waitForElementWithValueToBeClickable("textInput type number", ignoreCase = false, likeSearch = false))
                    Assert.assertEquals(event, orderedActionsElement.text)
                }
                else -> {
                    throw Exception("Wrong event: $event")
                }
            }


        }
    }


//    @Then("^I must check if the textInput value (.*) appears on the screen$")
//    fun checkTextInput(string: String) {
//        waitForElementWithValueToBeClickable(string, false, false)
//    }
//
//    @Then("^I must check if the textInput placeholder (.*) appears on the screen$")
//    fun checkTextInputPlaceholder(string: String) {
//        waitForElementWithValueToBeClickable(string, false, false)
//    }
//
//    @When("^the disabled textInput (.*) is visible$")
//    fun checkIfTextInputIsDisabled(string: String) {
//        waitForElementWithValueToBeDisabled(string, false, false)
//    }
//
//    @Then("^verify if (.*) is disabled$")
//    fun checkDisabledField(string: String) {
//        waitForElementWithValueToBeDisabled(string, false, false)
//    }
//
//    @When("^the value (.*) of the readOnly field is on the screen$")
//    fun checkIfTextInputIsReadOnly(string: String) {
//        waitForElementWithValueToBeDisabled(string, false, false)
//    }
//
//    @Then("^verify if the field with the value (.*) is read only$")
//    fun checkReadOnlyField(string: String) {
//        waitForElementWithValueToBeDisabled(string, false, false)
//    }
//
//    @When("^I click in the textInput with the placeholder (.*)$")
//    fun checkTextInputInSecondPlan(string: String) {
//        waitForElementWithValueToBeClickable(string, false, false).click()
//    }
//
//    @Then("^verify if the text (.*) is in the second plan$")
//    fun checkKeyboardFocus(elementText: String) {
//        val mobileElement = waitForElementWithValueToBeClickable(elementText,
//            likeSearch = false,
//            ignoreCase = false,
//            nativeLocator = false
//        )
//        Assert.assertTrue(elementText.equals(mobileElement.text))
//        mobileElement.sendKeys("a")
//        Assert.assertFalse(elementText.equals(mobileElement.text))
//        mobileElement.clear()
//        Assert.assertTrue(elementText.equals(mobileElement.text))
//    }
//
//    @Then("^validate that the value of the text input component (.*) of type \"date\" is shown correctly$")
//    fun checkDateWriting(string: String) {
//        val mobileElement = waitForElementWithValueToBeClickable(string,
//            likeSearch = false,
//            ignoreCase = false,
//            nativeLocator = false
//        )
//        mobileElement.sendKeys("22/04/1500")
//        waitForElementWithValueToBeClickable("22/04/1500", false, false)
//    }
//
//    @Then("^validate that the value of the text input component (.*) of type \"email\" is shown correctly$")
//    fun checkEmailWriting(string: String) {
//        val mobileElement = scrollUpToElementWithValue(string, false, false)
//        mobileElement.sendKeys("test@abc.com")
//        waitForElementWithValueToBeClickable("test@abc.com", false, false)
//    }
//
//    @Then("^validate that the value of the text input component (.*) of type \"password\" is shown correctly$")
//    fun checkPasswordWriting(textElement: String) {
//        val mobileElement = scrollDownToElementWithValue(textElement, false, false)
//        mobileElement.sendKeys("123")
//        Assert.assertTrue("123" != mobileElement.text) // validates text is in password format
//        Assert.assertTrue(mobileElement.text.length == 3)
//        Assert.assertTrue(textElement != mobileElement.text)
//    }
//
//    @Then("^validate that the value of the text input component (.*) of type \"number\" is shown correctly$")
//    fun checkNumberWriting(string: String) {
//        val mobileElement = scrollDownToElementWithValue(string, false, false)
//        mobileElement.sendKeys("12345678")
//        waitForElementWithValueToBeClickable("12345678", false, false)
//    }
//
//    @Then("^validate that the value of the text input component (.*) of type \"text\" is shown correctly$")
//    fun checkTextWriting(string: String) {
//        val mobileElement = scrollDownToElementWithValue(string, false, false)
//        mobileElement.sendKeys("This is a test!")
//        waitForElementWithValueToBeClickable("This is a test!", false, false)
//    }
//
//    @Then("^validate attribute of \"type number\" of textInput component (.*)$")
//    fun validateTextsInputNumberType(string: String) {
//        scrollDownToElementWithValue(string, false, false)
//        Assert.assertTrue(isTextFieldNumeric(string))
//    }
//
//    @And("^I click the textInput with the placeholder (.*)$")
//    fun textInputWithActionOfOnFocus(string: String) {
//        scrollDownToElementWithValue(string, false, false).click()
//    }
//
//    @Then("^the textInput with the placeholder \"Ordered actions\" should have value (.*)$")
//    fun checkOrderedActions(string: String) {
//        waitForElementWithValueToBePresent(string, false, false)
//    }
//
//    @Then("^the textInput with the placeholder \"Unordered actions\" will change its value to (.*)$")
//    fun textInputWithActionOfOnBlur(string: String) {
//        waitForElementWithValueToBePresent(string, false, false)
//    }
//
//    @And("^I type anything on textInput with the placeholder (.*)$")
//    fun triggersOnChangeMethodAndCheckChanges(string: String) {
//        waitForElementWithValueToBeClickable(string, false, false).sendKeys("a")
//    }
//
//    @When("^I click to textInput (.*) then change to (.*) and to (.*)$")
//    fun textInoutWithActionOfOnFocusAndOnChange(elementText1: String, elementText2: String, elementText3: String) {
//        val mobileElement = scrollUpToElementWithValue(elementText1, false, false)
//        waitForElementWithValueToBeClickable(elementText2, false, false, false)
//        mobileElement.sendKeys("a") // TODO: validar a'?
//        waitForElementWithValueToBeClickable(elementText3, false, false)
//    }
//
//    @Then("^the text (.*) should be appear in the correctly order$")
//    fun textInoutWithActionOfOnBlurCorrectlyOrder(string: String) {
//        val mobileElement = waitForElementWithValueToBeClickable("is textInput type number", false, false)
//        waitForElementWithValueToBeClickable(string, false, false)
//        Assert.assertFalse("DidOnFocus" == mobileElement.text)
//        Assert.assertFalse("DidOnFocusDidOnChange" == mobileElement.text)
//    }
//
//    @Then("^The hidden input fields (.*) should not be visible$")
//    fun checkInputTextIsHidden(string: String) {
//        swipeUp()
//        swipeUp()
//        waitForElementWithValueToBeInvisible(string, false, false)
//    }
}