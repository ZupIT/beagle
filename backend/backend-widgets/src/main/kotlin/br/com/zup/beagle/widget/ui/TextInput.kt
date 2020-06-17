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
import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.core.TextInputType

data class TextInput(
    val value: String,
    val placeholder: String,
    val disabled: Boolean,
    val readOnly: Boolean, // on android and iOS this attribute have the same effect as disabled
    val type: TextInputType,
    val hidden: Boolean,
    val styleId: String,
    val onChange: List<Action>? = null,
    val onBlur: List<Action>? = null,
    val onFocus: List<Action>? = null
) : Widget()