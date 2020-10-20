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

package br.com.zup.beagle.automatedTests.utils

import android.app.Activity
import android.content.Intent
import androidx.test.rule.ActivityTestRule
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.automatedTests.activity.AppBeagleActivity
import br.com.zup.beagle.automatedTests.activity.MainActivity

class TestUtils {

    companion object {
        fun <T: Activity> startActivity(activityTestRule: ActivityTestRule<T>, url: String) {

            val intent = Intent()
            intent.putExtra(MainActivity.BFF_URL_KEY, url)
            activityTestRule.launchActivity(intent)
        }

        fun startBeagleActivity(activityTestRule: ActivityTestRule<AppBeagleActivity>, url: String) {

            val intent = AppBeagleActivity.newAppIntent(activityTestRule.activity, ScreenRequest(url))
            activityTestRule.launchActivity(intent)
        }
    }


}