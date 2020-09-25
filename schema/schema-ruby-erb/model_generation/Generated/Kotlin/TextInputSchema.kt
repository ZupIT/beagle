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



import br.com.zup.beagle.core.BindAttribute
import br.com.zup.beagle.action.ui.ActionSchema
import br.com.zup.beagle.widget.core.TextInputType

interface TextInputSchema {
  public val value: BindAttribute<String>?
  public val placeholder: BindAttribute<String>?
  public val disabled: BindAttribute<Boolean>?
  public val readOnly: BindAttribute<Boolean>?
  public val type: BindAttribute<TextInputType>?
  public val hidden: BindAttribute<Boolean>?
  public val styleId: String?
  public val onChange: List<ActionSchema>?
  public val onBlur: List<ActionSchema>?
  public val onFocus: List<ActionSchema>? 
}
