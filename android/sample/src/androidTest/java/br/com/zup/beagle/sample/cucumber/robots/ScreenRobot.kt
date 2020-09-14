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

package br.com.zup.beagle.sample.cucumber.robots

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import br.com.zup.beagle.sample.utils.WaitHelper
import org.hamcrest.Matchers

class ScreenRobot {

    fun checkViewContainsText(text: String?, waitForText: Boolean = false): ScreenRobot {
        if (waitForText){
            WaitHelper.waitForWithElement(onView(withText(text)))
        }

        onView(Matchers.allOf(withText(text))).check(matches(isDisplayed()))

        return this

    }

    fun clickOnText(text: String?): ScreenRobot {
        onView(Matchers.allOf(withText(text), isDisplayed())).perform(ViewActions.click())
        return this
    }

    @Throws(InterruptedException::class)
    fun sleep(seconds: Int): ScreenRobot {
        Thread.sleep(seconds * 1000L)
        return this
    }
}