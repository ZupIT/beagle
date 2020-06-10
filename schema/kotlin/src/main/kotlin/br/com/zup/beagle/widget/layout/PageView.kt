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

package br.com.zup.beagle.widget.layout

import br.com.zup.beagle.core.LayoutComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.pager.PageIndicatorComponent

/**
 *  The PageView component is a specialized container to hold pages (views) that will be displayed horizontally.
 *
 * @param pages define a List of components (views) that are contained on this PageView. Consider the
 * @param pageIndicator defines in what page the PageView is currently on.
 *
 */
data class PageView(
    val pages: List<ServerDrivenComponent>,
    val pageIndicator: PageIndicatorComponent? = null
) : ServerDrivenComponent, LayoutComponent
