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

package br.com.zup.beagle.widget.core

import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.context.valueOfNullable
import br.com.zup.beagle.action.ui.Action

data class Alert (
  public override val title: Bind<String>? = null,  
  public override val message: Bind<String>,  
  public override val onPressOk: ActionSchema? = null,  
  public override val labelOk: String? = null   
) : Action, AlertSchema {
  constructor (    
    title: String? = null,      
    message: String,      
    onPressOk: ActionSchema? = null,      
    labelOk: String? = null      
  ) : this (    
      valueOfNullable(title),      
      valueOf(message),      
      onPressOk,      
      labelOk      
  )
}