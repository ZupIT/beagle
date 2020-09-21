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

import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.context.valueOfNullable
import br.com.zup.beagle.widget.core.TextInputType

data class TextInput (
  public override val value: Bind<String>?,  
  public override val placeholder: Bind<String>?,  
  public override val disabled: Bind<Boolean>?,  
  public override val readOnly: Bind<Boolean>?,  
  public override val type: Bind<TextInputType>?,  
  public override val hidden: Bind<Boolean>?,  
  public override val styleId: String?,  
  public override val onChange: List<Action>?,  
  public override val onBlur: List<Action>?,  
  public override val onFocus: List<Action>?   
) : TextInputSchema {
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
  ) : this (    
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