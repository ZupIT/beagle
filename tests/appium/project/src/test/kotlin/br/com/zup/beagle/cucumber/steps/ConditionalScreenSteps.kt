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

class ConditionalScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/conditional"

    @Before("@conditional")
    fun setup() {
        loadBffScreen()
    }

    @Given("^the Beagle application did launch with the conditional screen url$")
    fun checkBaseScreen() {
        waitForElementWithTextToBeClickable("Conditional Screen", likeSearch = false, ignoreCase = false)
    }

    @Then("^validate the invoked alerts and its message:$")
    fun checkAlertProperties(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var buttonTitle = columns[0]!!
            var alertMessage = columns[1]!!

            safeClickOnElement(waitForElementWithTextToBeClickable(buttonTitle, likeSearch = false, ignoreCase = false))
            waitForElementWithTextToBeClickable(alertMessage, likeSearch = false, ignoreCase = false)
            safeClickOnElement(waitForElementWithTextToBeClickable("OK", likeSearch = false, ignoreCase = true))
            waitForElementWithTextToBeInvisible("OK", likeSearch = false, ignoreCase = true)
        }
    }
}