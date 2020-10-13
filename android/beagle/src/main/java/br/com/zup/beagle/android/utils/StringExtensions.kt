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

package br.com.zup.beagle.android.utils

import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.setup.BeagleEnvironment

internal fun String.toAndroidColor(): Int? = try {
    ColorUtils.hexColor(this)
} catch (ex: Exception) {
    BeagleMessageLogs.errorWhenMalformedColorIsProvided(this, ex)
    null
}

internal fun String.getContextId() = this.split(".", "[")[0]

fun String.getExpressions(): List<String> {
    val expressions = mutableListOf<String>()
    val expressionContentRegex = "(\\\\*)@\\{(([^'\\}]|('([^'\\\\]|\\\\.)*'))*)\\}"
    expressionContentRegex.toRegex().findAll(this).iterator().forEach {
        val expressionContent = it.groupValues[2]
        expressions.add(expressionContent)
    }
    return expressions
}

internal fun String.removeBaseUrl(): String = this.removePrefix(BeagleEnvironment.beagleSdk.config.baseUrl)