package br.com.zup.beagle.automatedTests.cucumber.steps

import androidx.test.rule.ActivityTestRule
import br.com.zup.beagle.automatedTests.activity.MainActivity
import br.com.zup.beagle.automatedTests.cucumber.elements.*
import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import br.com.zup.beagle.automatedTests.utils.ActivityFinisher
import br.com.zup.beagle.automatedTests.utils.TestUtils
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.junit.Rule

val BUTTON_SCREEN_BFF_URL = "http://10.0.2.2:8080/button"

class ButtonScreen {
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@button")
    fun setup() {
        TestUtils.startActivity(activityTestRule, BUTTON_SCREEN_BFF_URL)
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
