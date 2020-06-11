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
 *  this class works like a regular input type in HTML.
 *  It will handle data input by the user on a screen to submit, for example, a user name in a login screen.
 *
 * @param name
 *                  attribute will define the input name tag on this item.
 *                  This is the tag name used when a request is made using a form component.
 * @param required defines if it is required to fill this field.
 * @param validator
 *                      define a string value set in your local pre-configured Validators
 *                      to check if the form input is valid.
 * @param errorMessage that is showed to the user if the validation fails.
 * @param child
 *                  Any Widget that conforms to InputWidget will do here.
 *                  It could be an EditText view in Android, a Radio button in HTML,
 *                  an UITextField in iOS or any other type of view that can receive and store input from users.
 *
 */
open class FormInput(
    open val name: String,
    open val required: Boolean? = null,
    open val validator: String? = null,
    open val errorMessage: String? = null,
    open override val child: InputWidget
) : ServerDrivenComponent, GhostComponent