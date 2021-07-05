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

class ButtonScreenSteps : AbstractStep() {

    private val buttonScreenHeader = "Beagle Button"
    private val actionClickText = "You clicked right"

    override var bffRelativeUrlPath = "/button"

    @Before("@button")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the button screen$")
    fun checkButtonScreen() {
        waitForElementWithTextToBeClickable(buttonScreenHeader)
    }

    @Then("^validate button clicks:$")
    fun checkAlertProperties(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            val buttonTitle = columns[0]!!
            val actionText = columns[1]!!
            val buttonElement = waitForElementWithTextToBePresent(buttonTitle, likeSearch = false, ignoreCase = true)

            if (actionText == "Disabled") {
                Assert.assertFalse(buttonElement.isEnabled)
            } else {
                safeClickOnElement(buttonElement)
                waitForElementWithTextToBeClickable(actionClickText)
                goBack()
                waitForElementWithTextToBeInvisible(actionClickText)
            }
        }
    }

}
