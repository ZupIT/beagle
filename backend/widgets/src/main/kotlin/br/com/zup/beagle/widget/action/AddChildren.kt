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

package br.com.zup.beagle.widget.action

import br.com.zup.beagle.core.ServerDrivenComponent

/**
 * Defines the placement of where the children will be inserted in the list or if the contents
 * of the list will be replaced.
 *
 * @property APPEND
 * @property PREPEND
 * @property REPLACE
 */
enum class Mode {
    /**
     * Adds the view in the end of the children's list.
     */
    APPEND,

    /**
     * Adds the view on the beginning of the children's list.
     */
    PREPEND,

    /**
     * Replaces all children of the widget.
     */
    REPLACE
}

/**
 * The AddChildren class is responsible for adding - at the beginning or in the end - or changing
 * all views that inherit from  Widget  and who accept children.
 *
 * @param componentId Required. Defines the widget's id, in which you want to add the views.
 * @param value Required. Defines the list of children you want to add.
 * @param mode Defines the placement of where the children will be inserted in the list or if the contents of
 * the list will be replaced.
 */
data class AddChildren(
    var componentId: String,
    var value: List<ServerDrivenComponent>,
    var mode: Mode? = Mode.APPEND
) : Action
