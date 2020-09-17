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

import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Display


data class Style (
  public override val backgroundColor: String?,  
  public override val cornerRadius: String?,  
  public override val size: Size?,  
  public override val margin: EdgeValue?,  
  public override val padding: EdgeValue?,  
  public override val position: EdgeValue?,  
  public override val flex: Flex?,  
  public override val positionType: PositionType?,  
  public override val display: Display?   
) : StyleSchema {
  constructor (    
    backgroundColor: String? = null,      
    cornerRadius: String? = null,      
    size: Size? = null,      
    margin: EdgeValue? = null,      
    padding: EdgeValue? = null,      
    position: EdgeValue? = null,      
    flex: Flex? = null,      
    positionType: PositionType? = null,      
    display: Display? = null      
  ) : this (    
      backgroundColor,      
      cornerRadius,      
      size,      
      margin,      
      padding,      
      position,      
      flex,      
      positionType,      
      display      
  )
}