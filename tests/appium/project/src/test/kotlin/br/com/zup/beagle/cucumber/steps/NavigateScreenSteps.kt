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
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then


class NavigateScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/navigate-actions"

    @Before("@navigation")
    fun setup() {
        loadBffScreen()
    }

    @Given("^the Beagle application did launch with the navigation screen url$")
    fun checkBaseScreen() {
        waitForElementWithTextToBeClickable("Navigation Screen", false, false)
    }

    @Then("^There must be a retry button with text (.*) on Android and (.*) on iOS$")
    fun checkButtonExistsInAView(androidText: String, iosText: String) {
        if (SuiteSetup.isIos())
            waitForElementWithTextToBeClickable(iosText, false, true)
        else
            waitForElementWithTextToBeClickable(androidText, false, true)
    }
}