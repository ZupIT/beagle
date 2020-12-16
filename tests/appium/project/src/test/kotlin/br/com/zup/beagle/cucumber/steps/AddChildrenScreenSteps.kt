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
import org.junit.Assert

private const val ADD_CHILDREN_HEADER = "Add Children"
private const val TEXT_FIXED = "I'm the single text on screen"
private const val TEXT_ADDED = "New text added"

@Suppress("unused")
class AddChildrenScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/add-children"

    @Before("@addChildren")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^that I'm on the addChildren Screen$")
    fun checkImageScreen() {
        //ScreenRobot().checkViewContainsText(ADD_CHILDREN_HEADER, true)
        waitForElementWithTextToBeClickable(ADD_CHILDREN_HEADER, false, false)
    }

    @Then("^A Text needs to be added after the already existing one$")
    fun checkTextAddedAfterTheExistedOrder() {
        // waitForBothTexts()
        // onView(withText(TEXT_FIXED)).check(isCompletelyAbove(withText(TEXT_ADDED)))
        waitForFixedAndAddedTexts()
        Assert.assertTrue(isElementAbove(TEXT_FIXED, TEXT_ADDED, false, false))
    }

    @Then("^A Text needs to be added before the already existing one$")
    fun checkTextAddedBeforeTheExistedOrder() {
        // waitForBothTexts()
        // onView(withText(TEXT_ADDED)).check(isCompletelyAbove(withText(TEXT_FIXED)))
        waitForFixedAndAddedTexts()
        Assert.assertTrue(isElementAbove(TEXT_ADDED, TEXT_FIXED, false, false))
    }

    @Then("^A Text needs to replace the already existing one$")
    fun checkTextReplaceTheExistedOne() {
        //waitForTextAdded()
        //onView((withText(TEXT_ADDED))).check(matches(isDisplayed()))
        //onView(withId(CONTAINER_ID.toAndroidId())).check(matches(not(withText(TEXT_FIXED))))
        waitForAddedText()
        waitForElementWithTextToBeInvisible(TEXT_FIXED, false, false)
    }

    @Then("^Nothing should happen$")
    fun checkTextAddedPositionIsNull() {
        //waitForTextFixed()
        //onView((withText(TEXT_FIXED))).check(matches(isDisplayed()))
        //onView(withId(CONTAINER_ID.toAndroidId())).check(matches(not(withText(TEXT_ADDED))))
        waitForFixedText()
        waitForElementWithTextToBeInvisible(TEXT_ADDED, false, false)
    }

    private fun waitForFixedAndAddedTexts() {
        waitForFixedText()
        waitForAddedText()
    }

    private fun waitForFixedText() {
        // ScreenRobot().checkViewContainsText(TEXT_FIXED, true)
        waitForElementWithTextToBeClickable(TEXT_FIXED, false, false)
    }

    private fun waitForAddedText() {
        // ScreenRobot().checkViewContainsText(TEXT_ADDED, true)
        waitForElementWithTextToBeClickable(TEXT_ADDED, false, false)
    }
}