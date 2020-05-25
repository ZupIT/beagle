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

package br.com.zup.beagle.action.components

import br.com.zup.beagle.widget.core.Action

/**
 * Component that hold the actions on click
 * @property onChange attribute to define actions when change the input
 * @property onFocus attribute to define actions when input focused
 * @property onBlur attribute to define actions when input lost the focus
 */
interface InputActionsComponent {
    val onChange: List<Action>?
    val onFocus: List<Action>?
    val onBlur: List<Action>?
}
