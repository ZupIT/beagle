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
require_relative '../server_driven_component.rb'
require_relative '../../Context/context_component.rb'

class NavigationBarItem < BaseComponent

    def initialize
        variables = [
            Variable.new(
                :name => "id",
                :typeName => TypesToString.string,
                :isOptional => true,
                :comment => "defines the Title on the navigation bar"
            ),
            Variable.new(
                :name => "image",
                :typeName => TypesToString.string,
                :isOptional => true,
                :comment => "defines an image for your navigation bar"
            ),
            Variable.new(
                :name => "text",
                :typeName => TypesToString.string,
                :comment => "defines an action to be called when the item is clicked on"
            ),
            Variable.new(
                :name => "action",
                :typeName => Action.new.name,
                :comment => "define the Title on the navigation bar"
            ),
            Variable.new(
                :name => "accessibility",
                :typeName => Accessibility.new.name,
                :isOptional => true,
                :comment => "define Accessibility details for the item"
            ),
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :comment => "Item that will be added to a navigation bar"
        )

        super(synthax_type)

    end

end