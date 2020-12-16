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

private const val PAGEVIEW_SCREEN_HEADER = "Beagle PageView"
private const val PAGE_1_TEXT = "Page 1"
private const val PAGE_2_TEXT = "Page 2"
private const val PAGE_3_TEXT = "Page 3"

class PageViewScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/pageview"

    @Before("@pageview")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^that I'm on the pageview screen$")
    fun checkTabViewScreen() {
        // ScreenRobot().checkViewContainsText(PAGEVIEW_SCREEN_HEADER, true)
        waitForElementWithTextToBeClickable(PAGEVIEW_SCREEN_HEADER, false, false)
    }

    @Then("^my pageview components should render their respective pages attributes correctly$")
    fun checkTabViewRendersTabs() {
        /*
        ScreenRobot()
            .checkViewContainsText(PAGE_1_TEXT)
            .swipeLeftOnView()
            .checkViewContainsText(PAGE_2_TEXT)
            .swipeLeftOnView()
            .checkViewContainsText(PAGE_3_TEXT)
            .swipeLeftOnView()
            .sleep(2)
            .swipeRightOnView()
            .swipeRightOnView()
         */
        waitForElementWithTextToBeClickable(PAGE_1_TEXT, false, false)
        swipeLeft()
        waitForElementWithTextToBeClickable(PAGE_2_TEXT, false, false)
        swipeLeft()
        waitForElementWithTextToBeClickable(PAGE_3_TEXT, false, false)

    }

}