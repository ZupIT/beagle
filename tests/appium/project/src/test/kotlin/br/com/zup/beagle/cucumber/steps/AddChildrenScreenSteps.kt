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
        loadBffScreen()
    }

    @Given("^that I'm on the addChildren Screen$")
    fun checkImageScreen() {
        waitForElementWithTextToBeClickable(ADD_CHILDREN_HEADER, likeSearch = false, ignoreCase = false)
    }

    @Then("^A text needs to be added after the already existing one$")
    fun checkTextAddedAfterTheExistedOrder() {
        waitForFixedAndAddedTexts()
        Assert.assertTrue(isElementAbove(TEXT_FIXED, TEXT_ADDED, likeSearch = false, ignoreCase = false))
    }

    @Then("^A text needs to be added before the already existing one$")
    fun checkTextAddedBeforeTheExistedOrder() {
        waitForFixedAndAddedTexts()
        Assert.assertTrue(isElementAbove(TEXT_ADDED, TEXT_FIXED, likeSearch = false, ignoreCase = false))
    }

    @Then("^A text needs to replace the already existing one$")
    fun checkTextReplaceTheExistedOne() {
        waitForAddedText()
        waitForElementWithTextToBeInvisible(TEXT_FIXED, likeSearch = false, ignoreCase = false)
    }

    @Then("^Nothing should happen when clicking in the following buttons and the component doesn't exist:$")
    fun checkNothingHappens(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var buttonTitle = columns[0]!!
            safeClickOnElement(waitForElementWithTextToBeClickable(buttonTitle, likeSearch = false, ignoreCase = true))
            waitForFixedText()
            waitForElementWithTextToBeInvisible(TEXT_ADDED, likeSearch = false, ignoreCase = false)
        }
    }

    private fun waitForFixedAndAddedTexts() {
        waitForFixedText()
        waitForAddedText()
    }

    private fun waitForFixedText() {
        waitForElementWithTextToBeClickable(TEXT_FIXED, likeSearch = false, ignoreCase = false)
    }

    private fun waitForAddedText() {
        waitForElementWithTextToBeClickable(TEXT_ADDED, likeSearch = false, ignoreCase = false)
    }
}