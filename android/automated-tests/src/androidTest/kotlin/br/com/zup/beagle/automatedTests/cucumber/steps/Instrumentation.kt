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

package br.com.zup.beagle.automatedTests.cucumber.steps

import android.os.Bundle
import android.support.test.runner.MonitoringInstrumentation
import cucumber.api.CucumberOptions
import cucumber.api.android.CucumberInstrumentationCore

@CucumberOptions(
    features = ["features"],
    tags = ["@regression"],
    glue = ["br.com.zup.beagle.automatedTests.cucumber.steps"])

class Instrumentation : MonitoringInstrumentation() {
    private val instrumentationCore: CucumberInstrumentationCore = CucumberInstrumentationCore(this)
    override fun onCreate(arguments: Bundle) {
        super.onCreate(arguments)
        val tags: String = BuildConfig.TEST_TAGS
        if (tags.isNotEmpty()) {
            arguments.putString("tags", tags.replace(",".toRegex(), "--").replace("\\s".toRegex(), ""))
        }
        instrumentationCore.create(arguments)
        start()
    }

    override fun onStart() {
        super.onStart()
        waitForIdleSync()
        instrumentationCore.start()
    }
}