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

data class SendRequest (
  public override val url: Bind<String>,  
  public override val method: Bind<HTTPMethod>? = null,  
  public override val data: Any? = null,  
  public override val headers: Bind<Map<String, String>>? = null,  
  public override val onSuccess: List<ActionSchema>? = null,  
  public override val onError: List<ActionSchema>? = null,  
  public override val onFinish: List<ActionSchema>? = null   
) : Action, SendRequestSchema {
  constructor (    
    url: String,      
    method: HTTPMethod? = null,      
    data: Any? = null,      
    headers: Map<String, String>? = null,      
    onSuccess: List<ActionSchema>? = null,      
    onError: List<ActionSchema>? = null,      
    onFinish: List<ActionSchema>? = null      
  ) : this (    
      valueOf(url),      
      valueOfNullable(method),      
      data,      
      valueOfNullable(headers),      
      onSuccess,      
      onError,      
      onFinish      
  )
}