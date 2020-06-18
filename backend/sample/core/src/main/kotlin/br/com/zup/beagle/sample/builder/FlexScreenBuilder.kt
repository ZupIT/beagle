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
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.FlexPositionType
import br.com.zup.beagle.widget.core.Position
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.ui.Text


object FlexScreenBuilder: ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Flex",
            showBackButton = true
        ),
        child = Container(
            children = listOf(
               Text(
                   text = "1",
                   alignment = TextAlignment.CENTER
               ).applyFlex(flex = Flex(
                   positionType = FlexPositionType.ABSOLUTE,
                   position = Position(all = 50.unitReal(), horizontal = 100.unitReal(), top = 45.unitReal()),
                   size = Size(width = 33.33.unitPercent(),height = 50.unitReal()))
               ).applyStyle(style = Style(backgroundColor = "#142850")),
                Text(
                    text = "2",
                    alignment = TextAlignment.CENTER
                ).applyFlex(flex = Flex(
                    size = Size(width = 33.33.unitPercent(),height = 50.unitReal()))
                ).applyStyle(style = Style(backgroundColor = "#dd7631")),
                Text(
                    text = "3",
                    alignment = TextAlignment.CENTER
                ).applyFlex(flex = Flex(
                    size = Size(width = 33.33.unitPercent(),height = 50.unitReal()))
                ).applyStyle(style = Style(backgroundColor = "#649d66"))
            )
        ).applyFlex(flex = Flex(
            flexDirection = FlexDirection.ROW
        )).applyStyle(style = Style(backgroundColor = "#000000"))
    )
}