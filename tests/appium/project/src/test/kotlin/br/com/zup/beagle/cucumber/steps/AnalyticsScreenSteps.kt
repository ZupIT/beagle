package br.com.zup.beagle.cucumber.steps

import br.com.zup.beagle.setup.SuiteSetup
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then

class AnalyticsScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = ""
    private val positionRegex = Regex("""\{\s*"[xy]": \d+(\.\d+)?\s*,\s*"[xy]": \d+(\.\d+)?\s*\}""")

    private val recordHashMap = hashMapOf(
        "Analytics 02" to hashMapOf(
            "type" to "action",
            "event" to "onPress",
            "type" to "beagle:button",
            "id" to "_beagle_5",
            "position" to positionRegex,
            "beagleAction" to "beagle:confirm",
            "title" to "Confirm Title",
            "message" to "Confirm Message",
            "screen" to "${SuiteSetup.getBaseUrl()}/analytics2",
            "platform" to platformCheck()
        ),
        "Analytics 03" to hashMapOf(
            "type" to "action",
            "event" to "onPress",
            "type" to "beagle:button",
            "id" to "_beagle_6",
            "position" to positionRegex,
            "beagleAction" to "beagle:alert",
            "message" to "AlertMessage",
            "screen" to "${SuiteSetup.getBaseUrl()}/analytics2",
            "platform" to platformCheck()
        ),
        "Analytics 05" to hashMapOf(
            "type" to "screen",
            "screen" to "/analytics2-navigate",
            "platform" to platformCheck()
        )
    )

    @Before("@analytics2.0")
    fun setup() {
        SuiteSetup.restartApp()
    }

    @Given("AppiumApp is properly configured with an AnalyticsProvider and with a native screen with id \"screen-analytics-link\"")
    fun properlyConfigured() {
        // couldn't think of a way to easily assert this behavior
    }

    @Given("AppiumApp did navigate to remote screen with url {string}")
    fun checkBaseScreen(url: String) {
        bffRelativeUrlPath = url
        loadBffScreen()
        waitForElementWithTextToBeClickable("Analytics 2.0")
    }

    @Then("^an alert dialog should appear on the screen$")
    fun checkAlertDialog() {
        waitForElementWithTextToBeClickable("AlertMessage")
    }

    @Then("^a confirm dialog should appear on the screen$")
    fun checkConfirmDialog() {
        waitForElementWithTextToBeClickable("Confirm Message")
    }

    @Then("^no analytics record should be created$")
    fun checkNoAnalyticsGenerated() {
        waitForElementWithTextToBeClickable("Analytics 2.0 native")
        waitForElementWithTextToBeInvisible("type", likeSearch = true, ignoreCase = false)
    }

    @Then("^an analytics record should be created for (.*)$")
    fun checkAnalyticsGenerated(string: String) {
        waitForElementWithTextToBeClickable("Analytics 2.0 native")
        val text = waitForElementWithTextToBeClickable("platform", likeSearch = true, ignoreCase = false).text

        val analytics = recordHashMap[string]
        analytics?.forEach {
            val value = if (it.value is Regex)
                it.value.toString()
            else "\"${it.value}\""

            val regex = Regex("\"${it.key}\": $value")
            if (!text.contains(regex))
                throw Exception("Record doesn't match $it")
        }
    }

    private fun platformCheck(): String {
        return if (SuiteSetup.isAndroid()) "android" else "ios"
    }

}