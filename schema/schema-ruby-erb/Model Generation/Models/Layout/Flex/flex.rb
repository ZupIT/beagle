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
        flex_direction = FlexDirection.new
        flex_wrap = FlexWrap.new
        justify_content = JustifyContent.new
        align_items = AlignItems.new
        align_self = AlignSelf.new
        align_content = AlignContent.new
        
        variables = [
            Variable.new(:name => "flexDirection", :typeName => flex_direction.name, :isOptional => true),
            Variable.new(:name => "flexWrap", :typeName => flex_wrap.name, :isOptional => true),
            Variable.new(:name => "justifyContent", :typeName => justify_content.name, :isOptional => true),
            Variable.new(:name => "alignItems", :typeName => align_items.name, :isOptional => true),
            Variable.new(:name => "alignSelf", :typeName => align_self.name, :isOptional => true),
            Variable.new(:name => "alignContent", :typeName => align_content.name, :isOptional => true),
            Variable.new(:name => "basis", :typeName => UnitValue.new.name, :isOptional => true),
            Variable.new(:name => "flex", :typeName => BasicTypeKeys.double, :isOptional => true),
            Variable.new(:name => "flexDirection", :typeName => BasicTypeKeys.double, :isOptional => true),
            Variable.new(:name => "shrink", :typeName => BasicTypeKeys.double, :isOptional => true),
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :sameFileTypes => [flex_direction, flex_wrap, justify_content, align_items, align_self, align_content]
        )

        super(synthax_type)

    end

end