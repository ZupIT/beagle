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

package br.com.zup.beagle.android.components.layout

import br.com.zup.beagle.analytics.ScreenAnalytics
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.SingleChildComponent
import br.com.zup.beagle.core.Style

data class SafeArea(
    val top: Boolean? = null,
    val leading: Boolean? = null,
    val bottom: Boolean? = null,
    val trailing: Boolean? = null
)

data class NavigationBarItem(
    val text: String,
    val image: ImagePath.Local? = null,
    val action: Action,
    val accessibility: Accessibility? = null
) : IdentifierComponent {
    override var id: String? = null
}

data class NavigationBar(
    val title: String,
    val showBackButton: Boolean = true,
    val styleId: String? = null,
    val navigationBarItems: List<NavigationBarItem>? = null,
    val backButtonAccessibility: Accessibility? = null
)

data class Screen(
    val identifier: String? = null,
    val safeArea: SafeArea? = null,
    val navigationBar: NavigationBar? = null,
    override val child: ServerDrivenComponent,
    val style: Style? = null,
    override val screenAnalyticsEvent: ScreenEvent? = null,
    override val context: ContextData? = null
) : ScreenAnalytics, ContextComponent, SingleChildComponent
