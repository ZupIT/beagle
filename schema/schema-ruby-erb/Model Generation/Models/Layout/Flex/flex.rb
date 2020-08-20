#   Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA

#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
 
#       http://www.apache.org/licenses/LICENSE-2.0
 
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

require_relative '../../../Synthax/Attributes/variable.rb'
require_relative '../../base_component.rb'
require_relative '../../../Synthax/Types/built_in_type.rb'
require_relative './flex_direction.rb'
require_relative './flex_wrap.rb'
require_relative './justify_content.rb'
require_relative './align_items.rb'
require_relative './align_self.rb'
require_relative './align_content.rb'

class Flex < BaseComponent

    def initialize
        flexDirection = FlexDirection.new
        flexWrap = FlexWrap.new
        justifyContent = JustifyContent.new
        alignItems = AlignItems.new
        alignSelf = AlignSelf.new
        alignContent = AlignContent.new
        
        textVariables = [
            Variable.new(:name => "flexDirection", :typeName => flexDirection.name, :isOptional => true),
            Variable.new(:name => "flexWrap", :typeName => flexWrap.name, :isOptional => true),
            Variable.new(:name => "justifyContent", :typeName => justifyContent.name, :isOptional => true),
            Variable.new(:name => "alignItems", :typeName => alignItems.name, :isOptional => true),
            Variable.new(:name => "alignSelf", :typeName => alignSelf.name, :isOptional => true),
            Variable.new(:name => "alignContent", :typeName => alignContent.name, :isOptional => true),
            Variable.new(:name => "basis", :typeName => UnitValue.new.name, :isOptional => true),
            Variable.new(:name => "flex", :typeName => "Double", :isOptional => true),
            Variable.new(:name => "flexDirection", :typeName => "Double", :isOptional => true),
            Variable.new(:name => "shrink", :typeName => "Double", :isOptional => true),
        ]
        synthaxType = BuiltInType.new(
            :name => self.name,
            :variables => textVariables,
            :package => "br.com.zup.beagle.widget.core",
            :sameFileTypes => [flexDirection, flexWrap, justifyContent, alignItems, alignSelf, alignContent]
        )

        super(synthaxType)

    end

end