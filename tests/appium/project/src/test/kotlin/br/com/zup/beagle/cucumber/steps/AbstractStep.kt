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

import br.com.zup.beagle.screens.MainScreen
import br.com.zup.beagle.setup.DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
import br.com.zup.beagle.setup.SuiteSetup
import br.com.zup.beagle.utils.AppiumUtil
import br.com.zup.beagle.utils.ImageUtil
import br.com.zup.beagle.utils.SwipeDirection
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidTouchAction
import io.appium.java_client.ios.IOSTouchAction
import io.appium.java_client.touch.offset.PointOption
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.ScreenOrientation
import java.io.File
import java.util.HashMap


abstract class AbstractStep {

    abstract var bffRelativeUrlPath: String

    private var driver: AppiumDriver<*>? = null

    protected fun getDriver(): AppiumDriver<*> {
        if (driver == null) {
            driver = SuiteSetup.getDriver()
        }
        return driver!!
    }

    protected fun loadBffScreenFromMainScreen() {
        val mainScreen = MainScreen(getDriver())
        mainScreen.setBffUrl(SuiteSetup.getBffBaseUrl() + bffRelativeUrlPath)
        mainScreen.clickOnGoButton()
    }

    protected fun loadBffScreenFromDeepLink() {
        if (SuiteSetup.isAndroid()) {
            val params = HashMap<String, String>()
            params["url"] = "appiumapp://bffurl/" + SuiteSetup.getBffBaseUrl() + bffRelativeUrlPath
            params["package"] = "br.com.zup.beagle.appiumapp"
            (getDriver() as JavascriptExecutor).executeScript("mobile:deepLink", params)
        } else {
            getDriver().get("appiumapp://" + SuiteSetup.getBffBaseUrl() + bffRelativeUrlPath)
        }
    }

    protected fun loadBffScreen() {
        if (SuiteSetup.isAndroid()) {
            loadBffScreenFromDeepLink()
        } else {
            /**
             * Deep link test strategy on iOS (URL Scheme) doesn't boost test speed when tests run on non-gpu Simulators (ex. GitHub Actions)
             * or real Devices (ex. Browserstack). Because of this, deep link is turned off by default on iOS.
             *
             * When testing on a gpu-enabled Simulator (ex. testing locally on a mac computer), the deep link strategy might result on some
             * speed boost. To enable deep link on iOS, use method loadBffScreenFromDeepLink() instead of method loadBffScreenFromMainScreen().
             */
            loadBffScreenFromMainScreen()
        }
    }

    /**
     * Waits for an element to be visible and enabled (clickable)
     */
    protected fun waitForElementWithTextToBeClickable(
        elementText: String,
        likeSearch: Boolean,
        ignoreCase: Boolean
    ): MobileElement {
        val xpath: By = getSearchByTextXpath(elementText, likeSearch, ignoreCase)
        return AppiumUtil.waitForElementToBeClickable(
            getDriver(),
            xpath,
            DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
        )

    }

    /**
     * Waits for an element to be visible and enabled (clickable)
     */
    protected fun waitForElementWithValueToBeClickable(
        elementValue: String,
        likeSearch: Boolean,
        ignoreCase: Boolean
    ): MobileElement {
        val xpath: By = getSearchByValueXpath(elementValue, likeSearch, ignoreCase)
        return AppiumUtil.waitForElementToBeClickable(
            getDriver(),
            xpath,
            DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
        )

    }

    /**
     * Waits for an element to be present on the screen
     */
    protected fun waitForElementWithValueToBePresent(
        elementValue: String,
        likeSearch: Boolean,
        ignoreCase: Boolean
    ): MobileElement {
        val xpath: By = getSearchByValueXpath(elementValue, likeSearch, ignoreCase)
        return AppiumUtil.waitForElementToBePresent(
            getDriver(),
            xpath,
            DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
        )

    }

    /**
     * Waits for an element to be visible and disabled (not clickable)
     */
    protected fun waitForElementWithTextToBeDisabled(elementText: String, likeSearch: Boolean, ignoreCase: Boolean) {
        val xpath: By = getSearchByTextXpath(elementText, likeSearch, ignoreCase)
        AppiumUtil.waitForElementToBeDisabled(getDriver(), xpath, DEFAULT_ELEMENT_WAIT_TIME_IN_MILL)
    }

    /**
     * Waits for an element to be visible and disabled (not clickable)
     */
    protected fun waitForElementWithValueToBeDisabled(elementValue: String, likeSearch: Boolean, ignoreCase: Boolean) {
        val xpath: By = getSearchByValueXpath(elementValue, likeSearch, ignoreCase)
        AppiumUtil.waitForElementToBeDisabled(getDriver(), xpath, DEFAULT_ELEMENT_WAIT_TIME_IN_MILL)
    }

