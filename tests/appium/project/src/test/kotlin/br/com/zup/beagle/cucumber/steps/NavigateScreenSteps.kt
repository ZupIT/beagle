/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.cucumber.steps

import br.com.zup.beagle.setup.SuiteSetup
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then


class NavigateScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/navigate-actions"

    @Before("@navigation")
    fun setup() {
        loadBffScreen()
    }

    @Given("^the Beagle application did launch with the navigation screen url$")
    fun checkBaseScreen() {
        waitForElementWithTextToBeClickable("Navigation Screen", false, false)
    }

    @Then("^validate PushStack and PushView screen titles:$")
    fun checkPushTitles(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var button1Title = columns[0]!!
            var newScreenTitle = columns[1]!!

            safeClickOnElement(
                waitForElementWithTextToBeClickable(
                    button1Title,
                    likeSearch = false,
                    ignoreCase = false
                )
            )

            waitForElementWithTextToBeClickable(newScreenTitle, likeSearch = false, ignoreCase = true)

            // goes back to the main screen
            safeClickOnElement(waitForElementWithTextToBeClickable("PopView", likeSearch = false, ignoreCase = false))
            waitForElementWithTextToBeClickable(button1Title, likeSearch = false, ignoreCase = false)

        }

    }


    @Then("^validate PushStack and PushView actions:$")
    fun checkPushProperties(dataTable: DataTable) {
        val rows = dataTable.asLists()
        val actionScreenTitle = "Reset Screen"
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var button1Title = columns[0]!!
            var button2Title = columns[1]!!
            var iosAction = columns[2]!!
            var androidAction = columns[3]!!

            safeClickOnElement(
                waitForElementWithTextToBeClickable(
                    button1Title,
                    likeSearch = false,
                    ignoreCase = false
                )
            )
            safeClickOnElement(
                waitForElementWithTextToBeClickable(
                    button2Title,
                    likeSearch = false,
                    ignoreCase = false
                )
            )

            if (SuiteSetup.isIos()) {

                // iOS screen transition animation
                sleep(200)

                when (iosAction) {
                    "no action" -> {
                        waitForElementWithTextToBeClickable(button2Title, likeSearch = false, ignoreCase = false)

                        // goes back to the main screen
                        waitForElementWithTextToBeClickable("PopView", likeSearch = false, ignoreCase = false).click()
                        waitForElementWithTextToBeClickable(button1Title, likeSearch = false, ignoreCase = false)
                    }
                    "goes back to main screen" -> {
                        waitForElementWithTextToBeClickable(button1Title, likeSearch = false, ignoreCase = false)
                    }
                    "opens a closable screen" -> {
                        waitForElementWithTextToBeClickable(actionScreenTitle, likeSearch = false, ignoreCase = false)
                        swipeDown() // closes screen
                        waitForElementWithTextToBeInvisible(actionScreenTitle, likeSearch = false, ignoreCase = false)
                        checkIsInMainPage()
                    }
                    "opens a non-closable screen" -> {
                        waitForElementWithTextToBeClickable(actionScreenTitle, likeSearch = false, ignoreCase = false)
                        swipeDown() // tries to close the screen
                        sleep(1000) // swipe animation

                        // confirm the screen is still showing and restarts the app
                        waitForElementWithTextToBeClickable(actionScreenTitle, likeSearch = false, ignoreCase = false)
                        SuiteSetup.restartApp()
                        loadBffScreen()
                    }
                    else -> {
                        throw Exception("Wrong action: $iosAction")
                    }
                }
            } else {
                when (androidAction) {
                    "no action" -> {
                        waitForElementWithTextToBeClickable(button2Title, likeSearch = false, ignoreCase = false)

                        // goes back to the main screen
                        goBack()
                    }
                    "goes back to main screen" -> {
                        waitForElementWithTextToBeClickable(button1Title, likeSearch = false, ignoreCase = false)
                    }
                    "opens a closable screen" -> {
                        waitForElementWithTextToBeClickable(actionScreenTitle, likeSearch = false, ignoreCase = false)
                        goBack()
                        checkIsInMainPage()
                    }
                    "opens a non-closable screen" -> {
                        waitForElementWithTextToBeClickable(actionScreenTitle, likeSearch = false, ignoreCase = false)
                        goBack() // closes the app in this case

                        // opens the app
                        loadBffScreenFromDeepLink()
                    }
                    "closes the app" -> {
                        // confirms the app is closed
                        checkAppIsClosed()

                        // opens the app
                        loadBffScreenFromDeepLink()
                    }
                    else -> {
                        throw Exception("Wrong action: $androidAction")
                    }
                }

            }

        }
    }

    @Then("^validate PushStack and PushView error routes:$")
    fun checkErrorRouteButtonTitle(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            var mainButtonTitle = columns[0]!!
            var newScreenIosButtonTitle = columns[1]!!
            var newScreenAndroidButtonTitle = columns[2]!!

            safeClickOnElement(
                waitForElementWithTextToBeClickable(
                    mainButtonTitle,
                    likeSearch = false,
                    ignoreCase = false
                )
            )

            if (SuiteSetup.isIos()) {
                waitForElementWithTextToBeClickable(newScreenIosButtonTitle, likeSearch = false, ignoreCase = false)
                safeClickOnElement(waitForElementWithTextToBeClickable("Close", likeSearch = false, ignoreCase = false))
            } else {

                waitForElementWithTextToBeClickable(newScreenAndroidButtonTitle, likeSearch = false, ignoreCase = false)
                goBack()

                if (mainButtonTitle == "ResetStackOtherSDAFailsToShowButton" ||
                    mainButtonTitle == "ResetApplicationOtherSDAFailsToShowButton" ||
                    mainButtonTitle == "ResetStackSameSDA" ||
                    mainButtonTitle == "ResetApplicationSameSDA"
                ) {
                    /**
                     * going backing in these cases closes the app
                     */
                    checkAppIsClosed()
                    loadBffScreenFromDeepLink()
                }

            }
        }

    }

    private fun checkIsInMainPage() {
        waitForElementWithTextToBeClickable("PushStackRemote", likeSearch = false, ignoreCase = false)
        waitForElementWithTextToBeClickable("PushViewRemote", likeSearch = false, ignoreCase = false)
    }

    private fun checkAppIsClosed() {
        waitForElementWithTextToBeInvisible(
            "PushStackRemote",
            likeSearch = false,
            ignoreCase = false
        )
        waitForElementWithTextToBeInvisible(
            "PopToViewInvalidRoute",
            likeSearch = false,
            ignoreCase = false
        )
        waitForElementWithTextToBeInvisible(
            "Reset Screen",
            likeSearch = false,
            ignoreCase = false
        )
        waitForElementWithTextToBeInvisible(
            "Try again",
            likeSearch = false,
            ignoreCase = true
        )
        waitForElementWithTextToBeInvisible(
            "Retry",
            likeSearch = false,
            ignoreCase = true
        )
    }
}