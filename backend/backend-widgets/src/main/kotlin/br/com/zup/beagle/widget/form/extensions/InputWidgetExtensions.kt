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

package br.com.zup.beagle.widget.form.extensions

import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.form.InputWidget

/**
 * Update list of actions to on change to this widget.
 * @return the current widget
 */
fun <T : InputWidget> T.setOnChange(actions: List<Action>) = this.apply { this.onChange = actions }

/**
 * Update list of actions to on focus to this widget.
 * @return the current widget
 */
fun <T : InputWidget> T.setOnFocus(actions: List<Action>) = this.apply { this.onFocus = actions }

/**
 * Update list of actions to on focus to this widget.
 * @return the current widget
 */
fun <T : InputWidget> T.setOnBlur(actions: List<Action>) = this.apply { this.onBlur = actions }