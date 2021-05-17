package br.com.zup.beagle.cucumber.steps

import br.com.zup.beagle.setup.SuiteSetup
import br.com.zup.beagle.utils.AppiumUtil
import io.appium.java_client.MobileElement
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.openqa.selenium.By
import br.com.zup.beagle.setup.DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
import br.com.zup.beagle.utils.SwipeDirection
import io.appium.java_client.MobileBy
import io.appium.java_client.android.AndroidElement
import io.appium.java_client.ios.IOSElement
import org.junit.Assert
import java.util.LinkedHashSet

class ListViewScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/listview"

    @Before("@listView")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the listView screen$")
    fun checkListViewScreen() {
        waitForElementWithTextToBeClickable("Beagle ListView", likeSearch = false, ignoreCase = true)
    }

    @Then("^listView with id (.*) should have exactly (.*) items$")
    fun checkListViewItemsCount(listViewId: String, expectedItemCount: Int) {
        Assert.assertEquals(countAllChildrenOfListView(listViewId, horizontalScroll = true), expectedItemCount)
    }

    @Then("^listView with id (.*) should be in vertical orientation$")
    fun checkListViewIsVertical(listViewId: String) {
        if (SuiteSetup.isAndroid()) // reset list state
            loadBffScreen()
        else {
            restartApp()
            loadBffScreen()
        }

        Assert.assertFalse(isListViewHorizontal(listViewId))
    }

    @Then("^listView with id (.*) should be in horizontal orientation$")
    fun checkListViewIsHorizontal(listViewId: String) {
        if (SuiteSetup.isAndroid()) // reset list state
            loadBffScreen()
        else {
            restartApp()
            loadBffScreen()
        }

        Assert.assertTrue(isListViewHorizontal(listViewId))
    }

    @When("^I scroll listView with id (.*) to position (.*)$")
    fun scrollListViewToPosition(listViewId: String, position: Int) {
        //        ScreenRobot()
        //            .scrollListToPosition(
        //                listViewId.toAndroidId(),
        //                position,
        //            )
    }

    @When("^I scroll listView with id (.*) to (.*) percent$")
    fun scrollListViewByPercent(listViewId: String, scrollPercent: Int) {
        //        ScreenRobot()
        //            .scrollListByPercent(
        //                listViewId.toAndroidId(),
        //                scrollPercent,
        //            )
    }

    @Then("^the screen should show text: (.*)$")
    fun checkScreenDisplaysText(expectedText: String) {
        waitForElementWithTextToBeClickable(expectedText, likeSearch = false, ignoreCase = false)
    }

    @Then("^listView with id (.*) at position (.*) should show text: (.*)$")
    fun checkListViewItemContainsText(
        listViewId: String,
        listViewPosition: Int,
        expectedText: String,
    ) {
        val parsedText = transform(expectedText)
        //        ScreenRobot()
        //            .checkListViewItemContainsText(
        //                listViewId.toAndroidId(),
        //                listViewPosition,
        //                expectedText,
        //            )
    }

    @Then("^listView with id (.*) at position (.*) should have a view with id (.*)$")
    fun checkListViewItemContainsViewWithId(listViewId: String, listViewPosition: Int, expectedViewId: String) {
        //        ScreenRobot()
        //            .checkListViewItemContainsViewWithId(
        //                listViewId.toAndroidId(),
        //                listViewPosition,
        //                expectedViewId.toAndroidId(),
        //            )
    }

    @Then("^I click on view with id (.*) at position (.*) of listView with id (.*)$")
    fun clickOnTextInsideListViewItem(viewId: String, position: Int, listViewId: String) {
        //        ScreenRobot()
        //            .clickOnTextInsideListViewItem(
        //                listViewId.toAndroidId(),
        //                position,
        //                viewId.toAndroidId(),
        //            )
    }

    private fun isListViewHorizontal(listViewId: String): Boolean {
        val listViewElement = getListViewElement(listViewId)
        return when (listViewId) {
            "charactersList" -> isListViewCharactersListHorizontal(listViewElement!!)
            else -> {
                false
            }
        }
    }

    /**
     * Tells if a list is horizontal by first comparing its elements' y position. When there's only one element showing,
     * then scrolls horizontally to compare with more elements
     */
    private fun isListViewCharactersListHorizontal(listViewElement: MobileElement): Boolean {

        var initialChildrenList = getChildrenOfListView(listViewElement)
        if (initialChildrenList.size > 1) {
            return initialChildrenList[0].location.y == initialChildrenList[1].location.y
        }

        scrollFromOnePointToBorder(initialChildrenList.get(0).location, SwipeDirection.LEFT)
        initialChildrenList = getChildrenOfListView(listViewElement)
        if (initialChildrenList.size > 1) {
            return initialChildrenList[0].location.y == initialChildrenList[1].location.y
        } else
            throw Exception("The given list contains only one element")
    }

    // Scrolls a given list to count all of its elements
    private fun countAllChildrenOfListView(listViewId: String, horizontalScroll: Boolean): Int {
        val listViewElement = getListViewElement(listViewId)
        return when (listViewId) {
            "charactersList" -> countChildrenOfListViewCharactersList(
                listViewElement!!,
                horizontalScroll = horizontalScroll
            )
            else -> {
                0
            }
        }
    }

    /**
     * Scrolls the list to count all of its elements
     */
    private fun countChildrenOfListViewCharactersList(listViewElement: MobileElement, horizontalScroll: Boolean): Int {
        var childrenNames =
            LinkedHashSet(getChildrenNamesOfListViewCharactersList(listViewElement!!)) // ignores identical values
        var lastChildElement: MobileElement
        var lastChildName = ""

        if (childrenNames.isEmpty())
            return 0

        var childrenNamesTemp: List<String> = mutableListOf()
        do {
            lastChildElement = getLastChildOfListView(listViewElement)
            lastChildName = getContentOfChildOfListViewCharactersList(lastChildElement)!!

            if (horizontalScroll)
                scrollFromOnePointToBorder(lastChildElement.location, SwipeDirection.LEFT)
            else
                scrollFromOnePointToBorder(lastChildElement.location, SwipeDirection.UP)

            childrenNamesTemp = getChildrenNamesOfListViewCharactersList(listViewElement!!)

            childrenNames.addAll(childrenNamesTemp)


        } while (lastChildName != childrenNamesTemp.last())

        // println(childrenNames)
        return childrenNames.size
    }

    private fun getListViewElement(listViewId: String): MobileElement? {
        if (SuiteSetup.isIos()) {
            //return waitForElementWithTextToBePresent(listViewId, false, true)
            return AppiumUtil.waitForElementToBePresent(
                getDriver(),
                MobileBy.id("charactersList"),
                DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
            )

        } else {
            when (listViewId) {
                "charactersList" -> {
                    return AppiumUtil.waitForElementToBePresent(
                        getDriver(),
                        By.xpath("(//androidx.recyclerview.widget.RecyclerView)[1]"),
                        DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
                    )
                }
                "categoriesList" -> return null // todo...
                "booksList" -> return null // todo...
                else -> {
                    return null
                }
            }
        }

    }

    /**
     * @return a list of names representing each child element of the list view charactersList
     */
    private fun getChildrenNamesOfListViewCharactersList(listViewElement: MobileElement): List<String> {
        val childrenNames = mutableListOf<String>()

        var childrenElements = getChildrenOfListView(listViewElement)
        childrenElements.forEach() {
            childrenNames.add(getContentOfChildOfListViewCharactersList(it)!!)
        }

        return childrenNames
    }

    /**
     * Returns a text representing the content of a child of the list view charactersList, thus
     * helping identifying the child element.
     */
    private fun getContentOfChildOfListViewCharactersList(childElement: MobileElement): String? {
        var childElementText: String? = null
        if (SuiteSetup.isIos()) {
            // name
            var element1 =
                (childElement as IOSElement).findElementByIosClassChain("**/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeTextView")
            // book
            var element2 =
                (childElement as IOSElement).findElementByIosClassChain("**/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeTextView[2]")
            // collection
            var element3 =
                (childElement as IOSElement).findElementByIosClassChain("**/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeTextView[3]")

            childElementText = element1.text + ";" + element2.text + ";" + element3.text
        } else {
            // name
            var element1 =
                childElement.findElementByXPath("(.//android.view.ViewGroup//android.widget.TextView)[1]")
            // book
            var element2 =
                childElement.findElementByXPath("(.//android.view.ViewGroup//android.widget.TextView)[2]")
            // collection
            var element3 =
                childElement.findElementByXPath("(.//android.view.ViewGroup//android.widget.TextView)[3]")

            childElementText = element1.text + ";" + element2.text + ";" + element3.text

        }
        return childElementText
    }

    /**
     * Locates the children elements of a list view. These elements refer only to elements showing
     * on the screen.
     */
    private fun getChildrenOfListView(listViewElement: MobileElement): List<MobileElement> {

        var lastChildOfListViewLocator: By? = null

        if (SuiteSetup.isIos()) {
            lastChildOfListViewLocator =
                By.xpath(".//XCUIElementTypeCell[.//XCUIElementTypeOther//XCUIElementTypeOther//XCUIElementTypeOther]")
        } else {
            lastChildOfListViewLocator = By.xpath(".//android.view.ViewGroup[.//android.view.ViewGroup]")
        }

        return listViewElement.findElements(lastChildOfListViewLocator)

    }

    /**
     * Locates the last child element of a list view. The child element refers only to a element showing
     * on the screen. The last element of a horizontal list will be the one most to the right, and the
     * last on a vertical list will be the one most to the bottom of the list.
     */
    private fun getLastChildOfListView(listViewElement: MobileElement): MobileElement {

        var lastChildOfListViewLocator: By? = null

        if (SuiteSetup.isIos()) {
            lastChildOfListViewLocator =
                By.xpath("(.//XCUIElementTypeCell[.//XCUIElementTypeOther//XCUIElementTypeOther//XCUIElementTypeOther])[last()]")
        } else {
            lastChildOfListViewLocator = By.xpath("(.//android.view.ViewGroup[.//android.view.ViewGroup])[last()]")
        }

        return listViewElement.findElement(lastChildOfListViewLocator)

    }

    /**
     * Locates the first child element of a list view. The child element refers only to a element showing
     * on the screen. The first element of a horizontal list will be the one most to the left, and the
     * last on a vertical list will be the one most to the top of the list.
     */
    private fun getFirstChildOfListView(listViewElement: MobileElement): MobileElement {

        var lastChildOfListViewLocator: By? = null

        if (SuiteSetup.isIos()) {
            lastChildOfListViewLocator =
                By.xpath("(.//XCUIElementTypeCell[.//XCUIElementTypeOther//XCUIElementTypeOther//XCUIElementTypeOther])[1]")
        } else {
            lastChildOfListViewLocator = By.xpath("(.//android.view.ViewGroup[.//android.view.ViewGroup])[1]")
        }

        return listViewElement.findElement(lastChildOfListViewLocator)

    }

    // TODO: fix name (what is it for?)
    fun transform(value: String?): String {
        value?.let {
            if (it.matches(Regex("'(.*?)'"))) {
                val match = Regex("'(.*?)'").find(it)!!
                val (text) = match.destructured
                return text
            }
        }
        return value ?: ""
    }
}

