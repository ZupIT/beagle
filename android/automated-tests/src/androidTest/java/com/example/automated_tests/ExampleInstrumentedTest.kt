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

package com.example.automated_tests

import androidx.test.rule.ActivityTestRule
import com.example.automated_tests.activity.MainActivity
import com.example.automated_tests.utils.TestUtils
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleInstrumentedTest {

    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(
        MainActivity::class.java)

    @Test
    fun testButtonUrlLoading() {
        TestUtils.startActivity(activityTestRule,"http://10.0.2.2:8080/button" )

        sleep(10000)
    }


    @Test
    fun testImageUrlLoading() {
        TestUtils.startActivity(activityTestRule,"http://10.0.2.2:8080/image" )

        sleep(10000)
    }



}