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

package br.com.zup.beagle.widget.ui

import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.analytics.TouchableAnalytics
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.builder.BeagleBuilder
import br.com.zup.beagle.widget.builder.BeagleListBuilder
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.context.valueOfNullable
import kotlin.properties.Delegates

/**
 * Define a button natively using the server driven information received through Beagle
 *
 * @param text define the button text content.
 * @param styleId reference a native style in your local styles file to be applied on this button.
 * @param onPress attribute to define action when onPress
 * @property clickAnalyticsEvent attribute to define click event name
 *
 */
data class Button(
    val text: Bind<String>,
    val styleId: String? = null,
    val onPress: List<Action>? = null,
    override val clickAnalyticsEvent: ClickEvent? = null
) : Widget(), TouchableAnalytics {
    constructor(
        text: String,
        styleId: String? = null,
        onPress: List<Action>? = null,
        clickAnalyticsEvent: ClickEvent? = null
    ) : this(
        valueOf(text),
        styleId,
        onPress,
        clickAnalyticsEvent
    )

    class Builder : BeagleBuilder<Button> {
        var text: Bind<String> by Delegates.notNull()
        var styleId: String? = null
        var onPress: List<Action>? = null
        var clickAnalyticsEvent: ClickEvent? = null

        fun text(text: Bind<String>) = this.apply { this.text = text }
        fun styleId(styleId: String?) = this.apply { this.styleId = styleId }
        fun onPress(onPress: List<Action>?) = this.apply { this.onPress = onPress }
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

        fun clickAnalyticsEvent(block: () -> ClickEvent?) {
            clickAnalyticsEvent(block.invoke())
        }

        override fun build() = Button(
            text = text,
            styleId = styleId,
            onPress = onPress,
            clickAnalyticsEvent = clickAnalyticsEvent
        )
    }
}

fun button(block: Button.Builder.() -> Unit) = Button.Builder().apply(block).build()