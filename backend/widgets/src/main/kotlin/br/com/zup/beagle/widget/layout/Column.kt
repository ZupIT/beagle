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

/**
 *  The Column component displays its children in a vertical array.
 *
 * @param children define a list of components that are rendered.
 * @param context define the contextData that be set to column.
 * @param onInit it is a parameter that allows you to define a list of actions to be performed
 * when the Widget is displayed.
 * @param reverse define direction of column
 */
@Suppress("FunctionNaming")
fun Column(
    context: ContextData? = null,
    onInit: List<Action>? = null,
    reverse: Boolean = false,
    styleId: String? = null,
    children: List<ServerDrivenComponent>? = null,
): Container {
    return Styled(
        Container(
            children = children,
            context = context,
            onInit = onInit,
            styleId = styleId,
        )
    ) {
        flex.flexDirection = if (!reverse) FlexDirection.COLUMN else FlexDirection.COLUMN_REVERSE
    }
}
