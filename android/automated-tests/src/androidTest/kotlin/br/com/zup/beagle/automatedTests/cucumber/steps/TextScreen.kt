package br.com.zup.beagle.automatedTests.cucumber.steps

import androidx.test.rule.ActivityTestRule
import br.com.zup.beagle.automatedTests.R
import br.com.zup.beagle.automatedTests.activity.MainActivity
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_DEFAULT
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_SCREEN_HEADER
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_VIA_EXPRESSION
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_WITH_ALIGNMENT_CENTER
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_WITH_ALIGNMENT_CENTER_VIA_EXPRESSION
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_WITH_ALIGNMENT_LEFT
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_WITH_ALIGNMENT_LEFT_VIA_EXPRESSION
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_WITH_ALIGNMENT_RIGHT
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_WITH_ALIGNMENT_RIGHT_VIA_EXPRESSION
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_WITH_TEXT_COLOR
import br.com.zup.beagle.automatedTests.cucumber.elements.TEXT_WITH_TEXT_COLOR_VIA_EXPRESSION
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

    @Given("^that I'm on the text screen$")
    fun checkTextScreen() {
        ScreenRobot()
            .checkViewContainsText(TEXT_SCREEN_HEADER, true)
    }

    @Then("my text component must render its respective text attribute correctly$")
    fun renderTextAttributeCorrectly() {
        ScreenRobot()
            .checkViewContainsText(TEXT_DEFAULT)
            .sleep(2)
    }

    @Then("my text component should render their respective text via expression correctly$")
    fun renderTextExpressionCorrectly() {
        ScreenRobot()
            .checkViewContainsText(TEXT_VIA_EXPRESSION)
            .sleep(2)
    }

    @Then("my text component should render their respective text with textColor correctly$")
    fun renderTextColorCorrectly() {
        ScreenRobot()
            .checkViewTextColor(TEXT_WITH_TEXT_COLOR, R.color.colorLightGray)
            .sleep(2)
    }

    @Then("my text component should render their respective text with textColor via expression correctly$")
    fun renderTextExpressionColorCorrectly() {
        ScreenRobot()
            .checkViewTextColor(TEXT_WITH_TEXT_COLOR_VIA_EXPRESSION, R.color.colorLightGray)
            .sleep(2)
    }

    @Then("my text component should render their respective text with textAlignment LEFT correctly$")
    fun renderTextAlignmentLeftCorrectly() {
        ScreenRobot()
            .checkViewTextAlignment(TEXT_WITH_ALIGNMENT_LEFT, TextAlignment.LEFT)
            .sleep(2)
    }

    @Then("my text component should render their respective text with textAlignment CENTER correctly$")
    fun renderTextAlignmentCenterCorrectly() {
        ScreenRobot()
            .checkViewTextAlignment(TEXT_WITH_ALIGNMENT_CENTER, TextAlignment.CENTER)
            .sleep(2)
    }

    @Then("my text component should render their respective text with textAlignment RIGHT correctly$")
    fun renderTextAlignmentRightCorrectly() {
        ScreenRobot()
            .checkViewTextAlignment(TEXT_WITH_ALIGNMENT_RIGHT, TextAlignment.RIGHT)
            .sleep(2)
    }

    @Then("my text component should render their respective text with textAlignment LEFT via expression correctly$")
    fun renderTextAlignmentLeftViaExpressionCorrectly() {
        ScreenRobot()
            .checkViewTextAlignment(TEXT_WITH_ALIGNMENT_LEFT_VIA_EXPRESSION, TextAlignment.LEFT)
            .sleep(2)
    }

    @Then("my text component should render their respective text with textAlignment CENTER via expression correctly$")
    fun renderTextAlignmentCenterViaExpressionCorrectly() {
        ScreenRobot()
            .checkViewTextAlignment(TEXT_WITH_ALIGNMENT_CENTER_VIA_EXPRESSION, TextAlignment.CENTER)
            .sleep(2)
    }

    @Then("my text component should render their respective text with textAlignment RIGHT via expression correctly$")
    fun renderTextAlignmentRightViaExpressionCorrectly() {
        ScreenRobot()
            .checkViewTextAlignment(TEXT_WITH_ALIGNMENT_RIGHT_VIA_EXPRESSION, TextAlignment.RIGHT)
            .sleep(2)
    }

    @After("@text")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }
}
