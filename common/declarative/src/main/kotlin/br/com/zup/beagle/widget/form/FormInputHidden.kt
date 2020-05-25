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

package br.com.zup.beagle.widget.form

import br.com.zup.beagle.core.ServerDrivenComponent

/**
 * represents the  <input type="hidden"> in a form.
 * It lets you include data that cannot be seen or modified by users when a form is submitted.
 * This item is not rendered on screen.
 *
 * @param name define this form item name and the key to retrieve its value when submitted inside a form.
 * @param value contain the data you wish to transfer.
 *
 */
data class FormInputHidden(
    val name: String,
    val value: String
) : ServerDrivenComponent