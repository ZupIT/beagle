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

const val SIMPLE_FORM_SCREEN_BFF_URL = "http://10.0.2.2:8080/simpleform"

class SimpleFormScreenSteps {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@simpleform")
    fun setup() {
        TestUtils.startActivity(activityTestRule, SIMPLE_FORM_SCREEN_BFF_URL)
    }

    @After("@simpleform")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

    @Given("^that I'm on the simple form screen$")
    fun checkTabViewScreen() {
        ScreenRobot()
            .checkViewContainsText(SIMPLE_FORM_SCREEN_HEADER, true)
    }

    @Then("^all my simple form components should render their respective text attributes correctly$")
    fun checkTabViewRendersTabs() {
        ScreenRobot()
            .checkViewContainsText(SIMPLE_FORM_TITLE)
            .checkViewContainsHint(STREET_FIELD)
            .checkViewContainsHint(ZIP_FIELD)
    }

    @And("^insert text (.*)$")
    fun insertText(text: String?) {
        ScreenRobot()
            .typeIntoTextField(0, 1, text)
    }

    @Then("^confirm popup should appear correctly$")
    fun confirmPopUp() {
        ScreenRobot()
            .checkViewContainsText(OK_BUTTON)
            .checkViewContainsText(CANCEL_BUTTON)
    }
}