    /**
     * Waits for an element to be hidden or nonexistent
     */
    protected fun waitForElementWithTextToBeInvisible(elementText: String, likeSearch: Boolean, ignoreCase: Boolean) {
        val xpath: By = getSearchByTextXpath(elementText, likeSearch, ignoreCase)
        AppiumUtil.waitForElementToBeInvisible(getDriver(), xpath, DEFAULT_ELEMENT_WAIT_TIME_IN_MILL)
    }

    /**
     * Waits for an element to be hidden or nonexistent
     */
    protected fun waitForElementWithValueToBeInvisible(elementValue: String, likeSearch: Boolean, ignoreCase: Boolean) {
        val xpath: By = getSearchByValueXpath(elementValue, likeSearch, ignoreCase)
        AppiumUtil.waitForElementToBeInvisible(getDriver(), xpath, DEFAULT_ELEMENT_WAIT_TIME_IN_MILL)
    }

    protected fun scrollDownToElementWithText(
        elementText: String,
        likeSearch: Boolean,
        ignoreCase: Boolean
    ): MobileElement {
        val locator = getSearchByTextXpath(elementText, likeSearch, ignoreCase)
        return scrollToElement(locator, SwipeDirection.UP) // swiping up scrolls down...
    }

    protected fun scrollDownToElementWithValue(
        elementValue: String,
        likeSearch: Boolean,
        ignoreCase: Boolean
    ): MobileElement {
        val locator = getSearchByValueXpath(elementValue, likeSearch, ignoreCase)
        return scrollToElement(locator, SwipeDirection.UP)
    }

    protected fun scrollUpToElementWithText(
        elementText: String,
        likeSearch: Boolean,
        ignoreCase: Boolean
    ): MobileElement {
        val locator = getSearchByTextXpath(elementText, likeSearch, ignoreCase)
        return scrollToElement(locator, SwipeDirection.DOWN) // swiping down scrolls up...
    }

    protected fun scrollUpToElementWithValue(
        elementText: String,
        likeSearch: Boolean,
        ignoreCase: Boolean
    ): MobileElement {
        val locator = getSearchByValueXpath(elementText, likeSearch, ignoreCase)
        return scrollToElement(locator, SwipeDirection.DOWN) // swiping down scrolls up...
    }

    private fun scrollToElement(
        elementLocator: By,
        direction: SwipeDirection
    ): MobileElement {
        return AppiumUtil.scrollToElement(
            getDriver(),
            elementLocator,
            direction,
            1500,
            DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
        )
    }

    protected fun hideKeyboard() {
        getDriver().hideKeyboard()
    }

    /**
     * Gets elements by text and returns true if element1 is above element2
     */
    protected fun isElementAbove(
        elementText1: String,
        elementText2: String,
        likeSearch: Boolean,
        ignoreCase: Boolean
    ): Boolean {
        val element1: MobileElement = waitForElementWithTextToBeClickable(elementText1, likeSearch, ignoreCase)
        val element2: MobileElement = waitForElementWithTextToBeClickable(elementText2, likeSearch, ignoreCase)
        return AppiumUtil.isElementAboveElement(element1, element2)
    }

    protected fun sleep(milliseconds: Long) {
        try {
            Thread.sleep(milliseconds)
        } catch (e: Exception) {
        }
    }

    protected fun swipeLeft() {
        AppiumUtil.swipeScreenTo(getDriver(), SwipeDirection.LEFT)
    }

    protected fun swipeRight() {
        AppiumUtil.swipeScreenTo(getDriver(), SwipeDirection.RIGHT)
    }

    protected fun swipeUp() {
        AppiumUtil.swipeScreenTo(getDriver(), SwipeDirection.UP)
    }

    protected fun swipeDown() {
        AppiumUtil.swipeScreenTo(getDriver(), SwipeDirection.DOWN)
    }

    protected fun rotateToLandscapePosition() {
        getDriver().rotate(ScreenOrientation.LANDSCAPE);
    }

    protected fun rotateToPortraitPosition() {
        getDriver().rotate(ScreenOrientation.PORTRAIT);
    }

    private fun getSearchByTextXpath(elementText: String, likeSearch: Boolean, ignoreCase: Boolean): By {
        val property = if (SuiteSetup.isAndroid())
            "text"
        else
            "name"

        return AppiumUtil.getPropertyXpath(property, elementText, likeSearch, ignoreCase)
    }

    private fun getSearchByValueXpath(elementHint: String, likeSearch: Boolean, ignoreCase: Boolean): By {
        val property = if (SuiteSetup.isAndroid())
            "text"
        else
            "value"
        return AppiumUtil.getPropertyXpath(property, elementHint, likeSearch, ignoreCase)
    }

