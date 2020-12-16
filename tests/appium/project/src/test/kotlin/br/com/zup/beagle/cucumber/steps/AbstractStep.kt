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
import org.openqa.selenium.ScreenOrientation
import java.io.File


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
            DEFAULT_ELEMENT_WAIT_TIME_IN_MILL,
            0
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
            DEFAULT_ELEMENT_WAIT_TIME_IN_MILL,
            0
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

    @Deprecated(
        message = "Seaches the element and scroll to it, but some platforms won't render the element when it is not visible. " +
                "Use swipe instead"
    )
    protected fun scrollDownToElementWithText(
        elementText: String,
        likeSearch: Boolean,
        ignoreCase: Boolean
    ): MobileElement {
        return scrollToElement(elementText, likeSearch, ignoreCase, SwipeDirection.DOWN)
    }

    private fun scrollToElement(
        elementText: String,
        likeSearch: Boolean,
        ignoreCase: Boolean,
        direction: SwipeDirection
    ): MobileElement {
        val xpath: By = getSearchByTextXpath(elementText, likeSearch, ignoreCase)

        // since the element might not be visible until scrolled to, should wait for presence, not visibility
        val element: MobileElement = AppiumUtil.waitForElementToBePresent(
            getDriver(),
            xpath,
            DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
        )

        return AppiumUtil.scrollToElement(getDriver(), element, direction, DEFAULT_ELEMENT_WAIT_TIME_IN_MILL)
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
        val databaseFile = File("${getScreenshotDatabaseFolder()}/${imageName}.png")
        val screenshotFile = getAppScreenShot()
        val resultFile =
            File(
                "${SuiteSetup.ERROR_SCREENSHOTS_FOLDER}/Comparison-" +
                        "${FilenameUtils.removeExtension(databaseFile.name)}-${System.currentTimeMillis()}.png"
            )

        ImageUtil.compareImages(screenshotFile, databaseFile, resultFile)

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
        val destinationFile = File("${getScreenshotDatabaseFolder()}/${imageName}.png")

        if (destinationFile.exists())
            destinationFile.delete()

        FileUtils.moveFile(
            screenshotFile,
            destinationFile
        )
    }

    private fun getAppScreenShot(): File {
        if (SuiteSetup.isAndroid()) {
            return AppiumUtil.getAppScreenshot(getDriver(), getBaseElementXpath())
        } else {
            return AppiumUtil.getIosAppScreenshot(getDriver())
        }

    }

    // TODO: verify if base elements are always the same and not Beagle \ bff related. In this case, move this method to AppiumUtil
    private fun getBaseElementXpath(): By {
        return if (SuiteSetup.isAndroid()) By.id("android:id/content") else By.id("")
    }

    private fun getScreenshotDatabaseFolder(): String {
        val imagesDbDir = if (SuiteSetup.isAndroid())
            return SuiteSetup.SCREENSHOTS_DATABASE_FOLDER + "/android"
        else
            return SuiteSetup.SCREENSHOTS_DATABASE_FOLDER + "/ios"
    }

}