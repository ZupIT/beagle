package br.com.zup.beagle.automatedTests.cucumber.steps

import androidx.test.rule.ActivityTestRule
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

const val TEXT_INPUT_SCREEN_BFF_URL = "http://10.0.2.2:8080/textinput"

class TextInputScreen {
    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before("@textInput")
    fun setup() {
        TestUtils.startActivity(activityTestRule, TEXT_INPUT_SCREEN_BFF_URL)
    }

    @Given("^the Beagle application did launch with the textInput on screen$")
    fun checkBaseScreen() {
        ScreenRobot()
            .checkViewContainsText("Beagle Text Input", true)
    }

    @Then("^I must check if the textInput value (.*) appears on the screen$")
    fun checkTextInput(string: String) {
        ScreenRobot()
            .checkViewContainsText(string, true)
    }

    @Then("^I must check if the textInput placeholder (.*) appears on the screen$")
    fun checkTextInputPlaceholder(string: String) {
        ScreenRobot()
            .checkViewContainsHint(string, true)
    }

    @When("^the placeholder (.*) of the disabled field is on the screen$")
    fun checkIfTextInputIsDisabled(string: String) {
        ScreenRobot()
            .checkViewContainsHint(string)
    }

    @Then("^verify if the field with the placeholder (.*) is disabled$")
    fun checkDisabledField(string: String) {
        ScreenRobot()
            .disabledFieldHint(string)
    }

    @When("^the value (.*) of the readOnly field is on the screen$")
    fun checkIfTextInputIsReadOnly(string: String) {
        ScreenRobot()
            .checkViewContainsText(string)
    }

    @Then("^verify if the field with the value (.*) is read only$")
    fun checkReadOnlyField(string: String) {
        ScreenRobot()
            .disabledFieldText(string)
    }

    @When("^I click in the textInput with the placeholder (.*)$")
    fun checkTextInputInSecondPlan(string: String) {
        ScreenRobot()
            .checkViewContainsHint(string)
            .clickOnInputWithHint(string)
    }

    @Then("^verify if the text (.*) is in the second plan$")
    fun checkKeyboardFocus(string: String) {
        ScreenRobot()
            .hintInSecondPlan(string)
    }

    @Then("^validate textInput component of type number with text (.*)$")
    fun validateTextsInTextInputNumberType(string: String) {
        ScreenRobot()
            .checkInputType(string)
    }

    @When("^I click to textInput (.*) then change to (.*) and to (.*)$")
    fun textInoutWithActionOfOnFocusAndOnChange(string: String, string2: String, string3: String) {
        ScreenRobot()
            .scrollToWithHint(string)
            .checkViewContainsHint(string)
            .clickOnInputWithHint(string)
            .checkViewContainsText(string2)
            .typeText(string, "a")
            .checkViewContainsText(string3)
    }

    @Then("^the text (.*) should be appear$")
    fun textInoutWithActionOfOnBlur(string: String) {
        ScreenRobot()
            .checkViewContainsHint("is textInput type number")
            .clickOnInputWithHint("is textInput type number")
            .checkViewContainsText(string)
    }

    @Then("^the text (.*) should be appear in the correctly order$")
    fun textInoutWithActionOfOnBlurCorrectlyOrder(string: String) {
        ScreenRobot()
            .checkViewContainsHint("is textInput type number")
            .clickOnInputWithHint("is textInput type number")
            .checkViewContainsText(string)
            .checkViewDoesNotContainsText("DidOnFocus")
            .checkViewDoesNotContainsText("DidOnFocusDidOnChange")
    }

    @Then("^The hidden input fields (.*) should not be visible$")
    fun checkInputTextIsHidden(string: String) {
        ScreenRobot()
            .scrollTo("There are two hidden input fields above")
            .checkViewIsNotDisplayed(string)
    }

    @After("@textInput")
    fun tearDown() {
        ActivityFinisher.finishOpenActivities()
    }

}

