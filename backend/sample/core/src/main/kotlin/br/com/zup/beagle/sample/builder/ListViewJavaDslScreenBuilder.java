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

package br.com.zup.beagle.sample.builder;

import br.com.zup.beagle.core.Style;
import br.com.zup.beagle.widget.action.Alert;
import br.com.zup.beagle.widget.context.Bind;
import br.com.zup.beagle.widget.core.EdgeValue;
import br.com.zup.beagle.widget.core.ListDirection;
import br.com.zup.beagle.widget.core.ScrollAxis;
import br.com.zup.beagle.widget.core.UnitType;
import br.com.zup.beagle.widget.core.UnitValue;
import br.com.zup.beagle.widget.layout.Container;
import br.com.zup.beagle.widget.layout.NavigationBar;
import br.com.zup.beagle.widget.layout.NavigationBarItem;
import br.com.zup.beagle.widget.layout.Screen;
import br.com.zup.beagle.widget.layout.ScreenBuilder;
import br.com.zup.beagle.widget.layout.ScrollView;
import br.com.zup.beagle.widget.ui.ImagePath;
import br.com.zup.beagle.widget.ui.ListView;
import br.com.zup.beagle.widget.ui.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static br.com.zup.beagle.ext.WidgetExtensionsKt.applyStyle;

public class ListViewJavaDslScreenBuilder implements ScreenBuilder {


    @NotNull
    @Override
    public Screen build() {
        return Screen.builder().navigationBar(
            NavigationBar.builder()
                .title("Beagle ListView")
                .showBackButton(true)
                .navigationBarItems(
                    List.of(NavigationBarItem.builder()
                        .text("")
                        .image(
                            ImagePath.Local.builder().mobileId("informationImage").build()
                        )
                        .action(
                            Alert.builder()
                                .title(new Bind.Value<>("ListView"))
                                .message(new Bind.Value<>("Is a Layout component that will define a list of views " +
                                    "natively. These views could be any Server Driven Component."))
                                .labelOk("OK").build()
                        ).build())
                ).build()
        ).child(
            ScrollView.builder()
                .scrollDirection(ScrollAxis.VERTICAL)
                .children(List.of(
                    getStaticListView(ListDirection.VERTICAL),
                    getStaticListView(ListDirection.HORIZONTAL),
                    getDynamicListView(ListDirection.VERTICAL),
                    getDynamicListView(ListDirection.HORIZONTAL)
                )).build()
        ).build();
    }

    private Container getStaticListView(ListDirection listDirection){
        return applyStyle(Container.builder().children(
            List.of(
                applyStyle(
                    Text.builder().text(new Bind.Value<>("Static " + listDirection + " ListView")).build(),
                    Style.builder().margin(
                        EdgeValue.builder().bottom(new UnitValue(10, UnitType.REAL)
                        ).build()).build()
                ),
                ListView.builder().direction(listDirection).children(
                    List.of(
                        createText(1),
                        createText(2),
                        createText(3),
                        createText(4),
                        createText(5),
                        createText(6),
                        createText(7),
                        createText(8),
                        createText(9),
                        createText(10)
                    )
                ).build()
            )
        ).build(),
            Style.builder().margin(
                EdgeValue.builder().bottom(UnitValue.builder().real(20).build()).build()
            ).build());
    }

    private Container getDynamicListView(ListDirection listDirection) {
        return Container.builder().children(
            List.of(
                applyStyle(
                    Text.builder().text(new Bind.Value<>("Dynamic " + listDirection + " ListView")).build(),
                    Style.builder().margin(
                        EdgeValue.builder().bottom(new UnitValue(10, UnitType.REAL)).build()
                    ).build()
                ),
                ListView.builder().direction(listDirection).children(
                    List.of(
                        createText(1),
                        createText(2),
                        createText(3),
                        createText(4),
                        createText(5),
                        createText(6),
                        createText(7),
                        createText(8),
                        createText(9),
                        createText(10),
                        createText(11),
                        createText(12),
                        createText(13),
                        createText(14),
                        createText(15),
                        createText(16),
                        createText(17),
                        createText(18),
                        createText(19)
                    )
                ).build()
            )
        ).build();
    }

    private Text createText(int index) {
        return Text.builder().text(new Bind.Value<>("Hello " + index)).build();
    }
}
