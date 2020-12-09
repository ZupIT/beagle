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

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.PositionAssertions.isCompletelyAbove
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.automatedTests.activity.MainActivity
import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import br.com.zup.beagle.automatedTests.utils.ActivityFinisher
import br.com.zup.beagle.automatedTests.utils.TestUtils
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import org.hamcrest.Matchers.not
import org.junit.Rule


private const val ADD_CHILDREN_SCREEN_BFF_URL = "http://10.0.2.2:8080/add-children"

class AddChildrenScreenSteps {

    private val ADD_CHILDREN_HEADER = "Add Children"
    private val TEXT_FIXED = "I'm the single text on screen"
    private val TEXT_ADDED = "New text added"
    private val CONTAINER_ID = "containerId"

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@addChildren")
    fun setup() {
        TestUtils.startActivity(activityTestRule, ADD_CHILDREN_SCREEN_BFF_URL)
    }

    @After("@addChildren")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the addChildren Screen$")
    fun checkImageScreen() {
        ScreenRobot()
            .checkViewContainsText(ADD_CHILDREN_HEADER, true)
    }

    @Then("^A Text need to be added after the already exist one$")
    fun checkTextAddedAfterTheExistedOrder() {
        waitForBothTexts()
        onView(withText(TEXT_FIXED)).check(isCompletelyAbove(withText(TEXT_ADDED)))
    }

    @Then("^A Text need to be added before the already exist one$")
    fun checkTextAddedBeforeTheExistedOrder() {
        waitForBothTexts()
        onView(withText(TEXT_ADDED)).check(isCompletelyAbove(withText(TEXT_FIXED)))
    }

    @Then("^A Text need to replace the already exist one$")
    fun checkTextReplaceTheExistedOne() {
        waitForTextAdded()
        onView((withText(TEXT_ADDED))).check(matches(isDisplayed()))
        onView(withId(CONTAINER_ID.toAndroidId())).check(matches(not(withText(TEXT_FIXED))))
    }

    @Then("^Nothing should happen$")
    fun checkTextAddedPositionIsNull() {
        waitForTextFixed()
        onView((withText(TEXT_FIXED))).check(matches(isDisplayed()))
        onView(withId(CONTAINER_ID.toAndroidId())).check(matches(not(withText(TEXT_ADDED))))
    }

    private fun waitForBothTexts() {
        waitForTextFixed()
        waitForTextAdded()
    }

    private fun waitForTextFixed(){
        ScreenRobot().checkViewContainsText(TEXT_FIXED, true)
    }

    private fun waitForTextAdded(){
        ScreenRobot().checkViewContainsText(TEXT_ADDED, true)
    }
}