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

package br.com.zup.beagle.setup

import io.cucumber.plugin.EventListener
import io.cucumber.plugin.event.EventHandler
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.TestRunFinished
import io.cucumber.plugin.event.TestRunStarted
import org.apache.commons.io.FileUtils
import java.io.File


/**
 * Custom suite test run hooks
 */
class SuiteSetupPlugin : EventListener {
    override fun setEventPublisher(publisher: EventPublisher) {

        // works as an @AfterAll method, running once at the end of the test suite
        publisher.registerHandlerFor(
            TestRunFinished::class.java,
            EventHandler<TestRunFinished> {
                SuiteSetup.closeDriver()
            })

        // works as a @BeforeAll method, running once at the beginning of the test suite
        publisher.registerHandlerFor(
            TestRunStarted::class.java,
            EventHandler<TestRunStarted> { event: TestRunStarted? ->

                SuiteSetup.initSuit()

                // cleans screenshot temp dir
                try {
                    val screenShotsFolder: File = File("${SuiteSetup.ERROR_SCREENSHOTS_FOLDER}")
                    if (screenShotsFolder.exists())
                        FileUtils.cleanDirectory(screenShotsFolder!!)
                } catch (exception: Exception) {
                    println("ERROR cleaning screenshots folder: ${exception.message}")
                }

            })
    }
}