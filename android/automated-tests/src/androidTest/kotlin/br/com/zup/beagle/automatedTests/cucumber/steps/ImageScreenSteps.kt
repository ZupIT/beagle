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

package br.com.zup.beagle.automatedTests.cucumber.steps

import androidx.test.rule.ActivityTestRule
import br.com.zup.beagle.automatedTests.activity.MainActivity
import br.com.zup.beagle.automatedTests.cucumber.elements.*
import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import br.com.zup.beagle.automatedTests.utils.ActivityFinisher
import br.com.zup.beagle.automatedTests.utils.TestUtils
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.*
import org.junit.Rule


class ImageScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@image")
    fun setup() {
        TestUtils.startActivity(activityTestRule, Constants.imageScreenBffUrl)
    }

    @After("@image")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the image screen$")
    fun checkImageScreen() {
        ScreenRobot()
            .checkViewContainsText(IMAGE_SCREEN_HEADER, true)
    }

    @Then("^image screen should render all text attributes correctly$")
    fun checkImageScreenTexts() {
        ScreenRobot()
            .checkViewContainsText(IMAGE_TEXT_1)
            .checkViewContainsText(IMAGE_TEXT_2)
            .scrollViewDown()
//            .checkViewContainsText(IMAGE_TEXT_3)
//            .checkViewContainsText(IMAGE_TEXT_4)
//            .checkViewContainsText(IMAGE_TEXT_5)

    }
}