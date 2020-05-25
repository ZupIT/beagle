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

import br.com.zup.beagle.core.CoreDeclarativeDsl

/**
 * Define click event
 *
 * @param category
 * @param label
 * @param value
 *
 */
data class ClickEvent(
    val category: String,
    val label: String? = null,
    val value: String? = null
)

fun clickEvent(block: ClickEventBuilder.() -> Unit): ClickEvent = ClickEventBuilder().apply(block).build()

@CoreDeclarativeDsl
class ClickEventBuilder {

    var category: String = ""
    var label: String? = null
    var value: String? = null

    fun build(): ClickEvent = ClickEvent(category, label, value)

}
