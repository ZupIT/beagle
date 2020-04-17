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

package br.com.zup.beagle.logger

import android.util.Log
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.setup.Environment

private const val BEAGLE_TAG = "BeagleSDK"

internal object BeagleLogger {

    fun warning(message: String) = runIfDebug {
        Log.w(BEAGLE_TAG, message)
    }

    fun error(message: String) = runIfDebug {
        Log.e(BEAGLE_TAG, message)
    }

    fun error(message: String, throwable: Throwable) = runIfDebug {
        Log.e(BEAGLE_TAG, message, throwable)
    }


    fun info(message: String) = runIfDebug {
        Log.i(BEAGLE_TAG, message)
    }

    fun debug(message: String) = runIfDebug {
        Log.d(BEAGLE_TAG, message)
    }

    fun verbose(message: String) = runIfDebug {
        Log.v(BEAGLE_TAG, message)
    }

    private fun runIfDebug(runBlock: () -> Unit) {
        if (BeagleEnvironment.beagleSdk.config.environment == Environment.DEBUG) {
            runBlock()
        }
    }
}