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

class ImageScreenSteps : AbstractStep() {

    private val textContainedInImage = "without size"

    override var bffRelativeUrlPath = "/image"

    @Before("@image")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the image screen$")
    fun checkImageScreen() {
        waitForElementWithTextToBeClickable(textContainedInImage, likeSearch = true, ignoreCase = false)
    }

    @Then("^take a screenshot from ImageScreenBuilder and assert it is identical to the (.*) image$")
    fun checkImageAttributes(imageDbToCompare: String) {
        Assert.assertTrue(compareCurrentScreenWithDatabase(imageDbToCompare))
    }
}
