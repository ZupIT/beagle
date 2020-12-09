package br.com.zup.beagle.automatedTests.cucumber.steps

import android.support.test.rule.ActivityTestRule
import br.com.zup.beagle.automatedTests.activity.MainActivity
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_SCREEN_HEADER
import br.com.zup.beagle.automatedTests.cucumber.robots.ScreenRobot
import br.com.zup.beagle.automatedTests.utils.ActivityFinisher
import br.com.zup.beagle.automatedTests.utils.TestUtils
import br.com.zup.beagle.widget.core.TextAlignment
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import org.junit.Rule

val TEXT_SCREEN_BFF_URL = "http://10.0.2.2:8080/text"

class TextScreen {
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@text")
    fun setup() {
        TestUtils.startActivity(activityTestRule, TEXT_SCREEN_BFF_URL)
    }

    @Given("^the Beagle application did launch with the texts on screen$")
    fun checkTextScreen() {
        ScreenRobot()
            .checkViewContainsText(TEXT_SCREEN_HEADER, true)
    }

    @Then("^my text component (.*) must be renderded on screen$")
    fun renderTextAttributeCorrectly(string:String) {
        ScreenRobot()
            .checkViewContainsText(string, true)
    }

    @Then("^my text component (.*) should render their respective color (.*) correctly$")
    fun renderTextColorCorrectly(string:String, string2: String) {

        ScreenRobot()
            .checkViewTextColor(string, string2)
    }

    @Then("^my text component (.*) should render itself with a textAlignment (.*)$")
    fun renderTextAlignmentLeftCorrectly(string1:String, string2:String) {

        val position = TextAlignment.valueOf(string2)
        ScreenRobot()
            .checkViewTextAlignment(text = string1, textAlignment = position, waitForText = true)
    }

    @After("@text")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }
}
