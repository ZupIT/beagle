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
import org.junit.Assert

class TextStyleScreenSteps: AbstractStep() {
    override var bffRelativeUrlPath = "/text"

    @Before("@textStyle")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^the Beagle application did launch with the texts on the screen$")
    fun checkScreen() {
        //ScreenRobot().checkViewContainsText(BUTTON_SCREEN_HEADER, true)
        waitForElementWithTextToBeClickable("TextScreen", false, false)
        // registerCurrentScreenInDatabase("TextStyleScreen01")
    }

    @Then("^take a screenshot and assert it is identical to the (.*) image$")
    fun checkStyle(imageDbToCompare: String) {
        Assert.assertTrue(compareCurrentScreenWithDatabase(imageDbToCompare))
    }


}