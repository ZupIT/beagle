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

import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

private const val SCROLLVIEW_SCREEN_HEADER = "Beagle ScrollView"
private const val PARAGRAPH =
    """Lorem ipsum diam luctus mattis arcu accumsan, at curabitur hac in dictum senectus neque,
    orci lorem aenean euismod leo. eu nunc tellus proin eget euismod lorem curabitur habitant nisi himenaeos 
    habitasse at quam, convallis potenti scelerisque aenean habitant viverra mollis fusce convallis dui
    urna aliquam. diam tristique etiam fermentum etiam nunc eget vel, ante nam eleifend habitant per senectus diam,
    bibendum lectus enim ultrices litora viverra. lorem fusce leo hendrerit himenaeos elementum aliquet nec,
    vestibulum luctus pretium diam tellus ligula conubia elit, a sodales torquent fusce massa euismod. et magna 
    imperdiet conubia sed netus vitae justo maecenas proin lorem, sapien nisi porttitor dolor facilisis pharetra 
    nam class. Morbi nullam odio accumsan quam urna sit tortor vulputate mi fames, elit molestie gravida ipsum
    dictumst aenean curabitur ultrices consectetur pharetra, auctor aenean diam pellentesque condimentum risus 
    diam scelerisque rutrum. conubia sem tincidunt cras venenatis tristique nisl duis rhoncus blandit, sed mattis 
    vulputate accumsan suscipit tristique imperdiet dui, ornare ipsum tempor viverra elementum consectetur euismod
    dapibus. ultricies in consectetur libero nam ultrices egestas quis volutpat ut nec sagittis eu, elementum
    malesuada ullamcorper dapibus donec aenean mattis odio mi nulla gravida. tellus metus imperdiet justo mattis 
    eros sodales potenti nibh nisl tincidunt, metus etiam cubilia amet donec primis sapien erat dictumst. Accumsan 
    etiam himenaeos tempor integer habitasse curae ac, tincidunt laoreet taciti nisl habitasse conubia, maecenas nec
    velit vitae amet varius. scelerisque vel fringilla consequat justo curabitur nam massa vitae, tempus tempor 
    sit torquent massa malesuada ullamcorper, laoreet elementum nam pharetra tempus nam mauris. sociosqu dictum 
    malesuada lectus suscipit ullamcorper aliquet pulvinar semper laoreet, vulputate aliquam nibh odio donec ligula
     bibendum suspendisse, facilisis ut lobortis lacus tortor hendrerit integer posuere. phasellus egestas dui hac 
    auctor faucibus purus accumsan arcu, sem vivamus rhoncus pharetra aliquam ornare curabitur rutrum, ut venenatis
     proin iaculis orci gravida molestie."""

class ScrollViewScreenSteps : AbstractStep() {
    override var bffRelativeUrlPath = "/scrollview"

    @Before("@scrollview")
    fun setup() {
        loadBffScreenFromMainScreen()
    }

    @Given("^that I'm on the scrollview screen$")
    fun checkScrollViewScreen() {
        // ScreenRobot().checkViewContainsText(SCROLLVIEW_SCREEN_HEADER, true)
        waitForElementWithTextToBeClickable(SCROLLVIEW_SCREEN_HEADER, false, false)
    }

    @When("^I have a horizontal scroll configured$")
    fun checkHorizontalScrollText() {
        // ScreenRobot().checkViewContainsText("Horizontal", true)
        waitForElementWithTextToBeClickable("Horizontal", false, false)
    }

    @Then("^scrollview screen should perform the scroll action horizontally$")
    fun validateHorizontalScroll() {
        // ScreenRobot().scrollTo("Horizontal")
        scrollDownToElementWithText("Horizontal", false, false)
    }

    @When("^I press on text scroll horizontal (.*)$")
    fun checkIfTextOneLineScrollViewHorizontal(string: String) {
        /*
        ScreenRobot()
            .scrollTo(string)
            .checkViewContainsText(string, true)
            .clickOnText(string)

         */
        scrollDownToElementWithText(string, false, false).click()
    }

