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



import br.com.zup.beagle.core.CornerRadiusSchema
import br.com.zup.beagle.widget.core.SizeSchema
import br.com.zup.beagle.widget.core.EdgeValueSchema
import br.com.zup.beagle.widget.core.FlexSchema
import br.com.zup.beagle.widget.core.PositionType
import br.com.zup.beagle.widget.core.Display

interface StyleSchema {
  public val backgroundColor: String?
  public val cornerRadius: CornerRadiusSchema?
  public val size: SizeSchema?
  public val margin: EdgeValueSchema?
  public val padding: EdgeValueSchema?
  public val position: EdgeValueSchema?
  public val positionType: PositionType?
  public val display: Display?
  public val flex: FlexSchema? 
}
