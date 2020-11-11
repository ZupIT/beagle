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

require_relative '../../Synthax/Attributes/field.rb'
require_relative '../base_component.rb'
require_relative '../../Synthax/Types/built_in_type.rb'

class Style < BaseComponent

    def initialize
        edge_value = EdgeValue.new.name
        variables = [
            Field.new(
                :name => "backgroundColor",
                :typeName => TypesToString.string,
                :isOptional => true,
                :comment => "Set the view background color. Supported formats:  `#RRGGBB[AA]` and `#RGB[A]`."
            ),
            Field.new(
                :name => "cornerRadius",
                :typeName => CornerRadius.new.name,
                :isOptional => true,
                :comment => "Sets the corner of your view to make it round."
            ),
            Field.new(
                :name => "borderColor",
                :typeName => TypesToString.string,
                :isOptional => true,
                :comment => "Sets the color of your view border. Supported formats:  `#RRGGBB[AA]` and `#RGB[A]`."
            ),
            Field.new(
                :name => "borderWidth",
                :typeName => TypesToString.double,
                :isOptional => true,
                :comment => "Sets the width of your view border."
            ),
            Field.new(
                :name => "size",
                :typeName => Size.new.name,
                :isOptional => true,
                :comment => "Allows  you to specify the size of the element."
            ),
            Field.new(
                :name => "margin",
                :typeName => edge_value,
                :isOptional => true,
                :comment => "Allows you to apply a space to the child element."
            ),
            Field.new(
                :name => "padding",
                :typeName => edge_value,
                :isOptional => true,
                :comment => "Allows you to apply a space to the parent element. So when a child is created it starts with padding-defined spacing."
            ),
            Field.new(
                :name => "position",
                :typeName => edge_value,
                :isOptional => true,
                :comment => "Sets the placement of the component in its parent."
            ),
            Field.new(
                :name => "flex",
                :typeName => Flex.new.name,
                :isOptional => true,
                :comment => "Apply positioning using the flex box concept."
            ),
            Field.new(
                :name => "positionType",
                :typeName => PositionType.new.name,
                :isOptional => true,
                :comment => "The position type of an element defines how it is positioned within its parent."
            ),
            Field.new(
                :name => "display",
                :typeName => Display.new.name,
                :isOptional => true,
                :isBindable => true,
                :comment => "Set the display type of the component, allowing o be flexible or locked."
            )
            
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.core",
            :sameFileTypes => [CornerRadius.new, Display.new, PositionType.new]
        )

        super(synthax_type)

    end

end
