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

package br.com.zup.beagle.analytics

import br.com.zup.beagle.builder.BeagleBuilder
import kotlin.properties.Delegates

/**
 * Beagle analytics is used to track screen.
 */
data class ScreenEvent(
    val screenName: String
) {
    companion object{
        @JvmStatic
        fun builder() = Builder()
    }
    class Builder: BeagleBuilder<ScreenEvent> {
        var screenName: String by Delegates.notNull()

        fun screenName(screenName: String) = this.apply { this.screenName = screenName }

        fun screenName(block: () -> String) {
            screenName(block.invoke())
        }

        override fun build() = ScreenEvent(screenName)
    }
}