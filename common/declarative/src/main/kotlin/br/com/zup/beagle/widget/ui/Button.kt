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

import br.com.zup.beagle.action.Action
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.analytics.TouchableAnalytics
import br.com.zup.beagle.enums.BeaglePlatform
import br.com.zup.beagle.widget.Widget

data class Button(
    val text: String,
    val style: String? = null,
    val action: Action? = null,
    override val clickAnalyticsEvent: ClickEvent? = null,
    override val beaglePlatform: BeaglePlatform = BeaglePlatform.ALL
) : Widget(), TouchableAnalytics