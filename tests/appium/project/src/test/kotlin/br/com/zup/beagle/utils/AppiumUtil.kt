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

package br.com.zup.beagle.utils

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidTouchAction
import io.appium.java_client.functions.ExpectedCondition
import io.appium.java_client.ios.IOSTouchAction
import io.appium.java_client.touch.WaitOptions
import io.appium.java_client.touch.offset.PointOption
import org.openqa.selenium.*
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import java.awt.image.BufferedImage
import java.io.File
import java.time.Duration
import java.util.*
import javax.imageio.ImageIO
import kotlin.Array
import kotlin.Boolean
import kotlin.Exception
import kotlin.IllegalArgumentException
import kotlin.Int
import kotlin.Long
import kotlin.String
import java.util.HashMap


object AppiumUtil {

    @Synchronized
    fun scrollToElement(
        driver: MobileDriver<*>,
        elementLocator: By,
        swipeDirection: SwipeDirection,
        elementSearchTimeout: Long,
        overallSearchTimeout: Long
    ): MobileElement {

        if (overallSearchTimeout <= elementSearchTimeout)
            throw Exception("Swipe timeout must be greater than element search timeout!")

        val wait = FluentWait(driver)
        wait.withTimeout(Duration.ofMillis(overallSearchTimeout))
        return wait.until(ExpectedCondition<MobileElement> {
            if (elementExists(driver, elementLocator, elementSearchTimeout)) {
                return@ExpectedCondition driver.findElement(elementLocator) as MobileElement
            } else {
                swipeScreenTo(driver, swipeDirection)
                return@ExpectedCondition null
            }
        })
    }

    @Synchronized
    fun swipeScreenTo(driver: MobileDriver<*>, swipeDirection: SwipeDirection) {
        if (driver is AndroidDriver<*>)
            androidSwipeScreenTo(driver, swipeDirection)
        else
            iosSwipeScreenTo(driver, swipeDirection)
    }

    /**
     *
     * Performs swipe from the center of screen
     * Adapted from http://appium.io/docs/en/writing-running-appium/tutorial/swipe/ios-mobile-screen/
     */
    private fun iosSwipeScreenTo(driver: MobileDriver<*>, swipeDirection: SwipeDirection) {

        val animationTime = 200 // ms
        val scrollObject = HashMap<String, String>()
        when (swipeDirection) {
            SwipeDirection.DOWN -> scrollObject["direction"] = "down"
            SwipeDirection.UP -> scrollObject["direction"] = "up"
            SwipeDirection.LEFT -> scrollObject["direction"] = "left"
            SwipeDirection.RIGHT -> scrollObject["direction"] = "right"
            else -> throw IllegalArgumentException("mobileSwipeScreenIOS(): dir: '$swipeDirection' NOT supported")
        }

        (driver as JavascriptExecutor).executeScript("mobile:swipe", scrollObject)

        Thread.sleep(animationTime.toLong())
    }


