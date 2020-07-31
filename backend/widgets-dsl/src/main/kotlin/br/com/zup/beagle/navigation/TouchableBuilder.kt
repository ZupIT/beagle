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

package br.com.zup.beagle.navigation

import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.builder.analytics.ClickEventBuilder
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.navigation.Touchable
import kotlin.properties.Delegates

fun touchable(block: TouchableBuilder.() -> Unit) = TouchableBuilder().apply(block).build()

class TouchableBuilder : BeagleBuilder<Touchable> {
    var onPress: MutableList<Action> by Delegates.notNull()
    var child: ServerDrivenComponent by Delegates.notNull()
    var clickAnalyticsEvent: ClickEvent? = null

    fun onPress(onPress: List<Action>) = this.apply { this.onPress = onPress.toMutableList() }
    fun child(child: ServerDrivenComponent) = this.apply { this.child = child }
    fun clickAnalyticsEvent(clickAnalyticsEvent: ClickEvent?)
        = this.apply { this.clickAnalyticsEvent = clickAnalyticsEvent }

    fun onPress(block: BeagleListBuilder<Action>.() -> Unit) {
        onPress(BeagleListBuilder<Action>().apply(block).build())
    }

    fun child(block: () -> ServerDrivenComponent) {
        child(block.invoke())
    }

    fun clickAnalyticsEvent(block: ClickEventBuilder.() -> Unit) {
        clickAnalyticsEvent(ClickEventBuilder().apply(block).build())
    }

    override fun build() = Touchable(
        onPress = onPress,
        child = child,
        clickAnalyticsEvent = clickAnalyticsEvent
    )
}