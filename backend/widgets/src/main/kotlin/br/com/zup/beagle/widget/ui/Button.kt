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
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.context.valueOfNullable

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
}