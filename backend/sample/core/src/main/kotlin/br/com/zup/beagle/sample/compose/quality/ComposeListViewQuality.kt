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

package br.com.zup.beagle.sample.compose.quality

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.BEACH_NETWORK_IMAGE
import br.com.zup.beagle.sample.constants.SCREEN_ACTION_CLICK_ENDPOINT
import br.com.zup.beagle.widget.action.Navigate
import br.com.zup.beagle.widget.action.Route
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.layout.extensions.dynamic
import br.com.zup.beagle.widget.navigation.Touchable
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.NetworkImage
import br.com.zup.beagle.widget.ui.Text

object ComposeListViewQuality: ComposeComponent {
    override fun build() = ScrollView(
        scrollDirection = ScrollAxis.VERTICAL,
        children = listOf(
            NetworkImage(path = BEACH_NETWORK_IMAGE).applyFlex(Flex(alignSelf = AlignSelf.FLEX_START)),
            getStaticListView(ListDirection.VERTICAL).applyFlex(Flex(alignItems = AlignItems.CENTER)),
            getStaticListView(ListDirection.HORIZONTAL).applyFlex(Flex(alignItems = AlignItems.CENTER)),
            Container(
                children = listOf(
                    getDynamicListView(ListDirection.VERTICAL).applyFlex(Flex(alignItems = AlignItems.FLEX_END)),
                    getDynamicListView(ListDirection.HORIZONTAL).applyFlex(Flex(alignItems = AlignItems.FLEX_START))
                )
            ).applyStyle(
                style = Style(
                    backgroundColor = "#4682B4",
                    size = Size(width = 300.unitReal(),height = 300.unitReal()),
                    flex = Flex(alignSelf = AlignSelf.CENTER)
                )
            ),

            Container(
                children = listOf(
                    ListView(
                        direction = ListDirection.VERTICAL,
                        children = listOf(
                            Text("Text1 with networkImage"),
                            NetworkImage(path = BEACH_NETWORK_IMAGE).applyFlex(Flex(alignSelf = AlignSelf.FLEX_START)),
                            Text("Text2 with touchable"),
                            Touchable(
                                child = Image(name ="imageBeagle"),
                                action = Navigate.PushView(Route.Remote(SCREEN_ACTION_CLICK_ENDPOINT))
                            ),
                            Text("Text3"),
                            Image(name = "imageBeagle").applyFlex(Flex(alignSelf = AlignSelf.FLEX_END)),
                            Text("Text4"),
                            Image(name = "imageBeagle"),
                            Text("Text5"),
                            Image(name = "imageBeagle"),
                            Text("Text6"),
                            Image(name = "imageBeagle"),
                            Text("final list view um"),
                            ListView(
                                direction = ListDirection.VERTICAL,
                                children = listOf(
                                    Text("segundo "),
                                    Text("segundo"),
                                    Touchable(
                                        child = Text("segundo"),
                                        action = Navigate.PushView(Route.Remote(SCREEN_ACTION_CLICK_ENDPOINT))
                                    )
                                )
                            )
                        )
                    )
                )
            )

        )
    )

    private fun getStaticListView(listDirection: ListDirection) = Container(
        children = listOf(
            Text("Static $listDirection ListView")
                .applyStyle(Style(
                    margin = EdgeValue(bottom = 10.unitReal(),top = 30.unitReal())
                )),
            ListView(children = (1..5).map(this::createText), direction = listDirection)
        )
    ).applyStyle(Style(
        margin = EdgeValue(bottom = 20.unitReal())
    ))

    private fun getDynamicListView(listDirection: ListDirection) = Container(
        children = listOf(
            Text("Dynamic $listDirection ListView")
                .applyStyle(Style(
                    margin = EdgeValue(bottom = 10.unitReal(),top = 30.unitReal())
                )),
            ListView.dynamic(size = 5, direction = listDirection, rowBuilder = this::createText)
        )
    )

    private fun createText(index: Int) = Text(text = "Lorem Ipsum is simply dummy text of the printing and " +
        "typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
        "when an unknown printer took a galley of type and scrambled it to make a type specimen book.")
}