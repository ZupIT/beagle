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


class TabBarScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/tabbar"

    @Before("@tabBar")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the tabBar screen$")
    fun checkTabBarScreen() {
        waitForElementWithTextToBeClickable("TabBar Screen", ignoreCase = true)
    }

    @Then("^the tab position should have its text changed to (.*)$")
    fun checksSetContextResult(text: String) {
        waitForElementWithTextToBeClickable(text, ignoreCase = true)
    }

    @Then("^I click on each tabBarItem and confirm its position$")
    fun validateTabBarItems() {
        for (i in 1..10) {
            waitForElementWithTextToBeClickable("Tab$i", ignoreCase = true).click()
            waitForElementWithTextToBeClickable("Tab position " + (i - 1), ignoreCase = true)
        }
    }

    @Then("^I validate the buttons that trigger tabBars$")
    fun validateTabBarButtons() {
        waitForElementWithTextToBeClickable("Select tab 4 hardcoded").click()
        waitForElementWithTextToBeClickable("Tab position 3", ignoreCase = true)

        waitForElementWithTextToBeClickable("Select tab 9 expression").click()
        waitForElementWithTextToBeInvisible("Tab position 3", ignoreCase = true)
        waitForElementWithTextToBeClickable("Tab position 8", ignoreCase = true)
    }

}