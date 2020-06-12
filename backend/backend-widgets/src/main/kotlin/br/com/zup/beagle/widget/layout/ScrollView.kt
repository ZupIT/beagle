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

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.ScrollAxis

/**
 * Component is a specialized container that will display its components in a Scroll
 *
 * @param children define a list of components to be displayed on this view.
 * @param scrollDirection define the scroll roll direction on screen.
 * @param scrollBarEnabled determine if the Scroll bar is displayed or not. It is displayed by default.
 *
 */
data class ScrollView(
    val children: List<ServerDrivenComponent>,
    val scrollDirection: ScrollAxis? = null,
    val scrollBarEnabled: Boolean? = null
) : ServerDrivenComponent