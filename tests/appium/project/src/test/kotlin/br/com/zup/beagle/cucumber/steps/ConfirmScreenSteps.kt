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
        waitForElementWithTextToBeClickable("Confirm Screen", likeSearch = false, ignoreCase = false)
    }

    @Then("^validate the invoked alerts and its confirm properties:$")
    fun checkAlertProperties(dataTable: DataTable) {
        val okButton = "OK"
        val cancelButton = "CANCEL"
        val rows: List<List<String?>> = dataTable.asLists(String::class.java)
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var buttonTitle = columns[0]!!
            var alertContents = columns[1]!!.split(";")
            var secondAlertTittle: String? = columns[2]

            safeClickOnElement(waitForElementWithTextToBeClickable(buttonTitle, likeSearch = false, ignoreCase = false))

            // checks if the alert appeared on screen with the correct properties
            for (alertContent in alertContents) {
                waitForElementWithTextToBeClickable(alertContent, likeSearch = false, ignoreCase = true)
            }

            if (buttonTitle == "TriggersAnActionWhenConfirmed") {
                waitForElementWithTextToBeClickable("ConfirmMessage", likeSearch = false, ignoreCase = true)
                safeClickOnElement(waitForElementWithTextToBeClickable(okButton, likeSearch = false, ignoreCase = true))
                waitForElementWithTextToBeClickable(secondAlertTittle!!, likeSearch = false, ignoreCase = false)
                safeClickOnElement(waitForElementWithTextToBeClickable(okButton, likeSearch = false, ignoreCase = true))
                waitForElementWithTextToBeInvisible(secondAlertTittle!!, likeSearch = false, ignoreCase = false)
            } else if (buttonTitle == "TriggersAnActionWhenCanceled") {
                waitForElementWithTextToBeClickable("CancelMessage", likeSearch = false, ignoreCase = true)
                safeClickOnElement(
                    waitForElementWithTextToBeClickable(
                        cancelButton,
                        likeSearch = false,
                        ignoreCase = true
                    )
                )
                waitForElementWithTextToBeClickable(secondAlertTittle!!, likeSearch = false, ignoreCase = false)
                safeClickOnElement(waitForElementWithTextToBeClickable(okButton, likeSearch = false, ignoreCase = true))
                waitForElementWithTextToBeInvisible(secondAlertTittle!!, likeSearch = false, ignoreCase = false)
            } else if (buttonTitle == "CustomConfirmLabel") {
                safeClickOnElement(
                    waitForElementWithTextToBeClickable(
                        "CustomLabelOK",
                        likeSearch = false,
                        ignoreCase = true
                    )
                )
                waitForElementWithTextToBeInvisible("CustomLabelOK", likeSearch = false, ignoreCase = false)
            } else {
                safeClickOnElement(
                    waitForElementWithTextToBeClickable(
                        cancelButton,
                        likeSearch = false,
                        ignoreCase = true
                    )
                )
            }

        }
    }
}