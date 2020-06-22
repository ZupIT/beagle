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

import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.widget.RootView
import org.json.JSONArray
import org.json.JSONObject

internal fun String.toAndroidColor(): Int = ColorUtils.hexColor(this)

internal fun String.getExpressions(): List<String> {
    val expressionPattern = "@{"
    val patterns = this.substringAfter(expressionPattern, "").split(expressionPattern)
    return if (patterns[0].isNotEmpty()) {
        patterns.map { pattern ->
            pattern.substring(0, pattern.indexOfFirst { it == '}' })
        }
    } else {
        emptyList()
    }
}

internal fun String.getValueWithEvaluatedExpressions(rootView: RootView): Any? {
    return try {
        var value: String = this
        getExpressions().forEach {
            value = value.replace(
                "@{$it}",
                Bind.Expression("@{${it}}", String::class.java).evaluateExpression(rootView)
            )
        }
        when {
            value.startsWith("{") -> JSONObject(value)
            value.startsWith("[") -> JSONArray(value)
            else -> value
        }
    } catch (ex: Exception) {
        BeagleMessageLogs.errorWhileTryingToEvaluateBinding(ex)
        null
    }
}
