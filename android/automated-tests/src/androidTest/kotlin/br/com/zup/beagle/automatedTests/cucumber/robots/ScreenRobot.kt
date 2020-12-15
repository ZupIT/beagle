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

package br.com.zup.beagle.automatedTests.cucumber.robots

import android.text.InputType
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.zup.beagle.automatedTests.R
import br.com.zup.beagle.automatedTests.utils.WaitHelper
import br.com.zup.beagle.automatedTests.utils.action.OrientationChangeAction
import br.com.zup.beagle.automatedTests.utils.action.SmoothScrollAction
import br.com.zup.beagle.automatedTests.utils.action.SmoothScrollPercentAction
import br.com.zup.beagle.automatedTests.utils.assertions.RecyclerViewItemCountAssertion
import br.com.zup.beagle.automatedTests.utils.assertions.RecyclerViewOrientationAssertion
import br.com.zup.beagle.automatedTests.utils.matcher.MatcherExtension
import br.com.zup.beagle.widget.core.TextAlignment
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher

class ScreenRobot {

    fun checkViewContainsText(text: String?, waitForText: Boolean = false): ScreenRobot {
        if (waitForText) {
            WaitHelper.waitForWithElement(onView(withText(text)))
        }
        onView(Matchers.allOf(withText(text))).check(matches(isDisplayed()))
        return this
    }

    fun checkTabContainsTextAndIcon(title: String? = null, icon: Int, waitForText: Boolean = false) {
        if (waitForText) {
            WaitHelper.waitForWithElement(onView(withText(title)))
        }

        onView(MatcherExtension.tabBarItemWithIconAndTitle(title, icon)).check(matches(isDisplayed()))
    }

    fun checkViewTextColor(text: String?, color: String, waitForText: Boolean = false): ScreenRobot {
        if (waitForText) {
            WaitHelper.waitForWithElement(onView(withText(text)))
        }

        onView(Matchers.allOf(withText(text), MatcherExtension.withTextColor(color))).check(matches(isDisplayed()))
        return this
    }

    fun checkViewTextAlignment(text: String?, textAlignment: TextAlignment, waitForText: Boolean = false): ScreenRobot {
        if (waitForText) {
            WaitHelper.waitForWithElement(onView(withText(text)))
        }

        onView(Matchers.allOf(withText(text), MatcherExtension.withTextAlignment(textAlignment))).check(matches(isDisplayed()))
        return this
    }

    fun checkViewDoesNotContainsText(text: String?, waitForText: Boolean = false): ScreenRobot {
        if (waitForText) {
            WaitHelper.waitForWithElement(onView(withText(text)))
        }

        onView(Matchers.allOf(withText(text))).check(doesNotExist())
        return this
    }

    fun checkViewIsNotDisplayed(text: String?): ScreenRobot {
        onView(Matchers.allOf(withText(text))).check(matches(not(isDisplayed())))
        return this
    }

    fun checkViewIsDisplayed(text: String?): ScreenRobot {
        onView(Matchers.allOf(withText(text))).check(matches(isDisplayed()))
        return this
    }

    fun typeText(hint: String, text: String): ScreenRobot {
        onView(withHint(hint)).perform(ViewActions.typeText((text)))
        return this
    }

    fun checkViewContainsHint(hint: String?, waitForText: Boolean = false): ScreenRobot {
        if (waitForText) {
            WaitHelper.waitForWithElement(onView(withHint(hint)))
        }

        onView(Matchers.allOf(withHint(hint))).check(matches(isDisplayed()))
        return this
    }

    fun clickOnText(text: String?): ScreenRobot {
        onView(Matchers.allOf(withText(text), isDisplayed())).perform(ViewActions.click())
        return this
    }

    fun clickOnInputWithHint(hint: String?): ScreenRobot {
        onView(Matchers.allOf(withHint(hint), isDisplayed())).perform(ViewActions.click())
        return this
    }

    fun disabledFieldHint(text: String): ScreenRobot {
        onView(withHint(text)).check(matches(not(isEnabled())))
        return this
    }

    fun disabledFieldText(text: String): ScreenRobot {
        onView(withText(text)).check(matches(not(isEnabled())))
        return this
    }

    fun hintInSecondPlan(text: String): ScreenRobot {
        onView(withHint(text)).perform(pressBack())
        onView(allOf(withHint(text), isDisplayed()))
        return this
    }

    fun checkInputTypeNumber(text: String): ScreenRobot {
        onView(withHint(text)).check(matches(allOf(withInputType(InputType.TYPE_CLASS_NUMBER))))
        return this
    }

