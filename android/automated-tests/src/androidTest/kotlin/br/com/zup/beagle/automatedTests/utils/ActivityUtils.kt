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
import java.lang.reflect.Field


class ActivityUtils {
        fun getResultCode(activity: Activity?): Int {
            var returnCode = 0
            if (activity != null) {
                returnCode = try {
                    val resultCodeField: Field = Activity::class.java.getDeclaredField("mResultCode")
                    resultCodeField.setAccessible(true)
                    resultCodeField.get(activity) as Int
                } catch (e: Exception) {
                    0
                }
            }
            return returnCode
        }

        fun getResultData(activity: Activity?): Intent? {
            var returnIntent: Intent? = null
            if (activity != null) {
                returnIntent = try {
                    val resultDataField: Field = Activity::class.java.getDeclaredField("mResultData")
                    resultDataField.setAccessible(true)
                    resultDataField.get(activity) as Intent
                } catch (e: Exception) {
                    null
                }
            }
            return returnIntent
        }
    }