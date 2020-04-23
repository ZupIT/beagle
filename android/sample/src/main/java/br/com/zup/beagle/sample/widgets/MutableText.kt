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

package br.com.zup.beagle.sample.widgets

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import br.com.zup.beagle.widget.core.WidgetView

data class MutableText(
    val firstText: String = "",
    val secondText: String = "",
    val color: String = "#000000"
): WidgetView() {
    override fun buildView(context: Context) = TextView(context).apply {
        val color = Color.parseColor(color)
        text = firstText
        setTextColor(color)
        setOnClickListener {
            text = if (text == firstText)
                secondText
            else
                firstText
        }
    }
}