    fun typeIntoTextField(position1: Int, position2: Int, text: String?): ScreenRobot {
        onView(childAtPosition(childAtPosition(withClassName(
            Matchers.`is`("br.com.zup.beagle.android.view.custom.BeagleFlexView")), position1), position2)).perform(scrollTo(), ViewActions.replaceText(text))
        Espresso.closeSoftKeyboard()
        return this
    }

    fun scrollViewDown(): ScreenRobot {
        onView(withId(R.id.root_layout)).perform(ViewActions.swipeUp())
        return this
    }

    fun swipeLeftOnView(): ScreenRobot {
        onView(Matchers.allOf(withId(R.id.root_layout))).perform(ViewActions.swipeLeft())
        return this
    }

    fun swipeRightOnView(): ScreenRobot {
        onView(withId(R.id.root_layout)).perform(ViewActions.swipeRight())
        return this
    }

    fun scrollTo(text: String?): ScreenRobot {
        onView(withText(text)).perform(scrollTo()).check(matches(isDisplayed()))
        return this
    }

    fun scrollTo(viewId: Int): ScreenRobot {
        onView(withId(viewId)).perform(scrollTo()).check(matches(isDisplayed()))
        return this
    }

    fun scrollToWithHint(text: String?): ScreenRobot {
        onView(withHint(text)).perform(scrollTo()).check(matches(isDisplayed()))
        return this
    }

    fun clickOnTouchableImage(): ScreenRobot {
        onView(childAtPosition(childAtPosition(withClassName(Matchers.`is`("br.com.zup.beagle.android.view.custom.BeagleFlexView")), 1), 1)).perform(ViewActions.click())
        return this
    }

    @Throws(InterruptedException::class)
    fun sleep(seconds: Int): ScreenRobot {
        Thread.sleep(seconds * 1000L)
        return this
    }

    fun hideKeyboard() {
        Espresso.closeSoftKeyboard()
    }

    fun checkListViewItemCount(listViewId: Int, expectedCount: Int) {
        onView(withId(listViewId))
            .check(RecyclerViewItemCountAssertion.withItemCount(expectedCount))
    }

    fun checkListViewOrientation(listViewId: Int, orientation: Int) {
        onView(withId(listViewId))
            .check(RecyclerViewOrientationAssertion.withOrientation(orientation))
    }

    fun scrollListToPosition(listId: Int, position: Int): ScreenRobot {
        onView(withId(listId))
            .perform(SmoothScrollAction(position))
        return this
    }

    fun scrollListByPercent(listId: Int, scrollPercent: Int): ScreenRobot {
        onView(withId(listId))
            .perform(SmoothScrollPercentAction(scrollPercent))
        return this
    }

    fun checkListViewItemContainsText(listId: Int, position: Int, expectedText: String): ScreenRobot {
        onView(withId(listId))
            .check { view, _ ->
                ViewMatchers.assertThat(
                    "RecyclerView item",
                    view,
                    atPosition(position, ViewMatchers.hasDescendant(withText(expectedText))))
            }

        return this
    }

    fun checkListViewItemContainsViewWithId(listId: Int, position: Int, expectedViewId: Int): ScreenRobot {
        onView(withId(listId))
            .check { view, _ ->
                ViewMatchers.assertThat(
                    "RecyclerView item template",
                    view,
                    atPosition(position, ViewMatchers.hasDescendant(withId(expectedViewId))))
            }

        return this
    }

    fun setScreenPortrait() {
        onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationPortrait())
    }

    fun setScreenLandScape() {
        onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
    }

    fun clickOnTextInsideListViewItem(
        listViewId: Int,
        position: Int,
        viewId: Int,
    ) {
        onView(withId(listViewId)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return allOf()
                }

                override fun getDescription(): String {
                    return ""
                }

                override fun perform(uiController: UiController?, view: View?) {
                    view?.findViewById<View>(viewId)?.let {
                        it.performClick()
                    }
                }

            })
        )
    }

    companion object {
        private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description) {
                    description.appendText("Child at position $position in parent ")
                    parentMatcher.describeTo(description)
                }

                public override fun matchesSafely(view: View): Boolean {
                    val parent = view.parent
                    return (parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position))
                }
            }
        }

        fun atPosition(position: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?>? {
            return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description) {
                    description.appendText("has item at position $position: ")
                    itemMatcher.describeTo(description)
                }

                override fun matchesSafely(view: RecyclerView): Boolean {
                    val viewHolder = view.findViewHolderForAdapterPosition(position)
                        ?: // has no item on such position
                        return false
                    return itemMatcher.matches(viewHolder.itemView)
                }
            }
        }
    }
}