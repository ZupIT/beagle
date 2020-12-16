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

private const val LISTVIEW_SCREEN_HEADER = "Beagle ListView"
private const val STATIC_LISTVIEW_TEXT_1 = "Static VERTICAL ListView"
private const val STATIC_LISTVIEW_TEXT_2 = "Static HORIZONTAL ListView"
private const val DYNAMIC_LISTVIEW_TEXT_1 = "Dynamic VERTICAL ListView"
private const val DYNAMIC_LISTVIEW_TEXT_2 = "Dynamic HORIZONTAL ListView"

class ListViewScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/listview"

    @Before("@listview")
    fun setup() {
        loadBffScreenFromMainScreen()
    }


    @Given("^that I'm on the listview screen$")
    fun checkListViewScreen() {
        // ScreenRobot().checkViewContainsText(LISTVIEW_SCREEN_HEADER, true)
        waitForElementWithTextToBeClickable(LISTVIEW_SCREEN_HEADER, false, false)
    }

    @When("^I have a vertical list configured$")
    fun checkVerticalListText() {
        // ScreenRobot().checkViewContainsText(STATIC_LISTVIEW_TEXT_1).sleep(2)
        waitForElementWithTextToBeClickable(STATIC_LISTVIEW_TEXT_1, false, false)
    }

    @Then("^listview screen should render all text attributes correctly$")
    fun checkListViewScreenTexts() {
        /*
        ScreenRobot()
            .checkViewContainsText(STATIC_LISTVIEW_TEXT_1)
            .checkViewContainsText(STATIC_LISTVIEW_TEXT_2)
            .checkViewContainsText(DYNAMIC_LISTVIEW_TEXT_1)

         */
        waitForElementWithTextToBeClickable(STATIC_LISTVIEW_TEXT_1, false, false)
        waitForElementWithTextToBeClickable(STATIC_LISTVIEW_TEXT_2, false, false)
        waitForElementWithTextToBeClickable(DYNAMIC_LISTVIEW_TEXT_1, false, false)
    }

    @Then("^listview screen should perform the scroll action vertically$")
    fun validateVerticalListScroll() {
        // ScreenRobot().scrollTo(DYNAMIC_LISTVIEW_TEXT_2).sleep(2)
        scrollDownToElementWithText(DYNAMIC_LISTVIEW_TEXT_2, false, false)
    }
}