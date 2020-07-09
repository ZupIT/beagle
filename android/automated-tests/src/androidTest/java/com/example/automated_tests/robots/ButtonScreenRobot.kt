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

package com.example.automated_tests.robots

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.automated_tests.R
import org.hamcrest.*

class ButtonScreenRobot {

    fun checkViewContainsText(text: String?): ButtonScreenRobot {
        Espresso.onView(Matchers.allOf(ViewMatchers.withText(text))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this
    }

    fun clickOnText(text: String?): ButtonScreenRobot {
        Espresso.onView(Matchers.allOf(ViewMatchers.withText(text), ViewMatchers.isDisplayed())).perform(ViewActions.click())
        return this
    }

//        fun selectMenuOption(): ButtonScreenRobot {
//            Espresso.onView(Matchers.allOf(ViewMatchers.withContentDescription("More options"), childAtPosition(childAtPosition(withId(R.id.action_bar), 1), 0))).perform(ViewActions.click())
//            return this
//        }

//        fun typeIntoTextField(position1: Int, position2: Int, text: String?): ButtonScreenRobot {
//            Espresso.onView(Matchers.allOf(childAtPosition(childAtPosition(withId(R.id.fragment_content), position1), position2))).perform(ViewActions.typeText(text))
//            Espresso.closeSoftKeyboard()
//            return this
//        }

        @Throws(InterruptedException::class)
        fun sleep(seconds: Int): ButtonScreenRobot {
            Thread.sleep(seconds * 1000L)
            return this
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