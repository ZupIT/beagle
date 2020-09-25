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

package br.com.zup.beagle.core

import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.PositionType
import br.com.zup.beagle.widget.core.Display

data class Style (
  public override val backgroundColor: String? = null,  
  public override val cornerRadius: CornerRadiusSchema? = null,  
  public override val size: SizeSchema? = null,  
  public override val margin: EdgeValueSchema? = null,  
  public override val padding: EdgeValueSchema? = null,  
  public override val position: EdgeValueSchema? = null,  
  public override val positionType: PositionType? = null,  
  public override val display: Display? = null,  
  public override val flex: FlexSchema? = null   
) : StyleSchema 