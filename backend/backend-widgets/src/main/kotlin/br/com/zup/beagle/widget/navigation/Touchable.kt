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

package br.com.zup.beagle.widget.navigation

import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.analytics.TouchableAnalytics
import br.com.zup.beagle.core.GhostComponent
import br.com.zup.beagle.core.ServerDrivenComponent

/**
 *   The Touchable component defines a click listener.
 *
 * @param onPress define an Action to be executed when the child component is clicked.
 * @param child define the widget that will trigger the Action.
 * @param clickAnalyticsEvent define the event will triggered when click
 *
 */
data class Touchable(
    val onPress: List<Action>,
    override val child: ServerDrivenComponent,
    override val clickAnalyticsEvent: ClickEvent? = null
) : ServerDrivenComponent, GhostComponent, TouchableAnalytics
