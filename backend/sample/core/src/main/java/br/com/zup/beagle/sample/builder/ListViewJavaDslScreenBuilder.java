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

import br.com.zup.beagle.action.AlertBuilder;
import br.com.zup.beagle.builder.core.StyleBuilder;
import br.com.zup.beagle.builder.widget.EdgeValueBuilder;
import br.com.zup.beagle.builder.widget.UnitValueBuilder;
import br.com.zup.beagle.layout.ContainerBuilder;
import br.com.zup.beagle.layout.NavigationBarBuilder;
import br.com.zup.beagle.layout.NavigationBarItemBuilder;
import br.com.zup.beagle.layout.ScreenWidgetBuilder;
import br.com.zup.beagle.layout.ScrollViewBuilder;
import br.com.zup.beagle.ui.ImagePathLocalBuilder;
import br.com.zup.beagle.ui.ListViewBuilder;
import br.com.zup.beagle.ui.TextBuilder;
import br.com.zup.beagle.widget.context.Bind;
import br.com.zup.beagle.widget.core.ListDirection;
import br.com.zup.beagle.widget.core.ScrollAxis;
import br.com.zup.beagle.widget.core.UnitType;
import br.com.zup.beagle.widget.core.UnitValue;
import br.com.zup.beagle.widget.layout.Container;
import br.com.zup.beagle.widget.layout.Screen;
import br.com.zup.beagle.widget.layout.ScreenBuilder;
import br.com.zup.beagle.widget.ui.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static br.com.zup.beagle.ext.WidgetExtensionsKt.applyStyle;

public class ListViewJavaDslScreenBuilder implements ScreenBuilder {


    @NotNull
    @Override
    public Screen build() {
        return new ScreenWidgetBuilder().navigationBar(
            new NavigationBarBuilder()
                .title("Beagle ListView")
                .showBackButton(true)
                .navigationBarItems(
                    List.of(new NavigationBarItemBuilder()
                        .text("")
                        .image(
                            new ImagePathLocalBuilder().justMobile("informationImage").build()
                        )
                        .action(
                            new AlertBuilder()
                                .title(new Bind.Value<>("ListView"))
                                .message(new Bind.Value<>("Is a Layout component that will define a list of views " +
                                    "natively. These views could be any Server Driven Component."))
                                .labelOk("OK").build()
                        ).build())
                ).build()
        ).child(
            new ScrollViewBuilder()
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
        return applyStyle(new ContainerBuilder().children(
            List.of(
                applyStyle(
                    new TextBuilder().text(new Bind.Value<>("Static " + listDirection + " ListView")).build(),
                    new StyleBuilder().margin(
                        new EdgeValueBuilder().bottom(new UnitValue(10, UnitType.REAL)
                        ).build()).build()
                ),
                new ListViewBuilder().direction(listDirection).children(
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
            new StyleBuilder().margin(
                new EdgeValueBuilder().bottom(new UnitValueBuilder().real(20).build()).build()
            ).build());
    }

    private Container getDynamicListView(ListDirection listDirection) {
        return new ContainerBuilder().children(
            List.of(
                applyStyle(
                    new TextBuilder().text(new Bind.Value<>("Dynamic " + listDirection + " ListView")).build(),
                    new StyleBuilder().margin(
                        new EdgeValueBuilder().bottom(new UnitValue(10, UnitType.REAL)).build()
                    ).build()
                ),
                new ListViewBuilder().direction(listDirection).children(
                    List.of(
                        createText(0),
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
        return new TextBuilder().text(new Bind.Value<>("Hello " + index)).build();
    }
}
