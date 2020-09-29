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
import br.com.zup.beagle.widget.core.TabBarItem
import br.com.zup.beagle.action.ui.Action

data class TabBar (
  public override val items: List<TabBarItemSchema>,  
  public override val styleId: String? = null,  
  public override val currentTab: Bind<Int>? = null,  
  public override val onTabSelection: List<ActionSchema>? = null   
) : ServerDrivenComponent, TabBarSchema {
  constructor (    
    items: List<TabBarItemSchema>,      
    styleId: String? = null,      
    currentTab: Int? = null,      
    onTabSelection: List<ActionSchema>? = null      
  ) : this (    
      items,      
      styleId,      
      valueOfNullable(currentTab),      
      onTabSelection      
  )
}