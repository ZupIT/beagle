package br.com.zup.beagle.cucumber.steps

import br.com.zup.beagle.setup.SuiteSetup
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.openqa.selenium.json.Json

class AnalyticsScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = ""
    private val positionRegex = Regex("position=\\{x=([0-9][0-9]*)(.?)([0-9]?),\\s*y=([0-9][0-9]*)(.?)([0-9]?)\\}")

    private val recordHashMap = hashMapOf<String, List<Regex>>(
        "Analytics 02" to listOf(
            Regex("\"type\" : \"action\"")
            // Regex("event=onPress"),
            // Regex("type=beagle:button"),
            // Regex("id=_beagle_5"),
            // positionRegex,
            // Regex("beagleAction=beagle:confirm"),
            // Regex("title=Confirm Title"),
            // Regex("message=Confirm Message"),
            // Regex("screen=/analytics2"),
            // platformCheck()
        ),
        "Analytics 03" to listOf(
            Regex("type:action"),
            Regex("event=onPress"),
            Regex("type=beagle:button"),
            Regex("id=_beagle_6"),
            positionRegex,
            Regex("beagleAction=beagle:alert"),
            Regex("message=AlertMessage"),
            Regex("screen=/analytics2"),
            platformCheck()
        ),
        "Analytics 05" to listOf(
            Regex("type:screen"),
            Regex("screen=/analytics2-navigate"),
            platformCheck()
        )
    )

    @Given("AppiumApp is properly configured with an AnalyticsProvider and with a native screen with id \"screen-analytics-link\"")
    fun properlyConfigured() {
        // couldn't think of a way to easily assert this behavior
    }

    @Given("AppiumApp did navigate to remote screen with url {string}")
    fun checkBaseScreen(url: String) {
        bffRelativeUrlPath = url
        loadBffScreenFromMainScreen()
        waitForElementWithTextToBeClickable("Analytics 2.0", false, false)
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
    fun checkNoAnalyticsGenerated() {
        waitForElementWithTextToBeClickable("Analytics 2.0 native", false, false)
        waitForElementWithTextToBeInvisible("type", true, false)
    }

    @Then("^an analytics record should be created for (.*)$")
    fun checkAnalyticsGenerated(string: String) {
        waitForElementWithTextToBeClickable("Analytics 2.0 native", false, false)
        val text = waitForElementWithTextToBeClickable("type:", true, false).text
        
        val analytics = recordHashMap[string]
        analytics?.forEach {
            val regex = "(.*)" + it + "(.*)"
            if (!text.matches(regex.toRegex()))
                throw Exception("Record doesn't match " + it)
        }
    }

    private fun platformCheck(): Regex {
        if (SuiteSetup.isAndroid()) {
            return Regex("platform:android")
        }
        return Regex("platform:ios")
    }

}