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

class ConfirmScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/confirm"

    @Before("@confirm")
    fun setup() {
        loadBffScreen()
    }

    @Given("^the Beagle application did launch with the confirm screen url$")
    fun checkBaseScreen() {
        waitForElementWithTextToBeClickable("Confirm Screen")
    }

    @Then("^validate the invoked alerts and its confirm properties:$")
    fun checkAlertProperties(dataTable: DataTable) {
        val okButton = "OK"
        val cancelButton = "CANCEL"
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            val buttonTitle = columns[0]!!
            val alertContents = columns[1]!!.split(";")
            val secondAlertTittle: String? = columns[2]

            safeClickOnElement(waitForElementWithTextToBeClickable(buttonTitle))

            // checks if the alert appeared on screen with the correct properties
            for (alertContent in alertContents) {
                waitForElementWithTextToBeClickable(alertContent, ignoreCase = true)
            }

            when (buttonTitle) {
                "TriggersAnActionWhenConfirmed" -> {
                    waitForElementWithTextToBeClickable("ConfirmMessage", ignoreCase = true)
                    safeClickOnElement(waitForElementWithTextToBeClickable(okButton, ignoreCase = true))
                    waitForElementWithTextToBeClickable(secondAlertTittle!!)
                    safeClickOnElement(waitForElementWithTextToBeClickable(okButton, ignoreCase = true))
                    waitForElementWithTextToBeInvisible(secondAlertTittle)
                }
                "TriggersAnActionWhenCanceled" -> {
                    waitForElementWithTextToBeClickable("CancelMessage", ignoreCase = true)
                    safeClickOnElement(waitForElementWithTextToBeClickable(cancelButton, ignoreCase = true))
                    waitForElementWithTextToBeClickable(secondAlertTittle!!)
                    safeClickOnElement(waitForElementWithTextToBeClickable(okButton, ignoreCase = true))
                    waitForElementWithTextToBeInvisible(secondAlertTittle)
                }
                "CustomConfirmLabel" -> {
                    safeClickOnElement(waitForElementWithTextToBeClickable("CustomLabelOK", ignoreCase = true))
                    waitForElementWithTextToBeInvisible("CustomLabelOK")
                }
                else -> {
                    safeClickOnElement(waitForElementWithTextToBeClickable(cancelButton, ignoreCase = true))
                }
            }
        }
    }
}