    @Then("^the text should change for the next and the scrollview should perform horizontally (.*)$")
    fun checkNewTextScrollViewHorizontal(string: String) {
        /*
        ScreenRobot()
            .checkViewContainsText(PARAGRAPH)
            .checkViewDoesNotContainsText(string)

         */
        waitForElementWithTextToBeClickable(PARAGRAPH, false, false)
        waitForElementWithTextToBeInvisible(string, false, false)
    }

    @When("^I press on text to be scrolled and rotated (.*)$")
    fun checkIfTextOneLineScrollViewWithRotationHorizontal(string: String) {
        /*
        ScreenRobot()
            .scrollTo(string)
            .checkViewContainsText(string, true)
            .clickOnText(string)

         */
        scrollDownToElementWithText(string, false, false).click()
    }

    @Then("^the text horizontal of scrollview rotate should change$")
    fun checkNewTextScrollViewRotationHorizontal() {
        // ScreenRobot().checkViewContainsText(PARAGRAPH)
        waitForElementWithTextToBeClickable(PARAGRAPH, false, false)
    }

    @And("^the scrollview rotate should perform horizontally (.*)$")
    fun checkScrollViewHorizontallyRotation(string: String) {
        // ScreenRobot().checkViewDoesNotContainsText(string)
        waitForElementWithTextToBeInvisible(string, false, false)
    }

    @And("^even if the screen is rotated the scrollview must be perform horizontally (.*)$")
    fun checkScrollViewRotationHorizontal(string: String) {
        /*
        onView(isRoot()).perform(orientationLandscape())
        ScreenRobot()
            .checkViewDoesNotContainsText(string)

         */
        rotateToLandscapePosition()
        waitForElementWithTextToBeInvisible(string, false, false)
    }

    @When("^I have a vertical scroll configured$")
    fun checkVerticalScrollText() {
        // ScreenRobot().checkViewContainsText("Vertical", true)
        waitForElementWithTextToBeClickable("Vertical", false, false)
    }

    @Then("^scrollview screen should perform the scroll action vertically$")
    fun validateVerticalScroll() {
        // ScreenRobot().scrollTo("Vertical")
        scrollDownToElementWithText("Vertical", false, false)
    }

    @When("^I press on text scrollview vertical (.*)$")
    fun checkIfTextOneLineScrollViewVertical(string: String) {
        /*
        ScreenRobot()
            .checkViewContainsText(string, true)
            .clickOnText(string)

         */
        waitForElementWithTextToBeClickable(string, false, false).click()
    }

    @Then("^the text should change$")
    fun checkNewTextScrollViewVertical() {
        // ScreenRobot().checkViewContainsText(PARAGRAPH)
        waitForElementWithTextToBeClickable(PARAGRAPH, false, false)
    }

    @And("^the scrollview should perform vertically (.*)$")
    fun checkScrollViewVertical(string: String) {
        // ScreenRobot().checkViewDoesNotContainsText(string)
        waitForElementWithTextToBeInvisible(string, false, false)
    }

    @When("^I press on text scrollview to be rotate (.*)$")
    fun checkIfTextOneLineScrollViewWithRotationVertical(string: String) {
        /*
        ScreenRobot()
            .checkViewContainsText(string, true)
            .clickOnText(string)

         */
        waitForElementWithTextToBeClickable(string, false, false).click()
    }

    @Then("^the text vertical of scrollview rotate should change$")
    fun checkNewTextScrollViewRotationVertical() {
        // ScreenRobot().checkViewContainsText(PARAGRAPH)
        waitForElementWithTextToBeClickable(PARAGRAPH, false, false)
    }

    @And("^the scrollview rotate should perform vertically (.*)$")
    fun checkScrollViewVerticallyRotation(string: String) {
        // ScreenRobot().checkViewDoesNotContainsText(string)
        waitForElementWithTextToBeInvisible(string, false, false)
    }

    @And("^even if the screen is rotated the scrollview must be perform vertically (.*)$")
    fun checkScrollViewRotationVertical(string: String) {
        /*
        onView(isRoot()).perform(orientationLandscape())
        ScreenRobot()
            .checkViewDoesNotContainsText(string)

         */
        rotateToLandscapePosition()
        waitForElementWithTextToBeInvisible(string, false, false)
    }

}