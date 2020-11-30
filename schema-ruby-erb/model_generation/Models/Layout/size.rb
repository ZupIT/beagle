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

class Size < BaseComponent

    def initialize
        unit_value = UnitValue.new
        variables = [
            Field.new(
                :name => "width",
                :type => unit_value,
                :isOptional => true,
                :comment => "The value specifies the view's width"
            ),
            Field.new(
                :name => "height",
                :type => unit_value,
                :isOptional => true,
                :comment => "The value specifies the view's height"
            ),
            Field.new(
                :name => "maxWidth",
                :type => unit_value,
                :isOptional => true,
                :comment => "The value specifies the view's max width"
            ),
            Field.new(
                :name => "maxHeight",
                :type => unit_value,
                :isOptional => true,
                :comment => "The value specifies the view's max height."
            ),
            Field.new(
                :name => "minWidth",
                :type => unit_value,
                :isOptional => true,
                :comment => "The value specifies the view's min width."
            ),
            Field.new(
                :name => "minHeight",
                :type => unit_value,
                :isOptional => true,
                :comment => "The value specifies the view's min height."
            ),
            Field.new(
                :name => "aspectRatio",
                :type => TypeDouble.new,
                :isOptional => true,
                :comment => "defined as the ratio between the width and the height of a node."
            )
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :comment => "Size handles the size of the item",
            :package => "br.com.zup.beagle.widget.core"
        )

        super(synthax_type)

    end

end