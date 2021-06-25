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

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.ContextComponent
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.valueOfNullable
import com.fasterxml.jackson.annotation.JsonProperty

data class PullToRefresh(
    override val context: ContextData? = null,
    val onPull: List<Action>,
    @get:JsonProperty("isRefreshing") val isRefreshing: Bind<Boolean>? = null,
    val color: Bind<String>? = null,
    val child: ServerDrivenComponent,
) : Widget(), ContextComponent {

    constructor(
        context: ContextData? = null,
        onPull: List<Action>,
        isRefreshing: Bind<Boolean>? = null,
        color: String? = null,
        child: ServerDrivenComponent,
    ) : this(
        context = context,
        onPull = onPull,
        isRefreshing = isRefreshing,
        color = valueOfNullable(color),
        child = child
    )
}