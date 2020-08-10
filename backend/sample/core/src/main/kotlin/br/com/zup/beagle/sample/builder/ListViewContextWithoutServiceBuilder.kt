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

package br.com.zup.beagle.sample.builder

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.CYAN_GREEN
import br.com.zup.beagle.sample.constants.RED_ORANGE
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.Text

object ListViewContextWithoutServiceBuilder : ScreenBuilder {
    override fun build() = Screen(
        child = Container(
            children = listOf(
                buildListView()
            )
        )
    )

    private fun buildListView() = listOut

    val listIn = ListView(
        context = ContextData(
            id = "inContext",
            value = listOf(
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro",
                "dentro"
            )
        ),
        direction = ListDirection.HORIZONTAL,
        dataSource = expressionOf("@{inContext}"),
        template = Text(text = expressionOf("@{item}"))
    )

    val listOut = ListView(
        context = ContextData(
            id = "outContext",
            value = listOf(
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora",
                "fora"
            )
        ),
        direction = ListDirection.VERTICAL,
        dataSource = expressionOf("@{outContext}"),
        template = Container(
            listOf(
                Text("@{item}"),
                listIn.applyStyle(Style(backgroundColor = RED_ORANGE))
            )
        ).applyStyle(Style(backgroundColor = CYAN_GREEN, size = Size(width = 100.unitPercent(), height = 50.unitReal()))
        )
    )
}