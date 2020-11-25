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

package br.com.zup.beagle.automatedtests.builders

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.WebView

object WebViewScreenBuilder {
    fun build() = Screen(
        child = Container(
            context = ContextData(id ="WebViewContext", value = "https://google.com"),
            children = listOf(
                Text("WebView screen"),

                Container(children = listOf(
                    Text("WebViewHardcoded"),
                    WebView(url = "https://google.com")
                )).applyStyle(style = Style(
                    margin = EdgeValue(top = UnitValue(10.0, UnitType.REAL)),
                    size = Size(height = UnitValue(250.0, UnitType.REAL)))),

                Container(children = listOf(
                    Text("WebViewExpression"),
                    WebView(url = "@{WebViewContext}")
                )).applyStyle(style = Style(
                    margin = EdgeValue(top = UnitValue(10.0, UnitType.REAL)),
                    size = Size(height = UnitValue(250.0, UnitType.REAL)))),
                Button(
                    text = "ClickToChangePage",
                    onPress = listOf(SetContext("WebViewContext", "https://git-scm.com"))
                )
            )
        )
    )
}