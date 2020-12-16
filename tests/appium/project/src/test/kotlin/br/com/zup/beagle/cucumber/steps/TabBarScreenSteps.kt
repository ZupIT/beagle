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
import io.cucumber.java.en.When


private const val TAB_BAR_SCREEN_HEADER = "TabBar Screen"

class TabBarScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/tabbar"

    @Before("@tabBar")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^that I'm on the tabBar screen$")
    fun checkTabBarScreen() {
        // ScreenRobot().checkViewContainsText(TAB_BAR_SCREEN_HEADER, true)
        waitForElementWithTextToBeClickable(TAB_BAR_SCREEN_HEADER, false, true)
    }

    @When("I click on a tab with text (.*)$")
    fun clickOnTab(text: String) {
        // ScreenRobot().clickOnText(text)
        waitForElementWithTextToBeClickable(text, false, true).click()
    }

    @Then("^a tabBarItem with text (.*) should exist$")
    fun checksTabItemsExists(text: String) {
        // ScreenRobot().checkViewContainsText(text)
        waitForElementWithTextToBeClickable(text, false, true)
    }

    @Then("^the tab position should have its text changed to (.*)$")
    fun checksSetContextResult(text: String) {
        // ScreenRobot().checkViewContainsText(text)
        waitForElementWithTextToBeClickable(text, false, true)
    }

    @Then("^I click on each tabBarItem and confirm its position$")
    fun clickOnText() {
        /*ScreenRobot()
            .clickOnText("Tab1").checkViewContainsText("Tab position 0")
            .clickOnText("Tab2").checkViewContainsText("Tab position 1")
            .clickOnText("Tab3").checkViewContainsText("Tab position 2")
            .clickOnText("Tab4").checkViewContainsText("Tab position 3")
            .clickOnText("Tab5").checkViewContainsText("Tab position 4")
            .clickOnText("Tab6").checkViewContainsText("Tab position 5")
            .clickOnText("Tab7").checkViewContainsText("Tab position 6")
            .clickOnText("Tab8").checkViewContainsText("Tab position 7")
            .clickOnText("Tab9").checkViewContainsText("Tab position 8")
            .clickOnText("Tab10").checkViewContainsText("Tab position 9")
         */
        for (i in 1..10) {
            waitForElementWithTextToBeClickable("Tab$i", false, true).click()
            waitForElementWithTextToBeClickable("Tab position " + (i - 1), false, true)
        }
    }

    @Then("^the tab with text (.*) must be on screen$")
    fun checkTabBarRendersTabs(text: String) {
        // ScreenRobot().checkViewContainsText(text)
        waitForElementWithTextToBeClickable(text, false, true)
    }
}