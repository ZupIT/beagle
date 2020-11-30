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
require_relative 'unit_value.rb'

class EdgeValue < BaseComponent

    def initialize
        unit_value = UnitValue.new
        variables = [
            Field.new(:name => "left",
                :typeName => unit_value,
                :isOptional => true,
                :comment => "specify the offset the left edge of the item should have from\nit’s closest sibling (item) or parent (container)."
            ),
            Field.new(
                :name => "top",
                :typeName => unit_value,
                :isOptional => true,
                :comment => "specify the offset the top edge of the item should have from\nit’s closest sibling (item) or parent (container)."
            ),
            Field.new(
                :name => "right",
                :typeName => unit_value,
                :isOptional => true,
                :comment => "specify the offset the right edge of the item should have from\nit’s closest sibling (item) or parent (container)."
            ),
            Field.new(
                :name => "bottom",
                :typeName => unit_value,
                :isOptional => true,
                :comment => "specify the offset the bottom edge of the item should have from\nit’s closest sibling (item) or parent (container)."
            ),
            Field.new(
                :name => "horizontal",
                :typeName => unit_value,
                :isOptional => true,
                :comment => "specify the offset the horizontal edge of the item should have from\nit’s closest sibling (item) or parent (container)."
            ),
            Field.new(
                :name => "vertical",
                :typeName => unit_value,
                :isOptional => true,
                :comment => "specify the offset the vertical edge of the item should have from\nit’s closest sibling (item) or parent (container)."
            ),
            Field.new(
                :name => "all",
                :typeName => unit_value,
                :isOptional => true,
                :comment => "specify the offset the all edge of the item should have from\nit’s closest sibling (item) or parent (container)."
            )
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :comment => "specify the offset the edge of the item should have from it’s closest sibling (item) or parent (container)"
        )

        super(synthax_type)

    end

end