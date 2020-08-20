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

data class Size (
  public override val height: UnitValue?,  
  public override val maxWidth: UnitValue?,  
  public override val maxHeight: UnitValue?,  
  public override val minWidth: UnitValue?,  
  public override val minHeight: UnitValue?,  
  public override val aspectRatio: Enum?   
) : SizeSchema {
  constructor (    
    height: UnitValue? = null,      
    maxWidth: UnitValue? = null,      
    maxHeight: UnitValue? = null,      
    minWidth: UnitValue? = null,      
    minHeight: UnitValue? = null,      
    aspectRatio: Enum? = null      
  ) : this (    
      height,      
      maxWidth,      
      maxHeight,      
      minWidth,      
      minHeight,      
      aspectRatio      
  )
}