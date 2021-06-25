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

private const val TOUCHABLE_SCREEN_HEADER = "Beagle Touchable"
private const val TOUCHABLE_REDIRECT_TEXT = "You clicked right"

class TouchScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/touchable"

    @Before("@touchable")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the touchable screen$")
    fun checkImageScreen() {
        waitForElementWithTextToBeClickable(TOUCHABLE_SCREEN_HEADER, false, false)
    }

    @Then("^validate touchable clicks:$")
    fun checkTouchableCliks(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue
            var touchableText = columns[0]!!

            if ("Text 1" == touchableText) {
                waitForElementWithTextToBeClickable("Click here!", likeSearch = false, ignoreCase = false).click()
            } else if ("Image 1" == touchableText) {
                safeClickOnElement(waitForImageElements()[0])
            } else if ("Image 2" == touchableText) {
                safeClickOnElement(waitForImageElements()[1])
            }

            waitForElementWithTextToBeClickable(TOUCHABLE_REDIRECT_TEXT, likeSearch = false, ignoreCase = false)
            goBack()
            waitForElementWithTextToBeInvisible(TOUCHABLE_REDIRECT_TEXT, likeSearch = false, ignoreCase = false)

        }
    }
}