    /**
     *
     * Performs swipe from the center of screen
     * Adapted from http://appium.io/docs/en/writing-running-appium/tutorial/swipe/simple-screen/
     */
    private fun androidSwipeScreenTo(driver: MobileDriver<*>, swipeDirection: SwipeDirection) {

        val animationTime = 200
        val pressTime = 200
        val edgeBorder = 10
        val pointOptionStart: PointOption<*>
        val pointOptionEnd: PointOption<*>

        // init screen variables
        val dims: Dimension = driver.manage().window().getSize()

        // init start point = center of screen
        pointOptionStart = PointOption.point(dims.width / 2, dims.height / 2)
        pointOptionEnd = when (swipeDirection) {
            SwipeDirection.DOWN -> PointOption.point(dims.width / 2, dims.height - edgeBorder)
            SwipeDirection.UP -> PointOption.point(dims.width / 2, edgeBorder)
            SwipeDirection.LEFT -> PointOption.point(edgeBorder, dims.height / 2)
            SwipeDirection.RIGHT -> PointOption.point(dims.width - edgeBorder, dims.height / 2)
            else -> throw IllegalArgumentException("swipeScreen(): dir: '$swipeDirection' NOT supported")
        }

        // execute swipe using TouchAction
        AndroidTouchAction(driver)
            .press(pointOptionStart)
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(pressTime.toLong())))
            .moveTo(pointOptionEnd)
            .release().perform()


        Thread.sleep(animationTime.toLong())

    }

    /**
     * Scrolls from the position of a given point to the border of the screen
     */
    fun androidScrollScreenFromOnePointToBorder(
        driver: MobileDriver<*>,
        originPoint: Point,
        swipeDirection: SwipeDirection
    ) {

        val animationTime = 200 // ms
        val pressTime = 200 // ms
        val borderEdge = 1
        val screenSize = driver.manage().window().size

        val destinationPoint = when (swipeDirection) {
            SwipeDirection.DOWN -> PointOption.point(originPoint.x, screenSize.height - borderEdge)
            SwipeDirection.UP -> PointOption.point(originPoint.x, borderEdge)
            SwipeDirection.LEFT -> PointOption.point(borderEdge, originPoint.y)
            SwipeDirection.RIGHT -> PointOption.point(screenSize.width - borderEdge, originPoint.y)
            else -> throw IllegalArgumentException("Diretion '$swipeDirection' not supported")
        }

        AndroidTouchAction(driver)
            .press(PointOption.point(originPoint.x, originPoint.y))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(pressTime.toLong())))
            .moveTo(destinationPoint).perform()

        Thread.sleep(animationTime.toLong())

    }

    /**
     * Scrolls from the position of a given point to the center of the screen
     */
    fun androidScrollScreenFromOnePointToCenterPoint(
        driver: MobileDriver<*>,
        originPoint: Point,
        horizontalScroll: Boolean
    ) {

        val animationTime = 200 // ms
        val pressTime = 200 // ms
        val screenSize = driver.manage().window().size
        var destinationPointX = originPoint.x
        var destinationPointY = originPoint.y

        if (horizontalScroll){
            destinationPointX = screenSize.width/2
        }else{
            destinationPointY = screenSize.height/2
        }

        val destinationPoint = PointOption.point(destinationPointX, destinationPointY)

        AndroidTouchAction(driver)
            .press(PointOption.point(originPoint.x, originPoint.y))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(pressTime.toLong())))
            .moveTo(destinationPoint).perform()

        Thread.sleep(animationTime.toLong())

    }

    /**
     * Scrolls from the position of a given element to the border of the screen
     */
    fun iosScrollScreenFromOnePointToBorder(
        driver: MobileDriver<*>,
        originPoint: Point,
        swipeDirection: SwipeDirection
    ) {
        val horizontalBorderEdge = 10
        val verticalBorderEdge = 300
        val screenSize = driver.manage().window().size

        /**
         * Should not click outside the screen border. On iOS, clicks near the border sometimes won't work, so
         * the origin point is reworked to half the size of the screen
         */
        if (originPoint.x >= screenSize.width / 2)
            originPoint.x = screenSize.width / 2
        if (originPoint.y >= screenSize.height / 2)
            originPoint.y = screenSize.height / 2

        val destinationPoint = when (swipeDirection) {
            SwipeDirection.DOWN -> Point(originPoint.x, screenSize.height - verticalBorderEdge)
            SwipeDirection.UP -> Point(originPoint.x, verticalBorderEdge)
            SwipeDirection.LEFT -> Point(horizontalBorderEdge, originPoint.y)
            SwipeDirection.RIGHT -> Point(screenSize.width - horizontalBorderEdge, originPoint.y)
            else -> throw IllegalArgumentException("Diretion '$swipeDirection' not supported")
        }

        IOSTouchAction(driver)
            .press(PointOption.point(originPoint.x, originPoint.y))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
            .moveTo(PointOption.point(destinationPoint)).perform()
    }

    /**
     * Scrolls from the position of a given point to the center of the screen
     */
    fun iosScrollScreenFromOnePointToCenterPoint(
        driver: MobileDriver<*>,
        originPoint: Point,
        horizontalScroll: Boolean
    ) {

        val animationTime = 200 // ms
        val pressTime = 200 // ms
        val screenSize = driver.manage().window().size
        var destinationPointX = originPoint.x
        var destinationPointY = originPoint.y

        // should not click outside the screen border. For some reason on iOS, clicks near the border won't work, so
        // the origin point is reworked to half the size of the screen
        if (originPoint.x >= screenSize.width / 2)
            originPoint.x = screenSize.width / 2
        if (originPoint.y >= screenSize.height / 2)
            originPoint.y = screenSize.height / 2

        if (horizontalScroll){
            destinationPointX = screenSize.width/2
        }else{
            destinationPointY = screenSize.height/2
        }

        val destinationPoint = PointOption.point(destinationPointX, destinationPointY)

        IOSTouchAction(driver)
            .press(PointOption.point(originPoint.x, originPoint.y))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(pressTime.toLong())))
            .moveTo(destinationPoint).perform()

        Thread.sleep(animationTime.toLong())

    }

    /**
     * Waits for an element to be found on the screen element tree. This does not
     * necessarily mean that the element is visible.
     */
    @Synchronized
    fun waitForElementToBePresent(driver: MobileDriver<*>, locator: By, timeoutInMilliseconds: Long): MobileElement {
        val wait: FluentWait<MobileDriver<*>> = FluentWait<MobileDriver<*>>(driver)
        wait.pollingEvery(Duration.ofMillis(200))
        wait.withTimeout(Duration.ofMillis(timeoutInMilliseconds))
        wait.ignoring(NoSuchElementException::class.java)
        wait.ignoring(StaleElementReferenceException::class.java)
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator)) as MobileElement
    }

    /**
     * Waits for an element, child of a given parent element, to be found on the screen element tree. This does not
     * necessarily mean that the element is visible.
     */
    @Synchronized
    fun waitForChildElementToBePresent(driver: MobileDriver<*>, parentElement: MobileElement, locator: By, timeoutInMilliseconds: Long): MobileElement {
        val wait: FluentWait<MobileDriver<*>> = FluentWait<MobileDriver<*>>(driver)
        wait.pollingEvery(Duration.ofMillis(200))
        wait.withTimeout(Duration.ofMillis(timeoutInMilliseconds))
        wait.ignoring(NoSuchElementException::class.java)
        wait.ignoring(StaleElementReferenceException::class.java)
        return wait.until {
            parentElement.findElement(locator)
        } as MobileElement
    }

    /**
     * Waits for a list of elements, children of a given parent element, to be found on the screen element tree. This does not
     * necessarily mean that these elements are visible.
     */
    @Synchronized
    fun waitForChildrenElementsToBePresent(driver: MobileDriver<*>, parentElement: MobileElement, locator: By, timeoutInMilliseconds: Long): List<MobileElement> {
        val wait: FluentWait<MobileDriver<*>> = FluentWait<MobileDriver<*>>(driver)
        wait.pollingEvery(Duration.ofMillis(200))
        wait.withTimeout(Duration.ofMillis(timeoutInMilliseconds))
        wait.ignoring(NoSuchElementException::class.java)
        wait.ignoring(StaleElementReferenceException::class.java)
        return wait.until {
            parentElement.findElements(locator)
        } as List<MobileElement>
    }

    /**
     * Waits for an element to be visible and enabled (clickable)
     */
    @Synchronized
    fun waitForElementToBeClickable(
        driver: MobileDriver<*>,
        locator: By,
        timeoutInMilliseconds: Long,
    ): MobileElement {
        val wait = FluentWait(driver)
        wait.pollingEvery(Duration.ofMillis(200))
        wait.withTimeout(Duration.ofMillis(timeoutInMilliseconds))
        wait.ignoring(NoSuchElementException::class.java)
        wait.ignoring(StaleElementReferenceException::class.java)
        wait.ignoring(ElementNotInteractableException::class.java)
        wait.ignoring(ElementNotVisibleException::class.java)
        return wait.until(ExpectedConditions.elementToBeClickable(locator)) as MobileElement // clickable = verifies enabled e visibility
    }

    /**
     * Waits for an element to be visible and enabled (clickable)
     */
    @Synchronized
    fun waitForElementToBeClickable(
        driver: MobileDriver<*>,
        element: MobileElement,
        timeoutInMilliseconds: Long
    ): MobileElement {
        val wait = FluentWait(driver)
        wait.pollingEvery(Duration.ofMillis(200))
        wait.withTimeout(Duration.ofMillis(timeoutInMilliseconds))
        wait.ignoring(NoSuchElementException::class.java)
        wait.ignoring(StaleElementReferenceException::class.java)
        wait.ignoring(ElementNotInteractableException::class.java)
        wait.ignoring(ElementNotVisibleException::class.java)
        return wait.until(ExpectedConditions.elementToBeClickable(element)) as MobileElement
    }

    /**
     * Waits for a set of elements to be visible and enabled (clickable)
     */
    @Synchronized
    fun waitForElementsToBeClickable(
        driver: MobileDriver<*>,
        elements: Array<MobileElement>,
        timeoutInMilliseconds: Long
    ) {
        for (element in elements) {
            waitForElementToBeClickable(driver, element, timeoutInMilliseconds)
        }
    }

    /**
     * Waits for an element to be hidden or nonexistent
     */
    @Synchronized
    fun waitForElementToBeInvisible(driver: MobileDriver<*>, locator: By, timeoutInMilliseconds: Long): Boolean {
        val wait = FluentWait(driver)
        wait.withTimeout(Duration.ofMillis(timeoutInMilliseconds))
        wait.pollingEvery(Duration.ofMillis(200))
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator))
    }

    /**
     * Waits for an element to be visible and disabled (not clickable)
     */
    @Synchronized
    fun waitForElementToBeDisabled(driver: MobileDriver<*>, locator: By, timeoutInMilliseconds: Long): Boolean {
        val mobileElement = waitForElementToBePresent(driver, locator, timeoutInMilliseconds)
        val wait = FluentWait(driver)
        wait.withTimeout(Duration.ofMillis(timeoutInMilliseconds))
        wait.pollingEvery(Duration.ofMillis(200))
        return wait.until {
            mobileElement.isDisplayed && !mobileElement.isEnabled
        }
    }

    @Synchronized
    fun waitForElementTextToBe(
        driver: MobileDriver<*>,
        element: MobileElement,
        text: String,
        timeoutInMilliseconds: Long
    ): Boolean {
        val wait = FluentWait(driver)
        wait.withTimeout(Duration.ofMillis(timeoutInMilliseconds))
        return wait.until {
            return@until element.text == text
        }
    }

    @Synchronized
    fun waitForElementAttributeToBe(
        driver: MobileDriver<*>,
        element: MobileElement,
        attribute: String,
        value: String,
        timeoutInMilliseconds: Long
    ): Boolean {
        val wait = FluentWait(driver)
        wait.withTimeout(Duration.ofMillis(timeoutInMilliseconds))
        wait.pollingEvery(Duration.ofMillis(200))
        return wait.until {
            element.getAttribute(attribute) != null && element.getAttribute(attribute) == value
        }
    }

    /**
     *
     * @param tableElement
     * @param value
     * @return line elements (tr) that contains the given value (@param value) in its children's text() attribute
     */
    @Synchronized
    fun getTableLineElementsThatContainsText(
        tableElement: MobileElement,
        value: String
    ): List<MobileElement> {
        val results: List<MobileElement>
        val xpath = ".//tr[.//*[contains(text(),'$value')]]"
        results = tableElement.findElements(By.xpath(xpath))
        return results
    }

    @Synchronized
    fun elementExists(driver: MobileDriver<*>, locator: By, timeoutInMilliseconds: Long): Boolean {
        try {
            waitForElementToBePresent(driver, locator, timeoutInMilliseconds)
            return true // element found
        } catch (e: Exception) {
        }
        return false
    }


    /**
     * Tries to set a value for timeoutInMilliseconds
     */
    @Synchronized
    fun setElementValue(
        driver: MobileDriver<*>,
        element: MobileElement,
        value: String,
        timeoutInMilliseconds: Long
    ): Boolean {
        element.clear()
        element.sendKeys(value)
        val wait = FluentWait(driver)
        wait.withTimeout(Duration.ofMillis(timeoutInMilliseconds))
        wait.pollingEvery(Duration.ofMillis(200))
        return wait.until(ExpectedCondition<Boolean> {
            val currentElementValue = element.getAttribute("value")
            if (currentElementValue != null && value.equals(currentElementValue))
                return@ExpectedCondition true

            element.clear()
            element.sendKeys(value)
            return@ExpectedCondition false
        })
    }

    /**
     * @return true if element1 is above element2
     */
    @Synchronized
    fun isElementAboveElement(element1: MobileElement, element2: MobileElement): Boolean {
        var element1LocationY: Int = element1.location.y // + element1.size.height
        var element2LocationY: Int = element2.location.y

        if (element2LocationY > element1LocationY)
            return true

        return false
    }

    @Synchronized
    fun getPropertyXpath(property: String, propertyValue: String, likeSearch: Boolean, ignoreCase: Boolean): By {
        var key = "@$property"
        var value = "\"$propertyValue\""

        if (ignoreCase) {
            key = "translate(@$property, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"
            value = value.toLowerCase()
        }

        return if (likeSearch) {
            By.xpath("//*[contains($key, $value)]")
        } else {
            By.xpath("//*[$key=$value]")
        }
    }

    /**
     * @return the current running app screenshot without the device's status and navigation bar, using
     * @param locator to crop the screenshot
     */
    @Synchronized
    fun getAppScreenshot(driver: AppiumDriver<*>, locator: By): File {

        // gets the base element's position
        val baseElement = driver.findElement(locator)
        val baseElementLocation = baseElement.location
        val baseElementWidth = baseElement.size.getWidth()
        val baseElementHeight = baseElement.size.getHeight()

        // takes a screenshot of the whole device screen.
        val screenshot = (driver as TakesScreenshot).getScreenshotAs(OutputType.FILE)

        // crops the image selection only the base element coordinates
        val bufferedImage = ImageIO.read(screenshot)
        val baseElementScreenshot: BufferedImage = bufferedImage.getSubimage(
            baseElementLocation.getX(),
            baseElementLocation.getY(),
            baseElementWidth,
            baseElementHeight
        )

        // save the cropped image overwriting the screenshot file
        ImageIO.write(baseElementScreenshot, "png", screenshot)

        return screenshot
    }

    /**
     * @return the current running app screenshot without the device's status and navigation bar
     * Device: iPhone 11
     * macOS resolution: 2560 x 1600
     */
    @Synchronized
    fun getIosAppScreenshot(driver: AppiumDriver<*>): File {

        // takes a screenshot of the whole device screen.
        val screenshot = (driver as TakesScreenshot).getScreenshotAs(OutputType.FILE)

        // crops the image selection only the base element coordinates
        val bufferedImage = ImageIO.read(screenshot)
        val baseElementScreenshot: BufferedImage = bufferedImage.getSubimage(
            0,
            65,
            828,
            1680
        )

        // save the cropped image overwriting the screenshot file
        ImageIO.write(baseElementScreenshot, "png", screenshot)

        return screenshot
    }
}