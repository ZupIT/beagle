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

package br.com.zup.beagle.sample.fragment.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.ListView
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.setId
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.Size

class ListViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val declarative = Screen(
            navigationBar = NavigationBar(title = "List"),
            child = buildListView()
        )
        setContentView(declarative.toView(this))
    }

    private fun buildListView() = ListView(
        context = ContextData(
            id = "outsideContext",
            value = listOf("0 OUTSIDE", "1 OUTSIDE", "2 OUTSIDE", "3 OUTSIDE", "4 OUTSIDE", "5 OUTSIDE",
                "6 OUTSIDE", "7 OUTSIDE", "8 OUTSIDE", "9 OUTSIDE", "10 OUTSIDE",
                "11 OUTSIDE", "12 OUTSIDE", "13 OUTSIDE", "14 OUTSIDE", "15 OUTSIDE",
                "16 OUTSIDE", "17 OUTSIDE", "18 OUTSIDE", "19 OUTSIDE", "20 OUTSIDE")
        ),
        dataSource = expressionOf("@{outsideContext}"),
        direction = ListDirection.VERTICAL,
        template = Container(
            children = listOf(
                Text(text = expressionOf("@{item}")),
                list
            )
        ).applyStyle(
            Style(
                size = Size(width = 100.unitPercent(), height = 600.unitReal())
            )
        )
    )

    data class Person(
        val name: String,
        val cpf: Int
    )

    private val list = ListView(
        context = ContextData(
            id = "insideContext",
            value = listOf(
                Person(
                    "John",
                    0
                ),
                Person(
                    "Carter",
                    1
                ),
                Person(
                    "Josie",
                    2
                ),
                Person(
                    "Dimitri",
                    3
                ),
                Person(
                    "Maria",
                    4
                ),
                Person(
                    "Max",
                    5
                ),
                Person(
                    "Kane",
                    6
                ),
                Person(
                    "Amelia",
                    7
                ),
                Person(
                    "Jose",
                    8
                ),
                Person(
                    "Percy",
                    9
                ),
                Person(
                    "Karen",
                    10
                ),
                Person(
                    "Sol",
                    11
                ),
                Person(
                    "Jacques",
                    12
                ),
                Person(
                    "Stephen",
                    13
                ),
                Person(
                    "Sullivan",
                    14
                ),
                Person(
                    "Zoe",
                    15
                )
            )
        ),
        key = "cpf",
        dataSource = expressionOf("@{insideContext}"),
        direction = ListDirection.HORIZONTAL,
        template = Container(
            children = listOf(
                Button(
                    text = expressionOf("@{item.name} - @{item.cpf}"),
                    onPress = listOf(
                        SetContext(
                            contextId = "insideContext",
                            path = "[0].name",
                            value = "Updated John"
                        )
                    )
                ).applyStyle(
                    Style(
                        size = Size(width = 300.unitReal(), height = 80.unitReal())
                    )
                )
            )
        )
    ).applyStyle(
        Style(
            backgroundColor = "#CCC"
        )
    ).setId(id = "innerList")
}
