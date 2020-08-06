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

package br.com.zup.beagle.ui

import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.builder.analytics.ClickEventBuilder
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.ui.Button
import kotlin.properties.Delegates

fun button(block: ButtonBuilder.() -> Unit) = ButtonBuilder().apply(block).build()

class ButtonBuilder : BeagleBuilder<Button> {
    var text: Bind<String> by Delegates.notNull()
    var styleId: String? = null
    var onPress: MutableList<Action>? = null
    var clickAnalyticsEvent: ClickEvent? = null

    fun text(text: Bind<String>) = this.apply { this.text = text }
    fun styleId(styleId: String?) = this.apply { this.styleId = styleId }
    fun onPress(onPress: List<Action>?) = this.apply { this.onPress = onPress?.toMutableList() }
    fun clickAnalyticsEvent(clickAnalyticsEvent: ClickEvent?)
        = this.apply { this.clickAnalyticsEvent = clickAnalyticsEvent }

    fun text(block: () -> Bind<String>) {
        text(block.invoke())
    }

    fun styleId(block: () -> String?) {
        styleId(block.invoke())
    }

    fun onPress(block: BeagleListBuilder<Action>.() -> Unit) {
        onPress(BeagleListBuilder<Action>().apply(block).buildNullable())
    }

    fun clickAnalyticsEvent(block: ClickEventBuilder.() -> Unit) {
        clickAnalyticsEvent(ClickEventBuilder().apply(block).build())
    }

    override fun build() = Button(
        text = text,
        styleId = styleId,
        onPress = onPress,
        clickAnalyticsEvent = clickAnalyticsEvent
    )
}