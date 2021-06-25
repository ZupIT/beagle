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


private const val BUTTON_SCREEN_HEADER = "Beagle Button"
private const val ACTION_CLICK_TEXT = "You clicked right"

class ButtonScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/button"

    @Before("@button")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the button screen$")
    fun checkButtonScreen() {
        waitForElementWithTextToBeClickable(BUTTON_SCREEN_HEADER, likeSearch = false, ignoreCase = false)
    }

    @Then("^validate button clicks:$")
    fun checkAlertProperties(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var buttonTitle = columns[0]!!
            var actionText = columns[1]!!
            var buttonElement = waitForElementWithTextToBePresent(buttonTitle, likeSearch = false, ignoreCase = true)

            if (actionText == "Disabled") {
                Assert.assertFalse(buttonElement.isEnabled)
            } else {
                safeClickOnElement(buttonElement)
                waitForElementWithTextToBeClickable(ACTION_CLICK_TEXT, likeSearch = false, ignoreCase = false)
                goBack()
                waitForElementWithTextToBeInvisible(ACTION_CLICK_TEXT, likeSearch = false, ignoreCase = false)
            }
        }
    }

}
