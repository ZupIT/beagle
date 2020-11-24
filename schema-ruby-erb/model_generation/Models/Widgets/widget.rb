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

require_relative '../../Synthax/Types/built_in_type.rb'
require_relative '../base_component.rb'

class Widget < BaseComponent

    #todo finish implementation, missing accessibility
    def initialize
        synthax_type = BuiltInType.new(
            :type => TypesToString.abstract,
            :name => self.name,
            :package => "br.com.zup.beagle.widget",
            :comment => "Base of all widgets",
            :inheritFrom => [
                StyleComponent.new,
                AccessibilityComponent.new,
                IdentifierComponent.new
            ]
        )

        super(synthax_type)

    end

end