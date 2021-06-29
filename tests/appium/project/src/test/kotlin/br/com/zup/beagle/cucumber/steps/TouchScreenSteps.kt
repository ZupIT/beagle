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

class TouchScreenSteps : AbstractStep() {

    private val touchableScreenHeader = "Beagle Touchable"
    private val touchableRedirectText = "You clicked right"

    override var bffRelativeUrlPath = "/touchable"

    @Before("@touchable")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the touchable screen$")
    fun checkImageScreen() {
        waitForElementWithTextToBeClickable(touchableScreenHeader, likeSearch = false, ignoreCase = false)
    }

    @Then("^validate touchable clicks:$")
    fun checkTouchableCliks(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue
            var touchableText = columns[0]!!

            when (touchableText) {
                "Text 1" -> {
                    waitForElementWithTextToBeClickable("Click here!", likeSearch = false, ignoreCase = false).click()
                }
                "Image 1" -> {
                    safeClickOnElement(waitForImageElements()[0])
                }
                "Image 2" -> {
                    safeClickOnElement(waitForImageElements()[1])
                }
            }

            waitForElementWithTextToBeClickable(touchableRedirectText, likeSearch = false, ignoreCase = false)
            goBack()
            waitForElementWithTextToBeInvisible(touchableRedirectText, likeSearch = false, ignoreCase = false)

        }
    }
}