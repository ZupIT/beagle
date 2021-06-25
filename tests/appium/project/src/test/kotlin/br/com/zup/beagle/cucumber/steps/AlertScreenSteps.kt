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
import io.cucumber.java.en.When
import org.junit.Assert


class AlertScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/alert"

    @Before("@alert")
    fun setup() {
        loadBffScreen()
    }

    @Given("^the Beagle application did launch with the alert screen url$")
    fun checkBaseScreen() {
        waitForElementWithTextToBeClickable("Alert Screen", false, false)
    }


    @Then("^validate the invoked alerts and its properties:$")
    fun checkAlertProperties(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var buttonTitle = columns[0]!!
            var alertTitle = columns[1]!!
            var alertMessage: String? = columns[2]
            var alertButtonTitle = columns[3]!!

            safeClickOnElement(waitForElementWithTextToBeClickable(buttonTitle, likeSearch = false, ignoreCase = false))

            // checks if the alert appeared on screen with the correct properties
            waitForElementWithTextToBeClickable(alertTitle, likeSearch = false, ignoreCase = false)
            if (alertMessage != null && alertMessage.trim().isNotEmpty()) {
                waitForElementWithTextToBeClickable(alertMessage, likeSearch = false, ignoreCase = false)
            }

            // checks if clicking on the alert button closes it
            safeClickOnElement(waitForElementWithTextToBeClickable(alertButtonTitle, likeSearch = false, ignoreCase = true))
            waitForElementWithTextToBeInvisible(alertTitle, likeSearch = false, ignoreCase = false)
        }
    }


}