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

package br.com.zup.beagle.sample.snapshot

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.com.zup.beagle.sample.MainActivity
import com.facebook.testing.screenshot.Screenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun testSnapshot() {
        rule.launchActivity(Intent())

//        val view = rule.activity.window.decorView.rootView

        Screenshot.snapActivity(rule.activity)
            .record()

    }
}
