package br.com.zup.beagle.cucumber.steps

import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class PullToRefreshScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/pull-to-refresh"

    @Before("@pullToRefresh")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the pull to refresh screen$")
    fun checkBaseScreen() {
        waitForElementWithTextToBeClickable("Beagle PullToRefresh screen", likeSearch = false, ignoreCase = false)
    }

    @When("^I swipe (.*) from the center of the screen$")
    fun scrollDownOnElement(swipeDirection: String) {
        if ("down".equals(swipeDirection, ignoreCase = true)) {
            // swipes inside the element since it is in the center of the screen
            swipeDown()
        } else {
            throw Exception("Wrong swipe direction")
        }
    }

    @Then("^the loading element should not be showing$")
    fun checkLoadingNotShowing() {
        waitForAllImagesToBeInvisible()
    }
}