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

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

/**
 * Used for debugging since cucumber task in gradle.build won't hit breakpoints.
 * Must be in the steps folder
 */
@RunWith(Cucumber::class)
@CucumberOptions(
    tags = "@android and not @inProgress",
    features = ["src/test/resources/features"],
    plugin = ["br.com.zup.beagle.setup.SuiteSetupPlugin"]
)
class Runner {
}