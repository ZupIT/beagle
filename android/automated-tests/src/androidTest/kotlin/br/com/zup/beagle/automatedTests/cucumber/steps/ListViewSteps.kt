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

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.automatedTests.activity.MainActivity
import br.com.zup.beagle.automatedTests.cucumber.elements.LISTVIEW_SCREEN_HEADER
import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import br.com.zup.beagle.automatedTests.utils.transform.SingleQuoteTransform
import cucumber.api.Transform
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.junit.Rule

const val LIST_VIEW_SCREEN_BFF_URL = "/listview"

class ListViewScreenSteps {

    val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        .putExtra(MainActivity.BFF_URL_KEY, LIST_VIEW_SCREEN_BFF_URL)

    lateinit var scenario: ActivityScenario<MainActivity>

    @get:Rule
    val activityTestRule = ActivityScenarioRule<MainActivity>(intent)


    @Before("@listView")
    fun setup() {
        scenario = launch(intent)
    }

    @After("@listView")
    fun tearDown() {
        scenario.close()
    }

    @Given("^that I'm on the listView screen$")
    fun checkListViewScreen() {
        ScreenRobot()
            .checkViewContainsText(
                LISTVIEW_SCREEN_HEADER,
                true,
            )
    }

    @Then("^listView with id (.*) should have exactly (.*) items$")
    fun checkListViewItemsCount(listViewId: String, expectedItemCount: Int) {
        ScreenRobot()
            .checkListViewItemCount(
                listViewId.toAndroidId(),
                expectedItemCount,
            )
    }

    @Then("^listView with id (.*) should be in vertical orientation$")
    fun checkListViewIsVertical(listViewId: String) {
        ScreenRobot()
            .checkListViewOrientation(
                listViewId.toAndroidId(),
                RecyclerView.VERTICAL,
            )
    }

    @Then("^listView with id (.*) should be in horizontal orientation$")
    fun checkListViewIsHorizontal(listViewId: String) {
        ScreenRobot()
            .checkListViewOrientation(
                listViewId.toAndroidId(),
                RecyclerView.HORIZONTAL,
            )
    }

    @When("^I scroll listView with id (.*) to position (.*)$")
    fun scrollListViewToPosition(listViewId: String, position: Int) {
        ScreenRobot()
            .scrollListToPosition(
                listViewId.toAndroidId(),
                position,
            )
    }

    @When("^I scroll listView with id (.*) to (.*) percent$")
    fun scrollListViewByPercent(listViewId: String, scrollPercent: Int) {
        ScreenRobot()
            .scrollListByPercent(
                listViewId.toAndroidId(),
                scrollPercent,
            )
    }

    @Then("^screen should show text: (.*)$")
    fun checkScreenDisplaysText(expectedText: String) {
        ScreenRobot()
            .checkViewContainsText(
                expectedText,
                true,
            )
    }

    @Then("^listView with id (.*) at position (.*) should show text: (.*)$")
    fun checkListViewItemContainsText(
        listViewId: String,
        listViewPosition: Int,
        @Transform(SingleQuoteTransform::class) expectedText: String,
    ) {
        ScreenRobot()
            .checkListViewItemContainsText(
                listViewId.toAndroidId(),
                listViewPosition,
                expectedText,
            )
    }

    @Then("^listView with id (.*) at position (.*) should have a view with id (.*)$")
    fun checkListViewItemContainsViewWithId(listViewId: String, listViewPosition: Int, expectedViewId: String) {
        ScreenRobot()
            .checkListViewItemContainsViewWithId(
                listViewId.toAndroidId(),
                listViewPosition,
                expectedViewId.toAndroidId(),
            )
    }

    @Then("^I click on view with id (.*) at position (.*) of listView with id (.*)$")
    fun clickOnTextInsideListViewItem(viewId: String, position: Int, listViewId: String) {
        ScreenRobot()
            .clickOnTextInsideListViewItem(
                listViewId.toAndroidId(),
                position,
                viewId.toAndroidId(),
            )
    }
}
