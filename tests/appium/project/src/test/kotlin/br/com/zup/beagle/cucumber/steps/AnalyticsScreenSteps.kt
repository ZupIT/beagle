package br.com.zup.beagle.cucumber.steps

import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class AnalyticsScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/analytics2.0"

    @Before("@analytics2.0")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^the Beagle application did launch with the Analytics screen url$")
    fun checkBaseScreen() {
        waitForElementWithTextToBeClickable("Analytics 2.0", false, false)
    }

    @When("^I press the button with title (.*)$")
    fun clickOnButton(string: String) {
        waitForElementWithTextToBeClickable(string, false, false).click()
    }

    @Then("^an alert dialog should appear on the screen$")
    fun checkAlertDialog() {
        waitForElementWithTextToBeClickable("AlertMessage", false, false)
    }

    @Then("^a confirm dialog should appear on the screen$")
    fun checkConfirmDialog() {
        waitForElementWithTextToBeClickable("Confirm Message", false, false)
    }

    @Then("^no analytics record should be created$")
    fun checkNoAnalyticsGenerated(){

        waitForElementWithTextToBeInvisible("type", false, false)
    }

    @Then("^an analytics record should be created with (.*)$")
    fun checkAnalyticsGenerated(string : String){
        waitForElementWithTextToBeClickable("Analytics 2.0 native", false, false)
        waitForElementWithTextToBeClickable(string, false, false)
    }

}