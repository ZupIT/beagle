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
import android.view.View
import android.widget.TextView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.android.widget.core.WidgetView

@RegisterWidget
data class MutableText(
    val firstText: String = "",
    val secondText: String = "",
    val color: String = "#000000"
) : WidgetView() {

    override fun buildView(context: Context) = TextView(context)

    override fun onBind(widget: Widget, view: View) {
        (widget as? MutableText)?.let { widget ->
            val widgetView: TextView = view as TextView
            val color = Color.parseColor(widget.color)
            widgetView.text = widget.firstText
            widgetView.setTextColor(color)
            widgetView.setOnClickListener {
                widgetView.text = if (widgetView.text == widget.firstText)
                    widget.secondText
                else
                    widget.firstText
            }
        }
    }
}