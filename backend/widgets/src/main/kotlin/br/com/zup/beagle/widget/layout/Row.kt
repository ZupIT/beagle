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
import br.com.zup.beagle.ext.Styled
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.core.FlexDirection
import com.sun.rowset.internal.Row

fun Row(
    context: ContextData? = null,
    onInit: List<Action>? = null,
    reverse: Boolean = false,
    children: List<ServerDrivenComponent> = listOf(),
): Container {
    return Styled(Container(
        children = children,
        context = context,
        onInit = onInit
    ), {
        flex.flexDirection = if (!reverse) FlexDirection.ROW else FlexDirection.ROW_REVERSE
        flex.grow = 1.0
    })
}