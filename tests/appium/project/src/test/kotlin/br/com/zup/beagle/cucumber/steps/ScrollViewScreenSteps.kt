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
import br.com.zup.beagle.utils.AppiumUtil
import br.com.zup.beagle.utils.SwipeDirection
import io.appium.java_client.MobileBy
import io.appium.java_client.MobileElement
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert
import org.openqa.selenium.By
import org.openqa.selenium.Point
import kotlin.Exception

private const val SCROLLVIEW_SCREEN_HEADER = "Beagle ScrollView"
private const val NEW_TEXT_PREFIX = "Lorem ipsum diam luctus"
private const val NEW_TEXT_SUFFIX = "proin iaculis orci gravida molestie."

class ScrollViewScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/scrollview"

    // cache
    companion object {
        var scrollViewElement1: MobileElement? = null
        var scrollViewElement2: MobileElement? = null
        var scrollViewElement3: MobileElement? = null
    }

    @Before("@scrollview")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the scrollview screen$")
    fun checkScrollViewScreen() {
        waitForElementWithTextToBeClickable(SCROLLVIEW_SCREEN_HEADER, likeSearch = false, ignoreCase = false)
    }


    @When("^I confirm that the ScrollView 1 is not showing a button by default$")
    fun confirmScrollView1IsNotShowingButton() {
        scrollViewElement1 = getScrollViewElement(1)
        Assert.assertFalse(isButtonShowingInsideOfScrollView(scrollViewElement1!!))

    }

    @When("^I expand all the items of ScrollView (.*), checking their new values$")
    fun checkScrollViewNewTexts(scrollViewElementNumber: Int) {
        checkScrollView1NewTexts()
    }

    @Then("^I should view a button by scrolling ScrollView (.*) to the end$")
    fun checkButtonVisible(scrollViewElementNumber: Int) {
        when (scrollViewElementNumber) {
            1 -> Assert.assertTrue(isButtonShowingInsideOfScrollView(scrollViewElement1!!))
            2 -> Assert.assertTrue(isButtonShowingInsideOfScrollView(scrollViewElement2!!))
            else -> throw Exception("Invalid ScrollView number. Only 1 and 2 are possible for this verification")
        }
    }

    private fun isButtonShowingInsideOfScrollView(scrollViewElement: MobileElement): Boolean {
        var locator: By

        if (SuiteSetup.isIos()) {
            locator = MobileBy.iOSClassChain("**/XCUIElementTypeButton")
        } else {
            locator = By.xpath(".//android.widget.Button")
        }

        if (childElementExists(scrollViewElement, locator)) {
            return waitForChildElementToBePresent(scrollViewElement, locator).isDisplayed
        }

        return false
    }

    private fun checkScrollView1NewTexts() {

        val text1 = "Horizontal"
        val text2 = "Click to see the new text in horizontal"
        val text3 = "Click to see the text change, rotate and scroll horizontally"

        var textElementsTemp = getScrollViewChildTextElements(scrollViewElement1!!)
        var textElement1 = textElementsTemp.elementAt(0)
        var textElement2 = textElementsTemp.elementAt(1)
        var textElement3 = textElementsTemp.elementAt(2)
        val locationTemp = Point(scrollViewElement1!!.location.x + scrollViewElement1!!.size.width,
            scrollViewElement1!!.location.y + scrollViewElement1!!.size.height)

        // ScrollView 1 original state: three elements showing but the button
        Assert.assertEquals(text1, textElement1.text)
        Assert.assertEquals(text2, textElement2.text)
        Assert.assertEquals(text3, textElement3.text)
        Assert.assertFalse(isButtonShowingInsideOfScrollView(scrollViewElement1!!))

        // ScrollView 1 second state: new texts showing instead of text 2 and 3
        textElement2.click()
        Assert.assertFalse(scrollViewChildElementTextExists(scrollViewElement1!!, text2))
        textElement2 = getScrollViewChildTextElements(scrollViewElement1!!).elementAt(1)
        Assert.assertTrue(textElement2.text.startsWith(NEW_TEXT_PREFIX))
        Assert.assertTrue(textElement2.text.endsWith(NEW_TEXT_SUFFIX))

        if (SuiteSetup.isIos()) {
            AppiumUtil.iosScrollInsideElement(getDriver(), scrollViewElement1!!, SwipeDirection.RIGHT)
        } else {
            AppiumUtil.androidScrollToElementByText(getDriver(), 1, text2, isHorizontalScroll = true)
        }

        textElement3.click()
        Assert.assertFalse(scrollViewChildElementTextExists(scrollViewElement1!!, text3))
        textElement3 = getScrollViewChildTextElements(scrollViewElement1!!).elementAt(0)
        Assert.assertTrue(textElement3.text.startsWith(NEW_TEXT_PREFIX))
        Assert.assertTrue(textElement3.text.endsWith(NEW_TEXT_SUFFIX))

        // ScrollView 1 third state: scrolls until the end
        if (SuiteSetup.isIos()) {
            AppiumUtil.iosScrollInsideElement(getDriver(), scrollViewElement1!!, SwipeDirection.RIGHT)
        } else {
            AppiumUtil.androidScrollToElementByText(getDriver(), 1, "horizontal scroll", isHorizontalScroll = true)
        }

    }

    private fun getScrollViewElement(scrollViewElementNumber: Int): MobileElement? {
        var locator: By?

        when (scrollViewElementNumber) {
            1 -> {
                if (SuiteSetup.isIos()) {
                    locator =
                        MobileBy.iOSClassChain("**/XCUIElementTypeScrollView/**/XCUIElementTypeScrollView[\$type == 'XCUIElementTypeTextView' AND value == 'Horizontal'\$]")
                } else {
                    locator =
                        By.xpath("//android.widget.ScrollView//android.widget.HorizontalScrollView[.//android.widget.TextView[@text='Horizontal']]")
                }
            }
//            2 -> {
//                if (SuiteSetup.isIos()) {
//
//                } else {
//
//                }
//            }
//            3 -> {
//                if (SuiteSetup.isIos()) {
//
//                } else {
//
//                }
//            }
            else -> {
                throw Exception("Invalid ScrollView number")
            }
        }

        return waitForElementToBePresent(locator)

    }

    private fun getScrollViewChildTextElementByText(
        scrollViewElement: MobileElement,
        elementTextQuery: String
    ): MobileElement {
        var locator: By?

        if (SuiteSetup.isIos()) {
            locator =
                MobileBy.iOSClassChain("**/XCUIElementTypeTextView[`value == \"$elementTextQuery\"`]")
        } else {
            locator =
                By.xpath(".//android.widget.TextView[@text='$elementTextQuery']")
        }

        return waitForChildElementToBePresent(scrollViewElement, locator)
    }

    private fun getScrollViewChildTextElements(
        scrollViewElement: MobileElement
    ): Collection<MobileElement> {
        var locator: By?

        if (SuiteSetup.isIos()) {
            locator =
                MobileBy.iOSClassChain("**/XCUIElementTypeTextView")
        } else {
            locator =
                By.xpath(".//android.widget.TextView")
        }

        return waitForChildrenElementsToBePresent(scrollViewElement, locator)
    }

    private fun scrollViewChildElementTextExists(
        scrollViewElement: MobileElement,
        elementTextQuery: String
    ): Boolean {
        var locator: By?

        if (SuiteSetup.isIos()) {
            locator =
                MobileBy.iOSClassChain("**/XCUIElementTypeTextView[`value == \"$elementTextQuery\"`]")
        } else {
            locator =
                By.xpath(".//android.widget.TextView[@text='$elementTextQuery']")
        }

        return childElementExists(scrollViewElement, locator)
    }


}