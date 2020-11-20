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
import br.com.zup.beagle.automatedTests.R
import br.com.zup.beagle.automatedTests.activity.MainActivity
import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import br.com.zup.beagle.automatedTests.utils.ActivityFinisher
import br.com.zup.beagle.automatedTests.utils.TestUtils
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.junit.Rule

const val TAB_BAR_SCREEN_BFF_URL = "http://10.0.2.2:8080/tabbar"
const val TAB_BAR_SCREEN_HEADER = "TabBar Screen"

class TabBarSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@tabBar")
    fun setup() {
        TestUtils.startActivity(activityTestRule, TAB_BAR_SCREEN_BFF_URL)
    }

    @After("@tabBar")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the tabBar screen$")
    fun checkTabBarScreen() {
        ScreenRobot()
            .checkViewContainsText(TAB_BAR_SCREEN_HEADER, true)
    }

    @When("I click in a tab with text (.*)")
    fun clickOnTab(text:String){
        ScreenRobot()
            .clickOnText(text)
    }

    @Then("^a tabBarItem with text (.*) should exist$")
    fun checksTabItemsExists(text: String){
        ScreenRobot()
            .checkViewContainsText(text)
    }

    @Then("^the tab position should have its text changed to (.*)$")
    fun checksSetContextResult(text: String){
        ScreenRobot()
            .checkViewContainsText(text)
    }

    @Then("^I click on each tabBarItem and confirm its position$")
    fun clickOnText() {
        ScreenRobot()
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
    }

    @Then("^the tab with text (.*) must be on screen$")
    fun checkTabBarRendersTabs(text:String) {
        ScreenRobot()
            .checkViewContainsText(text)
    }

    @Then("^check tab with text (.*) and beagle icon are on screen$")
    fun checkTitleAndIconOnTab(title:String){
        ScreenRobot()
            .checkTabContainsTextAndIcon(title, R.drawable.beagle)
    }

    @Then("^check tab with beagle icon is on screen$")
    fun checkTabWithIconOnly(){
        ScreenRobot()
            .checkTabContainsTextAndIcon(icon = R.drawable.beagle)
    }

    @Then("^the tab with text (.*) and beagle icon will change icon to delete icon$")
    fun checkTabWithIconChanged(title:String){
        ScreenRobot()
            .checkTabContainsTextAndIcon(title = title, icon = android.R.drawable.ic_delete)
    }
}