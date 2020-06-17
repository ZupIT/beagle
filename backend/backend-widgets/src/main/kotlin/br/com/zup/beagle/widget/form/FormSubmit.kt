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

import br.com.zup.beagle.core.GhostComponent
import br.com.zup.beagle.core.ServerDrivenComponent

/**
 * component will define a submit handler for a form.
 *
 * @param child
 *                  define the submit handler.
 *                  It is generally set as a button to be clicked after a form is filled up.
 * @param enabled
 *                  define as "true" by default and it will enable the button to be clicked on.
 *                  If it is defined as "false" the button will start as "disabled"
 *
 */
data class FormSubmit(
    override val child: ServerDrivenComponent,
    val enabled: Boolean = true
) : ServerDrivenComponent, GhostComponent