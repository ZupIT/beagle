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

public struct Size {
  
  // MARK: - Public Properties  
  public let height: UnitValue?  
  public let maxWidth: UnitValue?  
  public let maxHeight: UnitValue?  
  public let minWidth: UnitValue?  
  public let minHeight: UnitValue?  
  public let aspectRatio: Double?  

  public init(        
    height: UnitValue? = nil,        
    maxWidth: UnitValue? = nil,        
    maxHeight: UnitValue? = nil,        
    minWidth: UnitValue? = nil,        
    minHeight: UnitValue? = nil,        
    aspectRatio: Double? = nil        
  ) {    
      self.height = height    
      self.maxWidth = maxWidth    
      self.maxHeight = maxHeight    
      self.minWidth = minWidth    
      self.minHeight = minHeight    
      self.aspectRatio = aspectRatio    
  }
}