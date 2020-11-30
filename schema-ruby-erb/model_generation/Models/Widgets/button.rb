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
require_relative '../../Synthax/Attributes/list.rb'
require_relative '../../Synthax/Types/built_in_type.rb'
require_relative '../base_component.rb'
require_relative '../Action/action.rb'
require_relative 'widget.rb'

class Button < BaseComponent

    def initialize
        variables = [
            Field.new(:name => "text", :type => TypeString.new, :isBindable => true, :comment => "define the button text content."),
            Field.new(:name => "styleId", :type => TypeString.new, :isOptional => true, :comment => "reference a native style in your local styles file to be applied on this button."),
            List.new(:name => "onPress", :type => Action.new, :isOptional => true, :comment => "attribute to define action when onPress")
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.ui",
            :comment => "Define a button natively using the server driven information received through Beagle",
            :inheritFrom => [
               Widget.new,
               TouchableAnalytics.new
            ]
        )

        super(synthax_type)

    end
    
end