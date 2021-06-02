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
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOfNullable

data class Template(
    /**
     * Condition to tell if this is the template to render or not. Optional. If omitted, we consider it to be the
     * default template, i.e, it's used whenever no other template can be used.
     */
    val case: Bind<Boolean>? = null,
    /**
     * The template itself: view to render
     */
    val view: ServerDrivenComponent,
) {
    constructor(case: Boolean, view: ServerDrivenComponent) : this(valueOfNullable(case), view)
}
