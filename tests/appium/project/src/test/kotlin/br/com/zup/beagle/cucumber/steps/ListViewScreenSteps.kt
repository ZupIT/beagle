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
import io.appium.java_client.android.AndroidElement
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
        waitForElementWithTextToBeClickable("Beagle ListView", false, true)
    }

    @Then("^listView with id (.*) should have exactly (.*) items$")
    fun checkListViewItemsCount(listViewId: String, expectedItemCount: Int) {
        Assert.assertEquals(countChildrenOfListView(listViewId, true), 34)
    }

    @Then("^listView with id (.*) should be in vertical orientation$")
    fun checkListViewIsVertical(listViewId: String) {
//        ScreenRobot()
//            .checkListViewOrientation(
//                listViewId.toAndroidId(),
//                RecyclerView.VERTICAL,
//            )
    }

    @Then("^listView with id (.*) should be in horizontal orientation$")
    fun checkListViewIsHorizontal(listViewId: String) {
//        ScreenRobot()
//            .checkListViewOrientation(
//                listViewId.toAndroidId(),
//                RecyclerView.HORIZONTAL,
//            )
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

    @Then("^screen should show text: (.*)$")
    fun checkScreenDisplaysText(expectedText: String) {
//        ScreenRobot()
//            .checkViewContainsText(
//                expectedText,
//                true,
//            )
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

    private fun countChildrenOfListView(listViewId: String, horizontalScroll: Boolean): Int {

        // AppiumUtil.waitForElementToBePresent(getDriver(),By.xpath("//androidx.recyclerview.widget.RecyclerView//android.view.ViewGroup"),5000)
        // getDriver().findElementsByXPath("(//androidx.recyclerview.widget.RecyclerView)[1]//android.view.ViewGroup[.//android.view.ViewGroup]")

        val listViewElement = getListViewElement(listViewId)
        val childrenNames = LinkedHashSet(getChildrenNamesOfListView(listViewElement!!)) // ignores identical values
        var lastChildElement: MobileElement


        if (childrenNames.isEmpty())
            return 0

        var childrenNamesTemp: List<String> = mutableListOf()
        do {
            lastChildElement = getLastChildOfListView(listViewElement)

            if (horizontalScroll)
                swipeFromElementToScreenEdge(lastChildElement, SwipeDirection.LEFT)
            else
                swipeFromElementToScreenEdge(lastChildElement, SwipeDirection.UP)

            childrenNamesTemp = getChildrenNamesOfListView(listViewElement!!)
            childrenNames.addAll(childrenNamesTemp)

        } while (getChildElementText(lastChildElement) != childrenNamesTemp.last())


        return childrenNames.size

    }

    private fun getListViewElement(listViewId: String): MobileElement? {
        if (SuiteSetup.isIos()) {
            return waitForElementWithTextToBePresent(listViewId, false, true)
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
     * @return a list of names representing each child element of a list view
     */
    private fun getChildrenNamesOfListView(listViewElement: MobileElement): List<String> {
        val childrenNames = mutableListOf<String>()

        var childrenElements = getChildrenOfListView(listViewElement)
        childrenElements.forEach() {
            childrenNames.add(getChildElementText(it)!!)
        }

        return childrenNames
    }

    /**
     * This method gets the name / text property of the first element inside the child element,
     * to help identity the child element.
     */
    private fun getChildElementText(childElement: MobileElement): String? {
        var childElementText: String? = null
        if (SuiteSetup.isIos()) {
            // TODO
        } else {
            var element = childElement.findElementByXPath("(.//android.view.ViewGroup//android.widget.TextView)[1]")
            childElementText = (element as AndroidElement).text

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
            // TODO...
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

        if (SuiteSetup.isAndroid()) {
            lastChildOfListViewLocator = By.xpath("(.//android.view.ViewGroup[.//android.view.ViewGroup])[last()]")
        } else {
            // TODO...
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

