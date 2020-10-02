package com.example.automatedTests.cucumber.steps

import androidx.test.rule.ActivityTestRule
import com.example.automatedTests.activity.MainActivity
import com.example.automatedTests.cucumber.elements.*
import com.example.automatedTests.cucumber.robots.ScreenRobot
import com.example.automatedTests.utils.ActivityFinisher
import com.example.automatedTests.utils.TestUtils
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.junit.Rule

class ButtonScreen {
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@button")
    fun setup() {
        TestUtils.startActivity(activityTestRule, Constants.buttonScreenBffUrl)
    }

    @Given("^that I'm on the button screen$")
    fun checkButtonScreen() {
        ScreenRobot()
            .checkViewContainsText(BUTTON_SCREEN_HEADER, true)
    }

    @When("I click on a component with a valid style attribute configured$")
    fun clickOnButtonWithStyle() {
        ScreenRobot()
            .clickOnText(BUTTON_WITH_STYLE_TEXT)
            .sleep(2)
    }

    @Then("all my button components should render their respective text attributes correctly$")
    fun renderTextAttributeCorrectly() {
        ScreenRobot()
            .checkViewContainsText(BUTTON_DEFAULT_TEXT)
            .checkViewContainsText(BUTTON_WITH_STYLE_TEXT)
            .checkViewContainsText(BUTTON_WITH_APPEARANCE_TEXT)
            .sleep(2)
    }

    @Then("component should render the action attribute correctly$")
    fun renderActionAttributeCorrectly() {
        ScreenRobot()
            .checkViewContainsText(ACTION_CLICK_HEADER)
            .checkViewContainsText(ACTION_CLICK_TEXT)
            .sleep(2)
    }

    @After("@button")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }
}
