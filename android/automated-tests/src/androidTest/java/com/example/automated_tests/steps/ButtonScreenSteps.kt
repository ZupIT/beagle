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

package com.example.automated_tests.steps

import androidx.test.rule.ActivityTestRule
import com.example.automated_tests.MainActivity
import com.example.automated_tests.TestUtils
import org.junit.Rule
import org.junit.Test

class ButtonScreenSteps {

    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testButtonUrlLoading() {
        TestUtils.startActivity(activityTestRule,"http://10.0.2.2:8080/button" )

    @Test
     fun TestSelectTextField() {
      new ButtonScreenRobot()
                .checkViewContainsText("Beagle Sample")
                .selectMenuOption()
                .clickOnText("TextField")
                .typeIntoTextField(0,0,"beagle")
        }



        Thread.sleep(10000)
    }

}