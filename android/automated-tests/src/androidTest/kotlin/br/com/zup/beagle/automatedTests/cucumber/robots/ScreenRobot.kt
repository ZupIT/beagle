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
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.zup.beagle.automatedTests.R
import br.com.zup.beagle.automatedTests.utils.WaitHelper
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

    fun checkTabContainsTextAndIcon(title:String? = null, icon:Int, waitForText: Boolean = false ){
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
    }
}