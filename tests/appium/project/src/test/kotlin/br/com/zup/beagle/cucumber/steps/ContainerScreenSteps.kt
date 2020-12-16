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

class ContainerScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/container-test"

    @Before("@container")
    fun setup() {
        // TestUtils.startActivity(activityTestRule, CONDITIONAL_SCREEN_BFF_URL)
        loadBffScreenFromMainScreen()
    }

    @Given("^the Beagle application did launch with the container screen url$")
    fun checkBaseScreen() {
        // ScreenRobot().checkViewContainsText("Container Screen", true)
        waitForElementWithTextToBeClickable("Container Screen", false, false)
    }

    @Then("^the view that contains the texts with titles (.*) (.*) and (.*) must be displayed$")
    fun checkTextExistsInAView(string1:String, string2:String, string3:String) {
        /*
        ScreenRobot()
            .checkViewContainsText(string1, true)
            .checkViewContainsText(string2, true)
            .checkViewContainsText(string3, true)
         */
        waitForElementWithTextToBeClickable(string1, false, false)
        waitForElementWithTextToBeClickable(string2, false, false)
        waitForElementWithTextToBeClickable(string3, false, false)
    }

    @Then("^the views that contains the text (.*) set via context must be displayed$")
    fun checkTextExistsInAViewSetViaContext(string1:String) {
        // ScreenRobot().checkViewContainsText(string1, true)
        waitForElementWithTextToBeClickable(string1, false, false)
    }


}