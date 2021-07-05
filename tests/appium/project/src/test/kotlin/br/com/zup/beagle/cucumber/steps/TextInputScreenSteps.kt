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

import br.com.zup.beagle.setup.SuiteSetup
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.junit.Assert

class TextInputScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/textinput"

    @Before("@textInput")
    fun setup() {
        loadBffScreen()
    }

    @Given("^the Beagle application did launch with the textInput on screen$")
    fun checkBaseScreen() {
        waitForElementWithValueToBeClickable("Beagle Text Input")
    }

    @Then("^validate place holders and visibility:$")
    fun validatePlaceHoldersAndVisibility(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            val placeHolder = columns[0]!!
            val isVisible = columns[1]!!.toString().toBoolean()
            val isEnabled = columns[2]!!.toString().toBoolean()

            if (!isVisible) {
                waitForElementWithValueToBeInvisible(placeHolder, ignoreCase = true)
            } else {
                val element = waitForElementWithValueToBePresent(placeHolder, ignoreCase = true)
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

            val placeHolder = columns[0]!!

            when (val validationAction = columns[1]!!) {
                "place holder keeps showing after click" -> {

                    val element = waitForElementWithValueToBeClickable(placeHolder, nativeLocator = false)
                    element.sendKeys("randomValue")
                    Assert.assertFalse(placeHolder == element.text)
                    element.clear()
                    Assert.assertTrue(placeHolder == element.text)

                }
                "validate typed text" -> {
                    when {
                        placeHolder.contains("writing date") -> {

                            val mobileElement = waitForElementWithValueToBeClickable(placeHolder, nativeLocator = false)
                            mobileElement.sendKeys("22/04/1500")
                            waitForElementWithValueToBeClickable("22/04/1500")

                        }
                        placeHolder.contains("writing e-mail") -> {

                            val element = scrollUpToElementWithValue(placeHolder)
                            element.sendKeys("test@abc.com")
                            waitForElementWithValueToBeClickable("test@abc.com")

                        }
                        placeHolder.contains("writing password") -> {

                            val element = scrollDownToElementWithValue(placeHolder)
                            element.sendKeys("1234")
                            Assert.assertTrue("1234" != element.text) // validates text is in password format
                            Assert.assertTrue(element.text.length == 4)
                            Assert.assertTrue(placeHolder != element.text)

                        }
                        placeHolder.contains("writing number") -> {

                            val element = scrollDownToElementWithValue(placeHolder)
                            element.sendKeys("12345678")
                            waitForElementWithValueToBeClickable("12345678")

                        }
                        placeHolder.contains("writing text") -> {

                            val element = scrollDownToElementWithValue(placeHolder)
                            element.sendKeys("This is a test!")
                            waitForElementWithValueToBeClickable("This is a test!")

                        }
                        else -> {
                            throw Exception("Wrong place holder: $placeHolder")
                        }
                    }
                }
                "validate is number only textInput" -> {

                    scrollDownToElementWithValue(placeHolder)
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
            "action validation", nativeLocator = false
        )

        val unorderedActionsTextInputElement = waitForElementWithValueToBePresent(
            "Unordered actions", nativeLocator = false
        )

        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            when (val event = columns[0]!!) {
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
                }
                "DidOnBlur" -> {
                    safeClickOnElement(actionValidationTextInputElement)
                    safeClickOnElement(waitForElementWithValueToBeClickable("is textInput type number"))
                    Assert.assertEquals(event, unorderedActionsTextInputElement.text)
                    if (SuiteSetup.isIos()) {
                        safeClickOnElement(waitForElementWithTextToBeClickable("Done"))
                    } else {
                        hideKeyboard()
                    }


                }
                "DidOnFocusDidOnChangeDidOnBlur" -> {

                    // validate the actions of the textInput when they're executed in sequence
                    val orderedActionsElement =
                        waitForElementWithValueToBePresent("Ordered actions", nativeLocator = false)
                    val actionOrderElement =
                        waitForElementWithValueToBeClickable("action order")
                    safeClickOnElement(actionOrderElement)
                    actionOrderElement.sendKeys("anyText")
                    safeClickOnElement(waitForElementWithValueToBeClickable("is textInput type number"))
                    Assert.assertEquals(event, orderedActionsElement.text)
                }
                else -> {
                    throw Exception("Wrong event: $event")
                }
            }
        }
    }
}