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

data class Flex (
  public override val flexDirection: FlexDirection?,  
  public override val flexWrap: FlexWrap?,  
  public override val justifyContent: JustifyContent?,  
  public override val alignItems: AlignItems?,  
  public override val alignSelf: AlignSelf?,  
  public override val alignContent: AlignContent?,  
  public override val basis: UnitValue?,  
  public override val flex: Double?,  
  public override val flexDirection: Double?,  
  public override val shrink: Double?   
) : FlexSchema {
  constructor (    
    flexDirection: FlexDirection? = null,      
    flexWrap: FlexWrap? = null,      
    justifyContent: JustifyContent? = null,      
    alignItems: AlignItems? = null,      
    alignSelf: AlignSelf? = null,      
    alignContent: AlignContent? = null,      
    basis: UnitValue? = null,      
    flex: Double? = null,      
    flexDirection: Double? = null,      
    shrink: Double? = null      
  ) : this (    
      flexDirection,      
      flexWrap,      
      justifyContent,      
      alignItems,      
      alignSelf,      
      alignContent,      
      basis,      
      flex,      
      flexDirection,      
      shrink      
  )
}