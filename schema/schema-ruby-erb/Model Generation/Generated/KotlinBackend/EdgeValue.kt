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

import br.com.zup.beagle.widget.core.UnitValue

data class EdgeValue (
  public override val left: UnitValue?,  
  public override val top: UnitValue?,  
  public override val right: UnitValue?,  
  public override val bottom: UnitValue?,  
  public override val horizontal: UnitValue?,  
  public override val vertical: UnitValue?,  
  public override val all: UnitValue?   
) : EdgeValueSchema {
  constructor (    
    left: UnitValue? = null,      
    top: UnitValue? = null,      
    right: UnitValue? = null,      
    bottom: UnitValue? = null,      
    horizontal: UnitValue? = null,      
    vertical: UnitValue? = null,      
    all: UnitValue? = null      
  ) : this (    
      left,      
      top,      
      right,      
      bottom,      
      horizontal,      
      vertical,      
      all      
  )
}