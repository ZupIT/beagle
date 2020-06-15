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
import br.com.zup.beagle.widget.action.Action

/**
 *  Component represents a way to compose user inputs and submit  those values to your backend.
 *
 * @param onSubmit define the actions you want to be when click in the form.
 * @param child  you should provide an hierarchy of visual components on which form will act upon.
 * It's important to have somewhere in your component hierarchy input components
 * FormInput and a submit component FormSubmit.
 * @see FormInput
 * @see FormSubmit
 *
 */
data class Form(
    val child: ServerDrivenComponent,
    val onSubmit: List<Action>? = null,
    val group: String? = null,
    val shouldStoreFields: Boolean = false
) : ServerDrivenComponent
