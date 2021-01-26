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

package br.com.zup.beagle.cucumber.steps

import br.com.zup.beagle.setup.SuiteSetup
import io.cucumber.java.After
import io.cucumber.java.Scenario
import org.apache.commons.io.FileUtils
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import java.io.File

/**
 * Must be in the same package of cucumber steps
 */
class HookManager {

    /**
     * Before each Scenario and its Examples
     */
    /*
    @Before
    fun setupBeforeScenario() {
        //...
    }
    */

    /**
     * After each Scenario and its Examples
     */
    @After
    fun tearDownAfterScenario(scenario: Scenario) {

        // takes a screenshot of the screen on error
        if (scenario.isFailed) {
            try {
                val scrFile: File = (SuiteSetup.getDriver() as TakesScreenshot).getScreenshotAs(OutputType.FILE)
                val destFile: File =
                    File("${SuiteSetup.ERROR_SCREENSHOTS_FOLDER}/ERROR-${scenario.name}-${System.currentTimeMillis()}.png")

                if (destFile.exists())
                    destFile.delete()

                FileUtils.moveFile(
                    scrFile,
                    destFile
                )
            } catch (exception: Exception) {
                println("ERROR taking a screenshot on error: ${exception.message}")
            }
        }

        SuiteSetup.restartApp()
    }
}