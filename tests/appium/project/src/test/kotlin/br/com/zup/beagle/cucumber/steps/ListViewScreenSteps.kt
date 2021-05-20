package br.com.zup.beagle.cucumber.steps

import br.com.zup.beagle.setup.SuiteSetup
import br.com.zup.beagle.utils.SwipeDirection
import io.appium.java_client.MobileBy
import io.appium.java_client.MobileElement
import io.appium.java_client.ios.IOSElement
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert
import org.openqa.selenium.By
import java.util.*
import kotlin.collections.LinkedHashSet


/**
 *
 * For the Categories ListView tests, some methods include in its name references to lists of Type A or C. Refer to
 * the drawing bellow to understand these references.
 *
 *  categoriesListViewA [listView]
 *         |
 *         |-------------> Title ...
 *         |-------------> categoryListViewB1 [listView]
 *         |                      |
 *         |                      |------------> Title ...
 *         |                      |------------> Author ...
 *         |                      |------------> CharactersListView [listView]
 *         |                      |                    |
 *         |                      |                    |-------------> Character 1
 *         |                      |                    |-------------> Character 2
 *         |                      |                    |-------------> Character 3
 *         |                      |                    | ...
 *         |                      |
 *         |                      |
 *         |                      |------------> Title ...
 *         |                      |------------> Author ...
 *         |                      |------------> CharactersListView [listView]
 *         |                      |                    |
 *         |                      |                    |-------------> Character 1
 *         |                      |                    |-------------> Character 2
 *         |                      |                    | ...
 *         |                      | ....
 *         |
 *         |
 *         |-------------> Title ...
 *         |-------------> categoryListViewB2 [listView]
 *         |                      |
 *         |                      | ...
 *         |
 *         |
 *         |-------------> Title ...
 *         |-------------> categoryListViewB3 [listView]
 *         |                      |
 *         |                      | ...
 *         |
 *         | ...
 */

class ListViewScreenSteps : AbstractStep() {

    override var bffRelativeUrlPath = "/listview"

    /**
     * Caching
     */
    data class CategoryListViewItem(
        val title: String,
        val author: String,
        val characters: List<String>
    )

    companion object {
        val charactersListPage1 = LinkedHashSet<String>()
        val charactersListPage2 = LinkedHashSet<String>()
        val categoriesList = LinkedHashSet<LinkedHashSet<CategoryListViewItem>>()
    }


    @Before("@listView")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the listView screen$")
    fun checkListViewScreen() {
        waitForElementWithTextToBeClickable("Beagle ListView", likeSearch = false, ignoreCase = true)
    }

    @When("^I scroll left the listView with id charactersList on pagination (.*)$")
    fun cacheCharacterListPage1(paginationNumber: Int) {
        val listElement = getListViewElement("charactersList")
        val listElements = extractAllItemsOfListViewCharactersList(listElement!!, horizontalScroll = true)!!
        when (paginationNumber) {
            1 -> charactersListPage1.addAll(listElements)
            else -> charactersListPage2.addAll(listElements)
        }
    }

    @Then("^the listView with id charactersList on pagination (.*) should have exactly (.*) items$")
    fun countListViewCharactersListPage1(paginationNumber: Int, expectedItemCount: Int) {
        when (paginationNumber) {
            1 -> Assert.assertEquals(charactersListPage1.size, expectedItemCount)
            else -> Assert.assertEquals(charactersListPage2.size, expectedItemCount)
        }
    }

    @And("^the values of the listView with id charactersList on pagination (.*) should be:$")
    fun checkValuesOfListViewCharactersListPage1(paginationNumber: Int, dataTable: DataTable) {
        val rows: List<List<String?>> = dataTable.asLists(String::class.java)
        val cachedList = when (paginationNumber) {
            1 -> charactersListPage1.toList()
            else -> charactersListPage2.toList()
        }
        for ((lineCount, columns) in rows.withIndex()) {
            val childText = cachedList[lineCount].split(";")
            Assert.assertEquals(columns[0]!!.trim(), childText[0].trim()) // name
            Assert.assertEquals(columns[1]!!.trim(), childText[1].trim()) // book
            Assert.assertEquals(columns[2]!!.trim(), childText[2].trim()) // collection
        }
    }

