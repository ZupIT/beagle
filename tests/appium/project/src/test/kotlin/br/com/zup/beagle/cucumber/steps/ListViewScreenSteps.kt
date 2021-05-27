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
import java.util.*
import kotlin.collections.LinkedHashSet


/**
 *
 * For the Categories ListView tests, some methods include in its name references to lists of Type A or B. Refer to
 * the drawing bellow to understand these references.
 *
 *  categoriesListViewA [listView]
 *         |
 *         |-------------> Title ...
 *         |-------------> categoryListViewB1 [listView] ('Fantasy' list)
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
 *         |-------------> categoryListViewB2 [listView] ('Sci-fi' list)
 *         |                      |
 *         |                      | ...
 *         |
 *         |
 *         |-------------> Title ...
 *         |-------------> categoryListViewB3 [listView] ('Other' list)
 *         |                      |
 *         |                      | ...
 *         |
 *         |
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

    data class BooksListListViewItem(
        val title: String,
        val author: String,
        val collection: String,
        val bookNumber: String,
        val genre: String,
        val rating: String
    )

    companion object {
        val charactersListPage1 = LinkedHashSet<String>()
        val charactersListPage2 = LinkedHashSet<String>()
        val categoriesList = LinkedHashSet<LinkedHashSet<CategoryListViewItem>>()
        val booksListList = LinkedHashSet<BooksListListViewItem>()
    }


    @Before("@listView")
    fun setup() {
        loadBffScreen()
    }

    @Given("^that I'm on the listView screen$")
    fun checkListViewScreen() {
        waitForElementWithTextToBeClickable("Beagle ListView", likeSearch = false, ignoreCase = true)
    }

    @Then("^the listView with id (.*) should have exactly (.*) items$")
    fun countListViewCharactersListPage1(listViewId: String, expectedItemCount: Int) {
        when (listViewId) {
            "charactersList on pagination 1" -> Assert.assertEquals(charactersListPage1.size, expectedItemCount)
            "charactersList on pagination 2" -> Assert.assertEquals(charactersListPage2.size, expectedItemCount)
            "categoriesList" -> Assert.assertEquals(categoriesList.size, expectedItemCount)
            "category:1" -> Assert.assertEquals(categoriesList.elementAt(0).size, expectedItemCount)
            "category:2" -> Assert.assertEquals(categoriesList.elementAt(1).size, expectedItemCount)
            "category:3" -> Assert.assertEquals(categoriesList.elementAt(2).size, expectedItemCount)
            "booksList" -> Assert.assertEquals(booksListList.size, expectedItemCount)
            else -> throw Exception("List $listViewId not found!")
        }
    }

    @And("^the listView with id charactersList should be on pagination 1$")
    fun checkIfListViewCharactersListIsInPage1() {
        val childrenElementValues = getTextChildrenElementsOfListView(getListViewElement("charactersList")!!)
        val currentFirstChildElementValue =
            childrenElementValues[0].text + ";" + childrenElementValues[1].text + ";" + childrenElementValues[2].text
        Assert.assertEquals(charactersListPage1.toList()[0], currentFirstChildElementValue)
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


    @When("^I read all the elements of the listView with id (.*)$")
    fun getAllElementsOfListView(listViewId: String) {
        when (listViewId) {
            "charactersList on pagination 1" -> {
                val listElement = getListViewElement("charactersList")
                val listElements = extractAllItemsOfListViewCharactersList(listElement!!)!!
                charactersListPage1.addAll(listElements)
            }
            "charactersList on pagination 2" -> {
                val listElement = getListViewElement("charactersList")
                val listElements = extractAllItemsOfListViewCharactersList(listElement!!)!!
                charactersListPage2.addAll(listElements)
            }
            "categoriesList" -> {
                val listViewElement = getListViewElement("categoriesList")
                categoriesList.addAll(extractAllThreeListsOfCategoriesListViewOfTypeA(listViewElement!!)!!)
            }
            "booksList" -> {

                val anchorElementForSwipe = getChildrenElementsOfListView(getListViewElement("categoriesList")!!)[1]
                scrollFromOnePointToBorder(anchorElementForSwipe!!.location, SwipeDirection.UP)


                val listViewElement = getListViewElement("booksList")
                booksListList.addAll(extractAllItemsOfListViewBooksList(listViewElement!!)!!)
            }
            else -> throw Exception("List $listViewId not found!")
        }
    }

    @Then("^the values of the listView with id (.*) should be:$")
    fun checkListValues(listViewId: String, dataTable: DataTable) {
        when (listViewId) {
            "charactersList on pagination 1" -> validateCharactersListViewValues(dataTable, 1)
            "charactersList on pagination 2" -> validateCharactersListViewValues(dataTable, 2)
            "categoriesList" -> validateCategoriesListViewValues(dataTable)
            "booksList" -> validateBooksListListViewValues(dataTable)
            else -> throw Exception("List $listViewId not found!")
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

        var initialChildrenList = getChildrenElementsOfListView(listViewElement)
        if (initialChildrenList.size > 1) {
            return initialChildrenList[0].location.y == initialChildrenList[1].location.y
        }

        scrollFromOnePointToBorder(initialChildrenList.get(0).location, SwipeDirection.LEFT)
        initialChildrenList = getChildrenElementsOfListView(listViewElement)
        if (initialChildrenList.size > 1) {
            return initialChildrenList[0].location.y == initialChildrenList[1].location.y
        } else
            throw Exception("The given list contains only one element")
    }


    fun validateCharactersListViewValues(dataTable: DataTable, paginationNumber: Int) {
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

    fun validateCategoriesListViewValues(dataTable: DataTable) {
        val rows: List<List<String?>> = dataTable.asLists(String::class.java)
        var categoryListPosition: Int
        var bookListPosition: Int
        var title: String
        var author: String
        var characterListPosition: Int
        var character: String
        var categoryListViewItem: CategoryListViewItem
        var categoriesList = categoriesList.toList()

        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            categoryListPosition = columns[0]!!.toInt()
            bookListPosition = columns[1]!!.toInt()
            title = columns[2]!!
            author = columns[3]!!
            characterListPosition = columns[4]!!.toInt()
            character = columns[5]!!


            categoryListViewItem = categoriesList.elementAt(categoryListPosition).elementAt(bookListPosition)

            Assert.assertEquals(title, categoryListViewItem.title.removePrefix("Title: "))
            Assert.assertEquals(author, categoryListViewItem.author.removePrefix("Author: "))
            Assert.assertEquals(character, categoryListViewItem.characters[characterListPosition])

        }
    }

    fun validateBooksListListViewValues(dataTable: DataTable) {

        val rows: List<List<String?>> = dataTable.asLists(String::class.java)

        var booksListIndexTemp = 0
        var genreTemp = ""
        var titleTemp = ""
        var authorTemp = ""
        var collectionTemp = ""
        var bookNumberTemp = ""
        var ratingTemp = ""
        var booksListItemTemp: BooksListListViewItem

        for ((lineCount, columns) in rows.withIndex()) {

            if (lineCount == 0) // skip header
                continue

            booksListIndexTemp = columns[0]!!.toInt()
            genreTemp = columns[1]!!
            titleTemp = columns[2]!!
            authorTemp = columns[3]!!
            collectionTemp = columns[4]!!
            bookNumberTemp = columns[5]!!
            ratingTemp = columns[6]!!


            booksListItemTemp = booksListList.elementAt(booksListIndexTemp)

            Assert.assertEquals(genreTemp, booksListItemTemp.genre)
            Assert.assertEquals(titleTemp, booksListItemTemp.title)
            Assert.assertEquals(authorTemp, booksListItemTemp.author)
            Assert.assertEquals(collectionTemp.trim(), booksListItemTemp.collection.trim())
            Assert.assertEquals(bookNumberTemp.trim(), booksListItemTemp.bookNumber.trim())
            Assert.assertEquals(ratingTemp, booksListItemTemp.rating)

        }
    }

    /**
     * Scrolls the list of id categoriesList and return all its elements, that are lists Fantasy', Sci-fi' and 'Other'.
     * Return them parsed
     */
    private fun extractAllThreeListsOfCategoriesListViewOfTypeA(
        listViewElement: MobileElement
    ): Collection<LinkedHashSet<CategoryListViewItem>>? {

        val allItems = LinkedHashSet<LinkedHashSet<CategoryListViewItem>>()

        // scrolls up so all the three lists show
        scrollFromOnePointToBorder(listViewElement.location, SwipeDirection.UP)

        var childrenOfCategoriesListViewOfTypeA = getChildrenOfCategoriesListViewOfTypeA(listViewElement)

        if (childrenOfCategoriesListViewOfTypeA.isEmpty())
            return null

        if (childrenOfCategoriesListViewOfTypeA.size != 3)
            throw Exception("couldn't find all three lists")

        // Fantasy list
        scrollFromOnePointToCenterPoint(childrenOfCategoriesListViewOfTypeA[0].location, false)
        allItems.add(extractAllItemsOfACategoriesListViewOfTypeB(childrenOfCategoriesListViewOfTypeA[0])!!)

        // Sci-fi list
        scrollFromOnePointToCenterPoint(childrenOfCategoriesListViewOfTypeA[1].location, false)
        allItems.add(extractAllItemsOfACategoriesListViewOfTypeB(childrenOfCategoriesListViewOfTypeA[1])!!)

        // Other list
        scrollFromOnePointToCenterPoint(childrenOfCategoriesListViewOfTypeA[2].location, false)
        allItems.add(extractAllItemsOfACategoriesListViewOfTypeB(childrenOfCategoriesListViewOfTypeA[2])!!)

        return allItems
    }


    private fun extractAllItemsOfACategoriesListViewOfTypeB(childOfCategoriesListViewOfTypeA: MobileElement): LinkedHashSet<CategoryListViewItem>? {

        // extracts the listView of type B since childOfCategoriesListViewOfTypeA is not the list B itself
        val categoriesListViewOfTypeBElement = getListViewElement(childOfCategoriesListViewOfTypeA)

        var childrenTextElementsOfCategoriesListOfTypeBTemp =
            getChildrenTextElementsOfCategoriesListViewOfTypeB(categoriesListViewOfTypeBElement!!)

        if (childrenTextElementsOfCategoriesListOfTypeBTemp.isEmpty())
            return null

        var parsedChildrenElementsTemp =
            parseElementsToCategoryListViewItems(childrenTextElementsOfCategoriesListOfTypeBTemp)

        val allItems = LinkedHashSet<CategoryListViewItem>()
        do {

            var lastParsedChildElementTemp = parsedChildrenElementsTemp!!.last()

            for (parsedItem in parsedChildrenElementsTemp!!) {
                allItems.add(parsedItem)
            }

            if (SuiteSetup.isIos())
                AppiumUtil.iosScrollInsideElement(getDriver(), categoriesListViewOfTypeBElement, SwipeDirection.RIGHT)
            else {
                scrollFromOnePointToBorder(
                    getLastChildOfListViewOfTypeB(categoriesListViewOfTypeBElement).location,
                    SwipeDirection.LEFT
                )
            }

            childrenTextElementsOfCategoriesListOfTypeBTemp =
                getChildrenTextElementsOfCategoriesListViewOfTypeB(categoriesListViewOfTypeBElement)

            parsedChildrenElementsTemp =
                parseElementsToCategoryListViewItems(childrenTextElementsOfCategoriesListOfTypeBTemp)

        } while (parsedChildrenElementsTemp!!.last() != lastParsedChildElementTemp)

        return allItems
    }

    /**
     * Parse the contents of a given category list of type C to a CategoryListViewItem object
     */
    private fun parseElementsToCategoryListViewItems(childrenTextElementsOfCategoriesListOfTypeB: List<MobileElement>): LinkedHashSet<CategoryListViewItem>? {
        val resultList = LinkedHashSet<CategoryListViewItem>()

        var titleTemp = ""
        var authorTemp = ""
        var charsListTemp = LinkedList<String>()
        var textTemp = ""
        for ((count, textElement) in childrenTextElementsOfCategoriesListOfTypeB.withIndex()) {

            textTemp = textElement.text

            if ("Characters:".equals(textTemp, ignoreCase = true))
                continue

            // new list value
            if (textTemp.startsWith("Title:")) {

                // saves the current item before reading a new one
                if (charsListTemp.isNotEmpty()) {
                    resultList.add(CategoryListViewItem(titleTemp, authorTemp, charsListTemp))
                    authorTemp = ""
                    charsListTemp = LinkedList<String>()
                }

                titleTemp = textTemp

            } else if (textTemp.startsWith("Author:")) {
                authorTemp = textTemp
            } else {
                if (!"BUY:".equals(textTemp, ignoreCase = true))
                    charsListTemp.add(textTemp)

                if (count == childrenTextElementsOfCategoriesListOfTypeB.size - 1)
                    resultList.add(CategoryListViewItem(titleTemp, authorTemp, charsListTemp))
            }

        }

        return resultList
    }

    /**
     * Returns the children elements of the main CategoriesListView list. These elements refer only to elements showing
     * on the screen.
     */
    private fun getChildrenOfCategoriesListViewOfTypeA(categoriesListViewOfTypeAElement: MobileElement): List<MobileElement> {

        var childrenOfCategoryListViewOfTypeALocator: By?

        if (SuiteSetup.isIos()) {
            childrenOfCategoryListViewOfTypeALocator =
                MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeOther' AND name BEGINSWITH[c] 'category:'")

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
     * Returns the children text elements of a Categories ListView of type B. These elements refer only to elements showing
     * on the screen.
     */
    private fun getChildrenTextElementsOfCategoriesListViewOfTypeB(categoriesListViewOfTypeBElement: MobileElement): List<MobileElement> {

        var childrenOfCategoryListViewOfTypeBLocator: By?

        if (SuiteSetup.isIos()) {
            childrenOfCategoryListViewOfTypeBLocator =
                MobileBy.iOSClassChain("**/XCUIElementTypeCell[\$type == 'XCUIElementTypeCollectionView'\$]/**/XCUIElementTypeTextView")


        } else {
            childrenOfCategoryListViewOfTypeBLocator =
                By.xpath(
                    ".//android.view.ViewGroup[.//android.view.ViewGroup//androidx.recyclerview.widget.RecyclerView]//android.widget.TextView"
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
        listViewElement: MobileElement
    ): Collection<String>? {

        var childrenElementsValues = LinkedHashSet<String>()
        childrenElementsValues.addAll(getChildrenOfListViewCharactersList(listViewElement))

        if (childrenElementsValues.isEmpty())
            return null

        var childrenElementsValuesTemp: List<String>
        var lastChildElementValue: String?

        do {
            lastChildElementValue = childrenElementsValues.last()

            if (SuiteSetup.isIos())
                AppiumUtil.iosScrollInsideElement(getDriver(), listViewElement, SwipeDirection.RIGHT)
            else
                scrollFromOnePointToBorder(getLastChildOfListView(listViewElement).location, SwipeDirection.LEFT)

            childrenElementsValuesTemp = getChildrenOfListViewCharactersList(listViewElement)
            childrenElementsValues.addAll(childrenElementsValuesTemp)

        } while (lastChildElementValue != childrenElementsValuesTemp.last())

        return childrenElementsValues
    }

    /**
     * Scrolls the list of id booksList to read all of its elements and return them parsed
     */
    private fun extractAllItemsOfListViewBooksList(
        listViewElement: MobileElement
    ): Collection<BooksListListViewItem>? {

        var childrenElementsValues = LinkedHashSet<BooksListListViewItem>()
        childrenElementsValues.addAll(getChildrenOfListViewBooksList(listViewElement))

        if (childrenElementsValues.isEmpty())
            return null

        var childrenElementsValuesTemp: List<BooksListListViewItem>
        var lastChildElementValue: BooksListListViewItem?

        do {
            lastChildElementValue = childrenElementsValues.last()

            if (SuiteSetup.isIos())
                AppiumUtil.iosScrollInsideElement(getDriver(), listViewElement, SwipeDirection.DOWN)
            else
                AppiumUtil.androidScrollScreenFromOnePointToAnother(
                    getDriver(),
                    getLastChildOfListView(listViewElement).location,
                    listViewElement.location
                )

            childrenElementsValuesTemp = getChildrenOfListViewBooksList(listViewElement)
            childrenElementsValues.addAll(childrenElementsValuesTemp)

        } while (lastChildElementValue != childrenElementsValuesTemp.last())

        // TODO: temp, remover esse print
        for (item in childrenElementsValues)
            println("LINE: " + item.toString())

        return childrenElementsValues
    }

    /**
     * Returns the first listView element that is a child of a given element.
     */
    private fun getListViewElement(parentElement: MobileElement): MobileElement? {
        if (SuiteSetup.isIos()) {
            return waitForChildElementToBePresent(
                parentElement, MobileBy.iOSClassChain("**/XCUIElementTypeCollectionView[1]")
            )
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
                "charactersList" -> return waitForElementToBePresent(MobileBy.id("charactersList"))
                "categoriesList" -> return waitForElementToBePresent(MobileBy.id("categoriesList"))
                "booksList" -> return waitForElementToBePresent(MobileBy.id("booksList"))
                else -> return null
            }
        } else {
            when (listViewId) {
                "charactersList" ->
                    return waitForElementToBePresent(By.xpath("(//androidx.recyclerview.widget.RecyclerView)[1]"))
                "categoriesList" ->
                    return waitForElementToBePresent(By.xpath("(//androidx.recyclerview.widget.RecyclerView)[2]"))
                "booksList" ->
                    return waitForElementToBePresent(
                        By.xpath(
                            "//android.widget.TextView[contains(@text," +
                                    "'Books List View (infinite scroll):')]/following-sibling::" +
                                    "androidx.recyclerview.widget.RecyclerView[1]"
                        )
                    )
                else -> return null
            }
        }

    }

    /**
     * @return a list of names representing each child element of the list view charactersList
     */
    private fun getChildrenOfListViewCharactersList(charactersListViewElement: MobileElement): List<String> {
        val childrenList = mutableListOf<String>()

        var nameTemp = ""
        var bookTemp = ""
        var collectionTemp = ""
        var elementTextTemp = ""
        for (textElement in getTextChildrenElementsOfListView(charactersListViewElement)) {
            elementTextTemp = textElement.text
            when {
                elementTextTemp.startsWith("Name:", ignoreCase = true) -> nameTemp = elementTextTemp
                elementTextTemp.startsWith("Book:", ignoreCase = true) -> bookTemp = elementTextTemp
                elementTextTemp.startsWith("Collection:", ignoreCase = true) -> {
                    collectionTemp = elementTextTemp
                    childrenList.add("$nameTemp;$bookTemp;$collectionTemp")
                }
            }
        }

        return childrenList
    }

    /**
     * @return a list of names representing each child element of the list view booksList
     */
    private fun getChildrenOfListViewBooksList(booksListListViewElement: MobileElement): List<BooksListListViewItem> {
        val childrenList = mutableListOf<BooksListListViewItem>()

        var titleTemp = ""
        var authorTemp = ""
        var collectionTemp = ""
        var bookNumberTemp = ""
        var genreTemp = ""
        var ratingTemp = ""
        var elementTextTemp = ""
        for (textElement in getTextChildrenElementsOfListView(booksListListViewElement)) {
            elementTextTemp = textElement.text

            when {
                elementTextTemp.startsWith("Author:", ignoreCase = true) -> authorTemp = elementTextTemp
                elementTextTemp.startsWith("Collection:", ignoreCase = true) -> collectionTemp = elementTextTemp
                elementTextTemp.startsWith("Book Number:", ignoreCase = true) -> bookNumberTemp = elementTextTemp
                elementTextTemp.startsWith("Genre:", ignoreCase = true) -> genreTemp = elementTextTemp
                elementTextTemp.startsWith("Rating:", ignoreCase = true) -> {
                    ratingTemp = elementTextTemp

                    if (titleTemp.isEmpty() ||
                        authorTemp.isEmpty() ||
                        collectionTemp.isEmpty() ||
                        bookNumberTemp.isEmpty() ||
                        genreTemp.isEmpty() ||
                        ratingTemp.isEmpty()
                    )
                        continue


                    childrenList.add(
                        BooksListListViewItem(
                            titleTemp,
                            authorTemp,
                            collectionTemp,
                            bookNumberTemp,
                            genreTemp,
                            ratingTemp
                        )
                    )
                }
                else -> titleTemp = elementTextTemp
            }
        }

        return childrenList
    }


    /**
     * Locates the children elements of a list view. These elements refer only to elements showing
     * on the screen.
     */
    private fun getChildrenElementsOfListView(listViewElement: MobileElement): List<MobileElement> {

        var lastChildOfListViewLocator: By?

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
     * Locates the text children elements of a list view. These elements refer only to elements showing
     * on the screen.
     */
    private fun getTextChildrenElementsOfListView(listViewElement: MobileElement): List<MobileElement> {

        var lastChildOfListViewLocator: By?

        if (SuiteSetup.isIos()) {
            lastChildOfListViewLocator =
                MobileBy.iOSClassChain("**/XCUIElementTypeCell/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeTextView")

        } else {
            lastChildOfListViewLocator =
                By.xpath(".//android.view.ViewGroup//android.view.ViewGroup//android.widget.TextView")
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

        var lastChildOfListViewLocator: By?

        if (SuiteSetup.isIos()) {
            lastChildOfListViewLocator =
                By.xpath(".//XCUIElementTypeCell[.//XCUIElementTypeOther//XCUIElementTypeOther//XCUIElementTypeOther][last()]")

        } else {
            lastChildOfListViewLocator = By.xpath("(.//android.view.ViewGroup[.//android.view.ViewGroup])[last()]")
        }

        return waitForChildElementToBePresent(listViewElement, lastChildOfListViewLocator)

    }

    private fun getLastChildOfListViewOfTypeB(listViewOfTypeBElement: MobileElement): MobileElement {

        var lastChildOfListViewLocator: By?

        if (SuiteSetup.isIos()) {
            lastChildOfListViewLocator =
                MobileBy.iOSClassChain("**/XCUIElementTypeCell[\$type == 'XCUIElementTypeCollectionView'\$][-1]")
        } else {
            lastChildOfListViewLocator =
                By.xpath(".//android.view.ViewGroup[.//android.view.ViewGroup//androidx.recyclerview.widget.RecyclerView][last()]")
        }

        return waitForChildElementToBePresent(listViewOfTypeBElement, lastChildOfListViewLocator)

    }

//    /**
//     * Locates the first child element of a list view. The child element refers only to a element showing
//     * on the screen. The first element of a horizontal list will be the one most to the left, and the
//     * last on a vertical list will be the one most to the top of the list.
//     */
//    private fun getFirstChildOfListView(listViewElement: MobileElement): MobileElement {
//
//        var lastChildOfListViewLocator: By?
//
//        if (SuiteSetup.isIos()) {
//            lastChildOfListViewLocator =
//                By.xpath("(.//XCUIElementTypeCell[.//XCUIElementTypeOther//XCUIElementTypeOther//XCUIElementTypeOther])[1]")
//        } else {
//            lastChildOfListViewLocator = By.xpath("(.//android.view.ViewGroup[.//android.view.ViewGroup])[1]")
//        }
//
//        return waitForChildElementToBePresent(listViewElement, lastChildOfListViewLocator)
//
//    }

}

