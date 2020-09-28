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

class TabBarItem < BaseComponent

    def initialize
        variables = [
            Variable.new(
                :name => "icon",
                :typeName => TypesToString.string,
                :isOptional => true,
                :comment => "display an icon image on the TabView component. If it is left as null or not declared it won't display any icon."
            ),
            Variable.new(
                :name => "title",
                :typeName => TypesToString.string,
                :isOptional => true,
                :comment => "displays the text on the TabView component. If it is null or not declared it won't display any text."
            ),
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :comment => "Define the view has in the tab view"
        )

        super(synthax_type)

    end

end