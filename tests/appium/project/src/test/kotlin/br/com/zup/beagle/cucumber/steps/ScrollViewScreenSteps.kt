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


    @When("^I confirm that the ScrollView 1 is not showing a button with text \"(.*)\" by default$")
    fun confirmScrollView1IsNotShowingButton(buttonText: String) {
        scrollViewElement1 = getScrollViewElement(1)
        Assert.assertFalse(isButtonShowingInsideOfScrollView(scrollViewElement1!!, buttonText))

    }

    @When("^I confirm that the ScrollView 2 is showing a button with text \"(.*)\" by default$")
    fun confirmScrollView2IsShowingButton(buttonText: String) {
        scrollViewElement2 = getScrollViewElement(2)
        Assert.assertTrue(isButtonShowingInsideOfScrollView(scrollViewElement2!!, buttonText))

    }

    @When("^I expand all the items of ScrollView (.*), checking their new values$")
    fun checkScrollViewNewTexts(scrollViewElementNumber: Int) {
        when (scrollViewElementNumber) {
            1 -> checkScrollView1NewTexts()
            2 -> checkScrollView2NewTexts()
            else -> checkScrollView3NewTexts()
        }
    }

    @Then("^I should view a button with text \"(.*)\" by scrolling ScrollView (.*) to the end$")
    fun checkButtonVisible(buttonText: String, scrollViewElementNumber: Int) {
        when (scrollViewElementNumber) {
            1 -> Assert.assertTrue(isButtonShowingInsideOfScrollView(scrollViewElement1!!, buttonText))
            2 -> Assert.assertTrue(isButtonShowingInsideOfScrollView(scrollViewElement2!!, buttonText))
            else -> throw Exception("Invalid ScrollView number. Only 1 and 2 are possible for this verification")
        }
    }

    private fun checkScrollView1NewTexts() {

        val text1 = "Horizontal"
        val text2 = "Click to see the new text in horizontal"
        val text3 = "Click to see the text change, rotate and scroll horizontally"

        var textElementsTemp = getScrollViewChildTextElements(scrollViewElement1!!)
        var textElement1 = textElementsTemp.elementAt(0)
        var textElement2 = textElementsTemp.elementAt(1)
        var textElement3 = textElementsTemp.elementAt(2)

        // ScrollView 1 original state: three text elements showing
        Assert.assertEquals(text1, textElement1.text)
        Assert.assertEquals(text2, textElement2.text)
        Assert.assertEquals(text3, textElement3.text)

        // ScrollView 1 second state: new texts showing instead of text 2 and 3
        textElement2.click()
        Assert.assertFalse(scrollViewChildElementTextExists(scrollViewElement1!!, text2))
        textElement2 = getScrollViewChildrenTextElementByText(scrollViewElement1!!, NEW_TEXT_PREFIX).last()
        Assert.assertTrue(textElement2.text.startsWith(NEW_TEXT_PREFIX))
        Assert.assertTrue(textElement2.text.endsWith(NEW_TEXT_SUFFIX))

        if (SuiteSetup.isIos()) {
            AppiumUtil.iosScrollInsideElement(getDriver(), scrollViewElement1!!, SwipeDirection.RIGHT)
        } else {
            AppiumUtil.androidScrollToElementByText(getDriver(), 1, text2, isHorizontalScroll = true)
        }

        textElement3.click()
        Assert.assertFalse(scrollViewChildElementTextExists(scrollViewElement1!!, text3))
        textElement3 = getScrollViewChildrenTextElementByText(scrollViewElement1!!, NEW_TEXT_PREFIX).last()
        Assert.assertTrue(textElement3.text.startsWith(NEW_TEXT_PREFIX))
        Assert.assertTrue(textElement3.text.endsWith(NEW_TEXT_SUFFIX))

        // ScrollView 1 third state: scrolls until the end
        if (SuiteSetup.isIos()) {
            AppiumUtil.iosScrollInsideElement(getDriver(), scrollViewElement1!!, SwipeDirection.RIGHT)
        } else {
            AppiumUtil.androidScrollToElementByText(getDriver(), 1, "horizontal scroll", isHorizontalScroll = true)
        }

    }

    private fun checkScrollView2NewTexts() {

        val text1 = "Vertical"
        val text2 = "Click to see the new text in vertical"
        val text3 = "Click to see the text change, rotate and scroll vertically"

        var textElementsTemp = getScrollViewChildTextElements(scrollViewElement2!!)
        var textElement1 = textElementsTemp.elementAt(0)
        var textElement2 = textElementsTemp.elementAt(1)
        var textElement3 = textElementsTemp.elementAt(2)


        // ScrollView 1 original state: four text elements showing
        Assert.assertEquals(text1, textElement1.text)
        Assert.assertEquals(text2, textElement2.text)
        Assert.assertEquals(text3, textElement3.text)

        // ScrollView 2 second state: new texts showing instead of text 2 and 3
        textElement2.click()
        Assert.assertFalse(scrollViewChildElementTextExists(scrollViewElement2!!, text2))
        textElement2 = getScrollViewChildrenTextElementByText(scrollViewElement2!!, NEW_TEXT_PREFIX).last()
        Assert.assertTrue(textElement2.text.startsWith(NEW_TEXT_PREFIX))
        Assert.assertTrue(textElement2.text.endsWith(NEW_TEXT_SUFFIX))

        // Scrolls to text3
        if (SuiteSetup.isIos()) {
            AppiumUtil.iosScrollInsideElement(getDriver(), scrollViewElement2!!, SwipeDirection.DOWN)
        } else {
            AppiumUtil.androidScrollToElementByText(
                getDriver(),
                0,
                text3,
                isHorizontalScroll = false
            )
        }

        textElement3.click()
        Assert.assertFalse(scrollViewChildElementTextExists(scrollViewElement2!!, text3))
        textElement3 = getScrollViewChildrenTextElementByText(scrollViewElement2!!, NEW_TEXT_PREFIX).last()
        Assert.assertTrue(textElement3.text.startsWith(NEW_TEXT_PREFIX))
        Assert.assertTrue(textElement3.text.endsWith(NEW_TEXT_SUFFIX))

        // ScrollView 2 third state: scrolls until the end
        if (SuiteSetup.isIos()) {
            AppiumUtil.iosScrollInsideElement(getDriver(), scrollViewElement2!!, SwipeDirection.DOWN)
        } else {
            AppiumUtil.androidScrollToElementByText(getDriver(), 0, "vertical scroll", isHorizontalScroll = false)
        }

    }

    private fun checkScrollView3NewTexts() {

//        val text1 = "Vertical"
//        val text2 = "Click to see the new text in vertical"
//        val text3 = "Click to see the text change, rotate and scroll vertically"
//
//        var textElementsTemp = getScrollViewChildTextElements(scrollViewElement2!!)
//        var textElement1 = textElementsTemp.elementAt(0)
//        var textElement2 = textElementsTemp.elementAt(1)
//        var textElement3 = textElementsTemp.elementAt(2)
//
//
//        // ScrollView 1 original state: four elements showing, including the button
//        Assert.assertEquals(text1, textElement1.text)
//        Assert.assertEquals(text2, textElement2.text)
//        Assert.assertEquals(text3, textElement3.text)
//        Assert.assertTrue(isButtonShowingInsideOfScrollView(scrollViewElement2!!))
//
//        // ScrollView 2 second state: new texts showing instead of text 2 and 3
//        textElement2.click()
//        Assert.assertFalse(scrollViewChildElementTextExists(scrollViewElement2!!, text2))
//        textElement2 = getScrollViewChildrenTextElementByText(scrollViewElement2!!, NEW_TEXT_PREFIX).last()
//        Assert.assertTrue(textElement2.text.startsWith(NEW_TEXT_PREFIX))
//        Assert.assertTrue(textElement2.text.endsWith(NEW_TEXT_SUFFIX))
//
//        // Scrolls to text3
//        if (SuiteSetup.isIos()) {
//            AppiumUtil.iosScrollInsideElement(getDriver(), scrollViewElement2!!, SwipeDirection.DOWN)
//        } else {
//            AppiumUtil.androidScrollToElementByText(
//                getDriver(),
//                0,
//                text3,
//                isHorizontalScroll = false
//            )
//        }
//
//        textElement3.click()
//        Assert.assertFalse(scrollViewChildElementTextExists(scrollViewElement2!!, text3))
//        textElement3 = getScrollViewChildrenTextElementByText(scrollViewElement2!!, NEW_TEXT_PREFIX).last()
//        Assert.assertTrue(textElement3.text.startsWith(NEW_TEXT_PREFIX))
//        Assert.assertTrue(textElement3.text.endsWith(NEW_TEXT_SUFFIX))
//
//        // ScrollView 2 third state: scrolls until the end
//        if (SuiteSetup.isIos()) {
//            AppiumUtil.iosScrollInsideElement(getDriver(), scrollViewElement2!!, SwipeDirection.DOWN)
//        } else {
//            AppiumUtil.androidScrollToElementByText(getDriver(), 0, "vertical scroll", isHorizontalScroll = false)
//        }

    }

    private fun getScrollViewElement(scrollViewElementNumber: Int): MobileElement? {
        var locator: By?

        when (scrollViewElementNumber) {
            1 -> {
                locator = if (SuiteSetup.isIos()) {
                    MobileBy.iOSClassChain("**/XCUIElementTypeScrollView/**/XCUIElementTypeScrollView[\$type == 'XCUIElementTypeTextView' AND value == 'Horizontal'\$]")
                } else {
                    By.xpath("//android.widget.ScrollView//android.widget.HorizontalScrollView[.//android.widget.TextView[@text='Horizontal']]")
                }
            }
            2 -> {
                locator = if (SuiteSetup.isIos()) {
                    MobileBy.iOSClassChain("**/XCUIElementTypeScrollView/**/XCUIElementTypeScrollView[\$type == 'XCUIElementTypeTextView' AND value == 'Vertical'\$]")
                } else {
                    By.xpath("//android.widget.ScrollView//android.widget.ScrollView[.//android.widget.TextView[@text='Vertical']]")
                }
            }
            3 -> {
                locator = if (SuiteSetup.isIos()) {
                    MobileBy.iOSClassChain("**/XCUIElementTypeScrollView/**/XCUIElementTypeScrollView[\$type == 'XCUIElementTypeTextView' AND value == 'Vertical scroll within scroll'\$]")
                } else {
                    By.xpath("//android.widget.ScrollView//android.widget.ScrollView[.//android.widget.TextView[@text='Vertical scroll within scroll']]")
                }
            }
            else -> {
                throw Exception("Invalid ScrollView number")
            }
        }

        return waitForElementToBePresent(locator)

    }

    private fun getScrollViewChildrenTextElementByText(
        scrollViewElement: MobileElement,
        elementTextQuery: String
    ): Collection<MobileElement> {
        var locator: By?

        if (SuiteSetup.isIos()) {
            locator =
                MobileBy.iOSClassChain("**/XCUIElementTypeTextView[`value BEGINSWITH \"$elementTextQuery\"`]")
        } else {
            locator =
                By.xpath(".//android.widget.TextView[starts-with(@text,'$elementTextQuery')]")
        }

        return waitForChildrenElementsToBePresent(scrollViewElement, locator)
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

    private fun isButtonShowingInsideOfScrollView(scrollViewElement: MobileElement, buttonText: String): Boolean {
        var locator: By

        if (SuiteSetup.isIos()) {
            locator = MobileBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"$buttonText\"`]")
        } else {
            locator = By.xpath(".//android.widget.Button[@text='$buttonText']")
        }

        if (childElementExists(scrollViewElement, locator)) {
            return waitForChildElementToBePresent(scrollViewElement, locator).isDisplayed
        }
// waitForChildElementToBePresent(scrollViewElement, MobileBy.iOSClassChain("**/XCUIElementTypeButton[`value == \"$buttonText\"`]"))
        return false
    }


}