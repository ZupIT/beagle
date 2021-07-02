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
        waitForElementWithTextToBeClickable("Navigation Screen")
    }

    @Then("^validate PushStack and PushView screen titles:$")
    fun checkPushTitles(dataTable: DataTable) {
        val rows = dataTable.asLists()
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            val button1Title = columns[0]!!
            val newScreenTitle = columns[1]!!

            safeClickOnElement(waitForElementWithTextToBeClickable(button1Title))
            waitForElementWithTextToBeClickable(newScreenTitle, ignoreCase = true)

            // goes back to the main screen
            safeClickOnElement(waitForElementWithTextToBeClickable("PopView"))
            waitForElementWithTextToBeClickable(button1Title)

        }

    }


    @Then("^validate PushStack and PushView actions:$")
    fun checkPushProperties(dataTable: DataTable) {
        val rows = dataTable.asLists()
        val actionScreenTitle = "Reset Screen"
        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            val button1Title = columns[0]!!
            val button2Title = columns[1]!!
            val iosAction = columns[2]!!
            val androidAction = columns[3]!!

            safeClickOnElement(waitForElementWithTextToBeClickable(button1Title))
            safeClickOnElement(waitForElementWithTextToBeClickable(button2Title))

            if (SuiteSetup.isIos()) {
                // iOS screen transition animation
                when (iosAction) {
                    "no action" -> {
                        waitForElementWithTextToBeClickable(button2Title)

                        // goes back to the main screen
                        waitForElementWithTextToBeClickable("PopView").click()
                        waitForElementWithTextToBeClickable(button1Title)
                    }
                    "goes back to main screen" -> {
                        waitForElementWithTextToBeClickable(button1Title)
                    }
                    "opens a closable screen" -> {
                        waitForElementWithTextToBeClickable(actionScreenTitle)
                        swipeDown()

                        /**
                         * Retries to close the screen in case the first attempt failed. This behavior seems to exist
                         * only on iOS
                         */
                        try {
                            waitForElementWithTextToBeInvisible(actionScreenTitle)
                        } catch (e: Exception) {
                            println("Failed to close the screen invoked by $button1Title / $button2Title. Trying once more...")
                            swipeDown()
                            waitForElementWithTextToBeInvisible(actionScreenTitle)
                            println("Success!")
                        }

                        checkIsInMainPage()
                    }
                    "opens a non-closable screen" -> {
                        waitForElementWithTextToBeClickable(actionScreenTitle)
                        swipeDown() // tries to close the screen
                        sleep(400) // swipe animation

                        // confirm the screen is still showing and restarts the app
                        waitForElementWithTextToBeClickable(actionScreenTitle)
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
                        waitForElementWithTextToBeClickable(button2Title)

                        // goes back to the main screen
                        goBack()
                    }
                    "goes back to main screen" -> {
                        waitForElementWithTextToBeClickable(button1Title)
                    }
                    "opens a closable screen" -> {
                        waitForElementWithTextToBeClickable(actionScreenTitle)
                        goBack()
                        checkIsInMainPage()
                    }
                    "opens a non-closable screen" -> {
                        waitForElementWithTextToBeClickable(actionScreenTitle)
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

            val mainButtonTitle = columns[0]!!
            val newScreenIosButtonTitle = columns[1]!!
            val newScreenAndroidButtonTitle = columns[2]!!

            safeClickOnElement(waitForElementWithTextToBeClickable(mainButtonTitle))

            if (SuiteSetup.isIos()) {
                waitForElementWithTextToBeClickable(newScreenIosButtonTitle)
                safeClickOnElement(waitForElementWithTextToBeClickable("Close"))
            } else {

                waitForElementWithTextToBeClickable(newScreenAndroidButtonTitle)
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
        waitForElementWithTextToBeClickable("PushStackRemote")
        waitForElementWithTextToBeClickable("PushViewRemote")
    }

    private fun checkAppIsClosed() {
        waitForElementWithTextToBeInvisible("PushStackRemote")
        waitForElementWithTextToBeInvisible("PopToViewInvalidRoute")
        waitForElementWithTextToBeInvisible("Reset Screen")
        waitForElementWithTextToBeInvisible("Try again")
        waitForElementWithTextToBeInvisible("Retry", ignoreCase = true)
    }
}