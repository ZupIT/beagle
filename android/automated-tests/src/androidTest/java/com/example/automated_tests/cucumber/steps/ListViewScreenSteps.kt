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

package com.example.automated_tests.cucumber.steps

import androidx.test.rule.ActivityTestRule
import com.example.automated_tests.MainActivity
import com.example.automated_tests.cucumber.elements.*
import com.example.automated_tests.cucumber.robots.ScreenRobot
import com.example.automated_tests.utils.ActivityFinisher
import com.example.automated_tests.utils.TestUtils
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.*
import org.junit.Rule


class ListViewScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@listview")
    fun setup() {
        TestUtils.startActivity(activityTestRule, "http://10.0.2.2:8080/listview")
    }

    @After("@listview")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the listview screen$")
    fun checkListViewScreen() {
        ScreenRobot()
            .checkViewContainsText(MAIN_HEADER)
            .checkViewContainsText(LISTVIEW_SCREEN_HEADER)
            .sleep(2)
    }

    @When("^I have a vertical list configured$")
    fun checkVerticalListText() {
        ScreenRobot()
            .checkViewContainsText(STATIC_LISTVIEW_TEXT_1)
            .sleep(2)
    }

    @When("^I have a horizontal list configured$")
    fun checkHorizontalListText() {
        ScreenRobot()
            .checkViewContainsText(STATIC_LISTVIEW_TEXT_2)
            .sleep(2)
    }

    @Then("^listview screen should render all text attributes correctly$")
    fun checkListViewScreenTexts() {
        ScreenRobot()
            .checkViewContainsText(STATIC_LISTVIEW_TEXT_1)
            .checkViewContainsText(STATIC_LISTVIEW_TEXT_2)
            .checkViewContainsText(DYNAMIC_LISTVIEW_TEXT_1)

    }

    @Then("^listview screen should perform the scroll action vertically$")
    fun validateVerticalListScroll() {
        ScreenRobot()
            .scrollTo(DYNAMIC_LISTVIEW_TEXT_2)
            .sleep(2)
    }
//
//    @Then("^listview screen should perform the scroll action horizontally$")
//    fun validateHorizontalListScroll() {
//        ScreenRobot()
//            .scrollTo("Dynamic HORIZONTAL ListView")
//            .sleep(2)
//    }


}