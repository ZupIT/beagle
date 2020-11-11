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

require_relative '../../../Synthax/Attributes/field.rb'
require_relative '../../base_component.rb'
require_relative '../server_driven_component.rb'

class ListView < BaseComponent

    def initialize
        server_driven = ServerDrivenComponent.new
        variables = [
            List.new(
                :name => "children",
                :typeName => server_driven.name,
                :comment => "define the items on the list view."
            ),
            Field.new(
                :name => "direction",
                :typeName => Direction.new.name,
                :comment => "define the list direction."
            )
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.ui",
            :comment => "ListView is a Layout component that will define a list of views natively. These views could be any Server Driven Component.",
            :inheritFrom => [
               server_driven.name
            ],
            :sameFileTypes => [Direction.new]
        )

        super(synthax_type)

    end
    
end

class Direction < BaseComponent

    def initialize
        server_driven = ServerDrivenComponent.new
        variables = [
            EnumCase.new(
                :name => "vertical",
                :defaultValue => "VERTICAL",
            ),
            EnumCase.new(
                :name => "horizontal",
                :defaultValue => "HORIZONTAL"
            )
        ]
        synthax_type = EnumType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.ui",
            :inheritFrom => [
               TypesToString.string
            ]
        )

        super(synthax_type)

    end
    
end