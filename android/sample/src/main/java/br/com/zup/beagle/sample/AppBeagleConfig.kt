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

package br.com.zup.beagle.sample

import br.com.zup.beagle.android.annotation.BeagleComponent
import br.com.zup.beagle.android.setup.BeagleConfig
import br.com.zup.beagle.android.setup.Cache
import br.com.zup.beagle.android.setup.Environment
import br.com.zup.beagle.sample.constants.BASE_URL

@BeagleComponent
class AppBeagleConfig : BeagleConfig {
    override val environment: Environment get() = Environment.DEBUG
    override val baseUrl: String get() = BASE_URL
    override val isLoggingEnabled: Boolean = true
    override val cache: Cache = Cache(
        enabled = false,
        maxAge = 300,
        size = 15
    )
}