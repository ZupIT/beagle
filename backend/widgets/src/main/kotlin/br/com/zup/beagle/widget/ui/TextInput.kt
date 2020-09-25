// THIS IS A GENERATED FILE. DO NOT EDIT THIS
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

import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOfNullable
import br.com.zup.beagle.widget.core.TextInputType

data class TextInput(
    override val value: Bind<String>? = null,
    override val placeholder: Bind<String>? = null,
    override val disabled: Bind<Boolean>? = null,
    override val readOnly: Bind<Boolean>? = null,
    override val type: Bind<TextInputType>? = null,
    override val hidden: Bind<Boolean>? = null,
    override val styleId: String? = null,
    override val onChange: List<Action>? = null,
    override val onBlur: List<Action>? = null,
    override val onFocus: List<Action>? = null
) : Widget(), TextInputSchema {
    constructor (
        value: String? = null,
        placeholder: String? = null,
        disabled: Boolean? = null,
        readOnly: Boolean? = null,
        type: TextInputType? = null,
        hidden: Boolean? = null,
        styleId: String? = null,
        onChange: List<Action>? = null,
        onBlur: List<Action>? = null,
        onFocus: List<Action>? = null
    ) : this(
        valueOfNullable(value),
        valueOfNullable(placeholder),
        valueOfNullable(disabled),
        valueOfNullable(readOnly),
        valueOfNullable(type),
        valueOfNullable(hidden),
        styleId,
        onChange,
        onBlur,
        onFocus
    )
}
