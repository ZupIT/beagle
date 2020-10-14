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

import android.graphics.Color

internal object ColorUtils {

    fun hexColor(hexColor: String): Int {
        return when (hexColor.getColorLength()) {
            8 -> Color.parseColor(formatHexColorAlpha(hexColor))
            else -> Color.parseColor(formatHexColor(hexColor))
        }
    }

    private fun String.getColorLength() = removePrefix("#").length

    private fun formatHexColorAlpha(color: String): String {
        return "^#([0-9A-F]{6})([0-9A-F]{2})$"
            .toRegex(RegexOption.IGNORE_CASE)
            .replace(color, "#\$2\$1")
    }

    private fun formatHexColor(color: String): String {
        val colorLength = color.getColorLength()
        return generateRegexString(colorLength)
            .toRegex(RegexOption.IGNORE_CASE)
            .replace(color, generateReplacement(colorLength))
    }

    private fun generateRegexString(colorLength: Int): String {
        return if (colorLength == 3) {
            "^#([0-9A-F])([0-9A-F])([0-9A-F])?$"
        } else {
            "^#([0-9A-F])([0-9A-F])([0-9A-F])([0-9A-F])?$"
        }
    }

    private fun generateReplacement(colorLength: Int): String {
        return if (colorLength == 3) {
            "#\$1\$1\$2\$2\$3\$3"
        } else {
            "#\$4\$4\$1\$1\$2\$2\$3\$3"
        }
    }
}
