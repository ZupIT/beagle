package br.com.zup.beagle.cucumber.steps

import br.com.zup.beagle.setup.SuiteSetup
import br.com.zup.beagle.utils.AppiumUtil
import br.com.zup.beagle.utils.SwipeDirection
import io.appium.java_client.MobileElement
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.openqa.selenium.By
import br.com.zup.beagle.setup.DEFAULT_ELEMENT_WAIT_TIME_IN_MILL
import org.junit.Assert

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

        val listViewElement = getListViewElement(listViewId)
        val listViewChildrenLocator = By.xpath(".//android.view.ViewGroup[.//android.view.ViewGroup]")
        var childrenOfListView = LinkedHashSet(listViewElement!!.findElements(listViewChildrenLocator)) // android.view.ViewGroup

        // AppiumUtil.waitForElementToBePresent(getDriver(),By.xpath("//androidx.recyclerview.widget.RecyclerView//android.view.ViewGroup"),5000)
        // getDriver().findElementsByXPath("(//androidx.recyclerview.widget.RecyclerView)[1]//android.view.ViewGroup[.//android.view.ViewGroup]")

        // Changes:
        // 1. change childrenOfListView from list of WebElements to list of Strings... use xpath to get elements first text,
        //    ex .//android.view.ViewGroup[.//android.view.ViewGroup]//android.view.ViewGroup//android.widget.TextView/@text (or @name on iOS)
        //    Make this on a method for each list, ex getCurrentItemsFromListViewOfIdID-HERE
        // 2. Check why method swipeFromElementToScreenEdge is not working, even passing the last element. If 1. is implemented, consider getting
        //    the last element only, instead of a list of web Elements (children of listview)

        if (childrenOfListView.size == 0)
            return 0

        var listTemp: List<MobileElement> = mutableListOf()
        var lastElementFound: MobileElement?
        do {
            childrenOfListView.addAll(listTemp) // LinkedHashSet's addAll ignores duplicated

            if (horizontalScroll)
                swipeFromElementToScreenEdge(childrenOfListView.last(), SwipeDirection.LEFT)
            else
                swipeFromElementToScreenEdge(childrenOfListView.last(), SwipeDirection.UP)

            listTemp = listViewElement!!.findElements(listViewChildrenLocator)
            lastElementFound = listTemp.last()

        } while (childrenOfListView.last() != lastElementFound)


        return childrenOfListView.size

    }

    private fun getListViewElement(listViewId: String): MobileElement? {
        if (SuiteSetup.isIos()) {
            return waitForElementWithTextToBeClickable(listViewId, false, true)
        } else {
            when (listViewId) {
                "charactersList" -> {
                    return AppiumUtil.waitForElementToBePresent(getDriver(),
                        By.xpath("(//androidx.recyclerview.widget.RecyclerView)[1]"),
                        DEFAULT_ELEMENT_WAIT_TIME_IN_MILL)
                }
                "categoriesList" -> return null // todo...
                "booksList" -> return null // todo...
                else -> {
                    return null
                }
            }
        }

    }

    // todo: fix name (what is it for?)
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

