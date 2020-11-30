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

import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.automatedTests.activity.MainActivity
import br.com.zup.beagle.automatedTests.cucumber.elements.*
import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import br.com.zup.beagle.automatedTests.utils.ActivityFinisher
import br.com.zup.beagle.automatedTests.utils.TestUtils
import br.com.zup.beagle.automatedTests.utils.transform.SingleQuoteTransform
import cucumber.api.Transform
import cucumber.api.Transformer
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.*
import org.junit.Rule

val LIST_VIEW_SCREEN_BFF_URL = "http://10.0.2.2:8080/listview"

class ListViewScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@listview")
    fun setup() {
        TestUtils.startActivity(activityTestRule, LIST_VIEW_SCREEN_BFF_URL)
    }

    @After("@listview")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the listView screen$")
    fun checkListViewScreen() {
        ScreenRobot()
            .checkViewContainsText(LISTVIEW_SCREEN_HEADER, true)
    }

    @When("^I click the button (.*)$")
    fun clickOnButton(buttonText: String) {
        ScreenRobot()
            .clickOnText(buttonText)
    }

    @When("^the read status of the list of characters is (.*)$")
    fun checkCharactersListReadStatus(expectedText: String) {
        ScreenRobot()
            .checkViewContainsText(expectedText, true)
    }

    @When("^I scroll the list of characters to position (.*)$")
    fun scrollCharactersListToPosition(position: Int) {
        ScreenRobot()
            .scrollListToPosition(CHARACTERS_LIST_VIEW_ID.toAndroidId(), position)
    }

    @Then("^categories listView should have a template with (.*) at (.*)$")
    fun checkCategoriesListContainsViewWithId(expectedViewId: String, position: Int) {
        ScreenRobot()
            .scrollListToPosition(
                CATEGORIES_LIST_VIEW_ID.toAndroidId(),
                position
            )
            .checkListViewItemContainsViewWithId(
                CATEGORIES_LIST_VIEW_ID.toAndroidId(),
                position,
                expectedViewId.toAndroidId()
            )
    }

    @Then("^should render the list of characters with exactly (.*) items in the horizontal plane$")
    fun checkCharactersListItemsCount(expectedItemCount: Int) {
        ScreenRobot()
            .checkListViewItemCount(CHARACTERS_LIST_VIEW_ID.toAndroidId(), expectedItemCount)
    }

    @Then("^the list of characters should be scrollable only horizontally$")
    fun checkCharactersListOrientation() {
        ScreenRobot()
            .checkListViewOrientation(CHARACTERS_LIST_VIEW_ID.toAndroidId(), RecyclerView.HORIZONTAL)
    }

    @Then("^the page number should be (.*)$")
    fun checkListViewScreenTexts(pageNumberText: String) {
        ScreenRobot()
            .checkViewContainsText(pageNumberText)
    }

    @Then("^should render character (.*) in (.*) in (.*) at (.*) in the list of characters$")
    fun checkListViewItemRenderText(name: String, book: String, @Transform(SingleQuoteTransform::class) collection: String, position: Int) {
        val listId = CHARACTERS_LIST_VIEW_ID.toAndroidId()
        ScreenRobot()
            .scrollListToPosition(listId, position)
            .checkListViewItemContainsText(listId, position, name)
            .checkListViewItemContainsText(listId, position, book)
            .checkListViewItemContainsText(listId, position, collection)
    }

    @Then("^categories listView at(.*) a books list with (.*) with exactly (.*) items in the horizontal plane$")
    fun checkCategoryBookListItemsCount(position: Int, booksListId: String, expectedNumberOfBooks: Int) {
        ScreenRobot()
            .scrollListToPosition(
                CATEGORIES_LIST_VIEW_ID.toAndroidId(),
                position
            )
            .checkListViewItemCount(
                booksListId.toAndroidId(),
                expectedNumberOfBooks
            )
    }

    @Then("^books list with id (.*) should be scrollable only horizontally$")
    fun checkCategoryBooksListOrientation(booksListId: String) {
        ScreenRobot()
            .checkListViewOrientation(booksListId.toAndroidId(), RecyclerView.HORIZONTAL)
    }


    @Then("^categories listView at (.*) should show title (.*)$")
    fun checkCategoryListItemsTitle(position: Int, title: String) {
        ScreenRobot()
            .scrollListToPosition(
                CATEGORIES_LIST_VIEW_ID.toAndroidId(),
                position
            )
            .checkListViewItemContainsText(
                CATEGORIES_LIST_VIEW_ID.toAndroidId(),
                position,
                title
            )
    }
}
