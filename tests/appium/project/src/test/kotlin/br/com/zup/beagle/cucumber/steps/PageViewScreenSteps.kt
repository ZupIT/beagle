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

class PageViewScreenSteps : AbstractStep() {

    private val pageViewScreenHeader = "Beagle PageView"
    private val page1Text = "Page 1"
    private val page2Text = "Page 2"
    private val page3Text = "Page 3"

    override var bffRelativeUrlPath = "/pageview"

    @Before("@pageview")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the pageview screen$")
    fun checkTabViewScreen() {
        waitForElementWithTextToBeClickable(pageViewScreenHeader, likeSearch = false, ignoreCase = false)
    }

    @Then("^my pageview components should render their respective pages attributes correctly$")
    fun checkTabViewRendersTabs() {
        waitForElementWithTextToBeClickable(page1Text, likeSearch = false, ignoreCase = false)
        swipeLeft()
        waitForElementWithTextToBeClickable(page2Text, likeSearch = false, ignoreCase = false)
        swipeLeft()
        waitForElementWithTextToBeClickable(page3Text, likeSearch = false, ignoreCase = false)
    }

}