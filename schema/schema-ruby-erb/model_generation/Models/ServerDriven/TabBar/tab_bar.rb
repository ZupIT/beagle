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

class TabBar < BaseComponent

    def initialize
        variables = [
            List.new(
                :name => "items",
                :typeName => TabBarItem.new.name,
                :comment => "define your tabs title and icon"
            ),
            Variable.new(
                :name => "styleId",
                :typeName => TypesToString.string,
                :isOptional => true,
                :comment => "reference a native style in your local styles file to be applied on this view."
            ),
            Variable.new(
                :name => "currentTab",
                :typeName => TypesToString.integer,
                :isOptional => true,
                :isBindable => true,
                :comment => "define the expression that is observer to change the current tab selected"
            ),
            List.new(
                :name => "onTabSelection",
                :typeName => Action.new.name,
                :isOptional => true,
                :comment => "define a list of action that will be executed when a tab is selected"
            ),
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :inheritFrom => [
                ServerDrivenComponent.new.name
            ],
            :comment => " TabBar is a component responsible to display a tab layout. It works by displaying tabs that can change a context when clicked."
        )

        super(synthax_type)

    end

end