    @And("^the listView with id charactersList should be on pagination 1$")
    fun checkIfListViewCharactersListIsInPage1() {
        var currentFirstChildText =
            extractContentOfChildOfListViewCharactersList(getFirstChildOfListView(getListViewElement("charactersList")!!))
        Assert.assertEquals(charactersListPage1.toList()[0], currentFirstChildText)
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

    @Then("^the screen should show text: (.*)$")
    fun checkScreenDisplaysText(expectedText: String) {
        waitForElementWithTextToBeClickable(expectedText, likeSearch = false, ignoreCase = false)
    }

    @When("^I read all the elements of the listView with id categoriesList$")
    fun getAllElementsOfListViewCategoriesList() {
        val listViewElement = getListViewElement("categoriesList")
        categoriesList.addAll(extractAllItemsOfCategoriesListViewOfTypeA(listViewElement!!, horizontalScroll = true)!!)
    }

    @Then("^the listView with id categoriesList should have exactly (.*) items$")
    fun countListViewCategoriesList(itemsCount: Int) {
        Assert.assertEquals(itemsCount, categoriesList.size)
    }

    @And("^the values of the listView with id categoriesList and its items should be:$")
    fun checkValuesOfListViewCategoriesList(dataTable: DataTable) {
        val rows: List<List<String?>> = dataTable.asLists(String::class.java)
        for ((lineCount, columns) in rows.withIndex()) {
            // parei aqui
//            Assert.assertEquals(columns[0]!!.trim(), childText[0].trim()) // name
//            Assert.assertEquals(columns[1]!!.trim(), childText[1].trim()) // book
//            Assert.assertEquals(columns[2]!!.trim(), childText[2].trim()) // collection
        }
    }

    // TODO: keep this method?
    fun isListViewHorizontal(listViewId: String): Boolean {
        val listViewElement = getListViewElement(listViewId)
        return when (listViewId) {
            "charactersList" -> isListViewCharactersListHorizontal(listViewElement!!)
            else -> {
                false
            }
        }
    }

    // TODO: keep this method?
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

    /**
     * Scrolls the list of id categoriesList to read all of its elements and return them parsed
     */
    private fun extractAllItemsOfCategoriesListViewOfTypeA(
        listViewElement: MobileElement,
        horizontalScroll: Boolean
    ): Collection<LinkedHashSet<CategoryListViewItem>>? {

        var childrenOfCategoriesListViewOfTypeA = getChildrenOfCategoriesListViewOfTypeA(listViewElement)

        if (childrenOfCategoriesListViewOfTypeA.isEmpty())
            return null

        val allItems = LinkedHashSet<LinkedHashSet<CategoryListViewItem>>()
        do {

            for (childOfCategoriesListViewOfTypeA in childrenOfCategoriesListViewOfTypeA) {
                allItems.add(extractAllItemsOfACategoriesListViewOfTypeB(childOfCategoriesListViewOfTypeA)!!)
            }

            var lastChildElement = childrenOfCategoriesListViewOfTypeA.last()
            var lastChildElementTitle = extractTitleOfChildOfCategoriesListViewOfTypeA(lastChildElement)!!


            if (horizontalScroll)
                scrollFromOnePointToBorder(lastChildElement.location, SwipeDirection.LEFT)
            else
                scrollFromOnePointToBorder(lastChildElement.location, SwipeDirection.UP)

            childrenOfCategoriesListViewOfTypeA = getChildrenOfListView(listViewElement)


        } while (lastChildElementTitle != extractTitleOfChildOfCategoriesListViewOfTypeA(
                childrenOfCategoriesListViewOfTypeA.last()
            )
        )

        return allItems
    }


    private fun extractAllItemsOfACategoriesListViewOfTypeB(childOfCategoriesListViewOfTypeA: MobileElement): LinkedHashSet<CategoryListViewItem>? {

        // extracts the listView of type B since childOfCategoriesListViewOfTypeA is not the list B itself
        val categoriesListViewOfTypeBElement = getListViewElement(childOfCategoriesListViewOfTypeA)

        var childrenElementsOfCategoriesListOfTypeB =
            getChildrenElementsOfCategoriesListViewOfTypeB(categoriesListViewOfTypeBElement!!)

        if (childrenElementsOfCategoriesListOfTypeB.isEmpty())
            return null

        val allItems = LinkedHashSet<CategoryListViewItem>()
        do {

            var parsedChildrenElementsTemp =
                parseElementsToCategoryListViewItems(childrenElementsOfCategoriesListOfTypeB)
            var lastChildElement = childrenElementsOfCategoriesListOfTypeB.last()

            for (parsedItem in parsedChildrenElementsTemp!!) {
                allItems.add(parsedItem)
            }

            scrollFromOnePointToBorder(lastChildElement.location, SwipeDirection.LEFT)

            childrenElementsOfCategoriesListOfTypeB = getChildrenOfListView(categoriesListViewOfTypeBElement!!)

        } while (parsedChildrenElementsTemp!!.last() != parseElementToCategoryListViewItem(
                childrenElementsOfCategoriesListOfTypeB.last()
            )
        )

        return allItems
    }

    /**
     * Parse the contents of a given category list of type C to a CategoryListViewItem object
     */
    private fun parseElementsToCategoryListViewItems(categoriesListViewOfTypeCElements: List<MobileElement>): LinkedHashSet<CategoryListViewItem>? {
        val list = LinkedHashSet<CategoryListViewItem>()
        for (element in categoriesListViewOfTypeCElements)
            list.add(parseElementToCategoryListViewItem(element)!!)
        return list
    }

    /**
     * Parse the contents of a given cell, of a category list of type C, to a CategoryListViewItem object
     */
    private fun parseElementToCategoryListViewItem(categoriesListViewOfTypeCElement: MobileElement): CategoryListViewItem? {
        lateinit var categoryListViewItemTemp: CategoryListViewItem
        if (SuiteSetup.isIos()) {
            // TODO
        } else {

            var title =
                waitForChildElementToBePresent(
                    categoriesListViewOfTypeCElement,
                    By.xpath("(.//android.view.ViewGroup//android.view.ViewGroup//android.widget.TextView)[1]")
                ).text


            var author =
                waitForChildElementToBePresent(
                    categoriesListViewOfTypeCElement,
                    By.xpath("(.//android.view.ViewGroup//android.view.ViewGroup//android.widget.TextView)[2]")
                ).text


            var characters = extractCharactersListViewValues(
                waitForChildElementToBePresent(
                    categoriesListViewOfTypeCElement,
                    By.xpath(".//android.view.ViewGroup//android.view.ViewGroup//androidx.recyclerview.widget.RecyclerView")
                )

            )

            categoryListViewItemTemp = CategoryListViewItem(title = title, author = author, characters = characters!!)
        }

        return categoryListViewItemTemp
    }

    /**
     * Extracts all the values from a given CharactersListView
     */
    private fun extractCharactersListViewValues(charactersListViewElement: MobileElement): List<String>? {
        val characterNamesList = LinkedList<String>()
        val elementsOfList = getChildrenOfListView(charactersListViewElement)

        for (element in elementsOfList) {
            if (SuiteSetup.isIos()) {
                // TODO
            } else {
                characterNamesList.add(
                    waitForChildElementToBePresent(
                        element,
                        By.xpath(".//android.view.ViewGroup//android.widget.TextView")
                    ).text
                )
            }
        }
        return characterNamesList
    }

    /**
     * Returns the title of an item of the main Categories ListView.
     * Title values are 'Fantasy', 'Sci-fi' or 'Other'
     */
    private fun extractTitleOfChildOfCategoriesListViewOfTypeA(childOfCategoriesListViewOfTypeAElement: MobileElement): String? {

        var title: String? = null
        if (SuiteSetup.isIos()) {
            // TODO
        } else {
            title =
                waitForChildElementToBePresent(
                    childOfCategoriesListViewOfTypeAElement,
                    By.xpath(".//android.view.ViewGroup//android.widget.TextView")
                ).text
        }
        return title
    }

    /**
     * Returns the children elements of the main CategoriesListView list. These elements refer only to elements showing
     * on the screen.
     */
    private fun getChildrenOfCategoriesListViewOfTypeA(categoriesListViewOfTypeAElement: MobileElement): List<MobileElement> {

        var childrenOfCategoryListViewOfTypeALocator: By? = null

        if (SuiteSetup.isIos()) {
            // TODO: fix this locator using ids!
            childrenOfCategoryListViewOfTypeALocator =
                By.xpath(".//XCUIElementTypeCell[.//XCUIElementTypeOther//XCUIElementTypeOther//XCUIElementTypeOther]")

        } else {
            childrenOfCategoryListViewOfTypeALocator =
                By.xpath(
                    ".//android.view.ViewGroup[.//android.view.ViewGroup//androidx.recyclerview.widget.RecyclerView" +
                            "//android.view.ViewGroup//android.view.ViewGroup//androidx.recyclerview.widget.RecyclerView]"
                )
        }

        return waitForChildrenElementsToBePresent(
            categoriesListViewOfTypeAElement,
            childrenOfCategoryListViewOfTypeALocator
        )
    }

    /**
     * Returns the children elements of a Categories ListView of type B. These elements refer only to elements showing
     * on the screen.
     */
    private fun getChildrenElementsOfCategoriesListViewOfTypeB(categoriesListViewOfTypeBElement: MobileElement): List<MobileElement> {

        var childrenOfCategoryListViewOfTypeBLocator: By? = null

        if (SuiteSetup.isIos()) {
            // TODO: fix this locator using ids!
            childrenOfCategoryListViewOfTypeBLocator =
                By.xpath(".//XCUIElementTypeCell[.//XCUIElementTypeOther//XCUIElementTypeOther//XCUIElementTypeOther]")

        } else {
            childrenOfCategoryListViewOfTypeBLocator =
                By.xpath(
                    ".//android.view.ViewGroup[.//android.view.ViewGroup//androidx.recyclerview.widget.RecyclerView]"
                )
        }

        return waitForChildrenElementsToBePresent(
            categoriesListViewOfTypeBElement,
            childrenOfCategoryListViewOfTypeBLocator
        )
    }

    /**
     * Scrolls the list of id charactersList to read all of its elements and return them parsed
     */
    private fun extractAllItemsOfListViewCharactersList(
        listViewElement: MobileElement,
        horizontalScroll: Boolean
    ): Collection<String>? {
        var childrenNames =
            LinkedHashSet(getChildrenNamesOfListViewCharactersList(listViewElement!!)) // ignores identical values
        var lastChildElement: MobileElement
        var lastChildName = ""

        if (childrenNames.isEmpty())
            return null

        var childrenNamesTemp: List<String> = mutableListOf()
        do {
            lastChildElement = getLastChildOfListView(listViewElement)
            lastChildName = extractContentOfChildOfListViewCharactersList(lastChildElement)!!

            if (horizontalScroll)
                scrollFromOnePointToBorder(lastChildElement.location, SwipeDirection.LEFT)
            else
                scrollFromOnePointToBorder(lastChildElement.location, SwipeDirection.UP)

            childrenNamesTemp = getChildrenNamesOfListViewCharactersList(listViewElement!!)

            childrenNames.addAll(childrenNamesTemp)


        } while (lastChildName != childrenNamesTemp.last())

        return childrenNames
    }

    /**
     * Returns the first listView element that is a child of a given element.
     */
    private fun getListViewElement(parentElement: MobileElement): MobileElement? {
        if (SuiteSetup.isIos()) {
            // TODO
            return null
        } else {
            return waitForChildElementToBePresent(
                parentElement,
                By.xpath("(.//android.view.ViewGroup//android.view.ViewGroup//androidx.recyclerview.widget.RecyclerView)[1]")
            )
        }
    }

    private fun getListViewElement(listViewId: String): MobileElement? {
        if (SuiteSetup.isIos()) {
            when (listViewId) {
                "charactersList" -> {
                    return waitForElementToBePresent(MobileBy.id("charactersList"))
                }
                "categoriesList" -> return null // todo...
                else -> {
                    return null
                }
            }
        } else {
            when (listViewId) {
                "charactersList" -> {
                    return waitForElementToBePresent(By.xpath("(//androidx.recyclerview.widget.RecyclerView)[1]"))
                }
                "categoriesList" -> {
                    return waitForElementToBePresent(By.xpath("(//androidx.recyclerview.widget.RecyclerView)[2]"))
                }
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
            childrenNames.add(extractContentOfChildOfListViewCharactersList(it)!!)
        }

        return childrenNames
    }

    /**
     * Returns a text representing the content of a child of the list view charactersList, thus
     * helping identifying the child element.
     */
    private fun extractContentOfChildOfListViewCharactersList(childElement: MobileElement): String? {
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
            var element1 = waitForChildElementToBePresent(
                childElement,
                By.xpath("(.//android.view.ViewGroup//android.widget.TextView)[1]")
            )
            // childElement.findElementByXPath("(.//android.view.ViewGroup//android.widget.TextView)[1]")
            // book
            var element2 = waitForChildElementToBePresent(
                childElement,
                By.xpath("(.//android.view.ViewGroup//android.widget.TextView)[2]")
            )
            // childElement.findElementByXPath("(.//android.view.ViewGroup//android.widget.TextView)[2]")
            // collection
            var element3 = waitForChildElementToBePresent(
                childElement,
                By.xpath("(.//android.view.ViewGroup//android.widget.TextView)[3]")
            )
            // childElement.findElementByXPath("(.//android.view.ViewGroup//android.widget.TextView)[3]")

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

        return waitForChildrenElementsToBePresent(
            listViewElement,
            lastChildOfListViewLocator
        )

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

        return waitForChildElementToBePresent(listViewElement, lastChildOfListViewLocator)
        //listViewElement.findElement(lastChildOfListViewLocator)

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

        return waitForChildElementToBePresent(listViewElement, lastChildOfListViewLocator)
        //return listViewElement.findElement(lastChildOfListViewLocator)

    }

}