    private fun getSearchByImageXpath(): By {
        val xpath: By
        if (SuiteSetup.isAndroid()) {
            xpath = By.xpath("//*[contains(@class,'ImageView')]")
        } else {
            xpath = By.xpath("//*[contains(@type,'XCUIElementTypeImage')]")
        }
        return xpath
    }


    protected fun isTextFieldNumeric(elementText: String): Boolean {
        val textElement = waitForElementWithValueToBeClickable(elementText, false, false)
        textElement.click()
        sleep(1000) // TouchActions sometimes get called before an element is ready to write
        if (SuiteSetup.isAndroid()) {
            var androidActions = AndroidTouchAction(getDriver())
            androidActions.press(PointOption.point(500, 1700)).release().perform() // digit 5
        } else {
            var iosActions = IOSTouchAction(getDriver())
            iosActions.press(PointOption.point(160, 660)).release().perform()
        }

        var typedChar =
            if (textElement.text.length > 1) textElement.text.substring(textElement.text.length - 1)
            else textElement.text

        try {
            typedChar.toInt()
        } catch (nfe: NumberFormatException) {
            return false
        }

        return true
    }

    /**
     * Waits for the image at order @order to be clickable and return it
     */
    protected fun waitForImageElementToBeVisible(order: Int): MobileElement {
        return AppiumUtil.waitForElementToBeClickable(
            getDriver(),
            waitForImageElements().get(order),
            DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
        )
    }


    /**
     * @return all image elements
     */
    protected fun waitForImageElements(): List<MobileElement> {
        val xpath = getSearchByImageXpath()
        return getDriver().findElements(xpath) as List<MobileElement>
    }

    /**
     * Takes a screenshot of the current screen and compares it with @param imageName
     * @return true if images are identical or false otherwise.
     */
    protected fun compareCurrentScreenWithDatabase(imageName: String): Boolean {
        val databaseScreenshotFile = File("${getScreenshotDatabaseFolderPath()}/${imageName}.png")

        if (!databaseScreenshotFile.exists())
            throw Exception(
                "Screenshot database file not found: ${databaseScreenshotFile}! " +
                        "Refer to function registerCurrentScreenInDatabase to create a reference screenshot file"
            )

        val queryScreenshotFile = getAppScreenShot()
        val resultFile =
            File(
                "${SuiteSetup.ERROR_SCREENSHOTS_FOLDER}/Comparison-" +
                        "${FilenameUtils.removeExtension(databaseScreenshotFile.name)}-${System.currentTimeMillis()}.png"
            )

        ImageUtil.compareImages(queryScreenshotFile, databaseScreenshotFile, resultFile)

        // difference found
        if (resultFile.exists()) {
            return false
        }

        return true
    }

    /**
     * Takes a screenshot of the current screen and saves it in the screenshot database folder with
     * the given @param imageName.
     */
    protected fun registerCurrentScreenInDatabase(imageName: String) {
        val screenshotFile = getAppScreenShot()
        val destinationFile = File("${getScreenshotDatabaseFolderPath()}/${imageName}.png")

        if (destinationFile.exists())
            destinationFile.delete()

        FileUtils.moveFile(
            screenshotFile,
            destinationFile
        )
    }

    private fun getAppScreenShot(): File {
        if (SuiteSetup.isAndroid()) {
            return AppiumUtil.getAppScreenshot(getDriver(), getAndroidBaseElementXpath())
        } else {
            return AppiumUtil.getIosAppScreenshot(getDriver())
        }

    }

    // currently, only Android has an element that is present in all screens
    private fun getAndroidBaseElementXpath(): By {
        return if (SuiteSetup.isAndroid()) By.id("android:id/content") else By.id("")
    }

    private fun getScreenshotDatabaseFolderPath(): String {

        /**
         *  Retrieving deviceName from SuiteSetup class instead of driver.getCapability("deviceName")
         *  because the latter changes its value after driver initialization
         */
        val deviceName = SuiteSetup.getDeviceName().trim()

        val dataBaseFolderPath = if (SuiteSetup.isAndroid())
            SuiteSetup.SCREENSHOTS_DATABASE_FOLDER + "/android/" + deviceName
        else
            SuiteSetup.SCREENSHOTS_DATABASE_FOLDER + "/ios/" + deviceName

        if (!File(dataBaseFolderPath).exists())
            throw Exception(
                "Screenshot database folder not found: ${dataBaseFolderPath} " +
                        "\nCreate this folder and refer to function registerCurrentScreenInDatabase " +
                        "to create a reference screenshot inside that folder"
            )

        return dataBaseFolderPath
    }

}