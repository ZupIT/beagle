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

class NavigationBar < BaseComponent

    def initialize
        variables = [
            Variable.new(
                :name => "title",
                :typeName => TypesToString.string,
                :comment => "define the Title on the navigation bar"
            ),
            Variable.new(
                :name => "styleId",
                :typeName => TypesToString.string,
                :isOptional => true,
                :comment => "could define a custom layout for your action bar/navigation  bar"
            ),
            Variable.new(
                :name => "showBackButton",
                :typeName => TypesToString.bool,
                :isOptional => true,
                :comment => "enable a back button into your action bar/navigation bar"
            ),
            Variable.new(
                :name => "backButtonAccessibility",
                :typeName => Accessibility.new.name,
                :isOptional => true,
                :comment => "define accessibility details for the item"
            ),
            List.new(
                :name => "navigationBarItems",
                :typeName => NavigationBarItem.new.name,
                :isOptional => true,
                :comment => "defines a List of navigation bar items."
            ),
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :comment => "Typically displayed at the top of the window, containing buttons for navigating within a hierarchy of screens"
        )

        super(synthax_type)

    end

end