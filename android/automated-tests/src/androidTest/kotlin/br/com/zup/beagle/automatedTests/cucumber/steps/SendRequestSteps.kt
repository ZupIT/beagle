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

class SendRequestSteps {
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@sendrequest")
    fun setup() {
        TestUtils.startActivity(activityTestRule, Constants.sendrequestUrlBff)
    }

    @Given("^that I'm on the screen with a button for call a sendRequest and to realize the http requests (.*)$")
    fun checkTitleScreen(string: String) {
        ScreenRobot()
            .checkViewContainsText(string, true)
    }

    @When("I click on sendRequestSuccess button (.*)$")
    fun clickOnButtonSendRequestSuccess(string: String) {
        ScreenRobot()
            .clickOnText(string)
            .sleep(2)
    }

    @Then("the component should return an alert with a message onSuccess (.*) and onFinished (.*)$")
    fun renderSendRequestCorrectly(string: String) {
        ScreenRobot()
            .checkViewContainsText(string, true)
            .sleep(2)
    }

    @When("I click on sendRequestError button (.*)$")
    fun clickOnButtonSendRequestError(string: String) {
        ScreenRobot()
            .clickOnText(string)
            .sleep(2)
    }

    @Then("the component should return an alert with a message onError (.*) and SendRequestError (.*)$")
    fun renderSendRequestIncorrectly(string: String) {
        ScreenRobot()
            .checkViewContainsText(string, true)
            .sleep(2)
    }

    @After("@sendrequest")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }
}
