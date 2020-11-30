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

class IdentifierComponent < BaseComponent

    def initialize
    	variables = [
            Field.new(:name => "id", :typeName => TypeString.new, :isOptional => true, :isMutable => true, :comment => "use to identifier the current view")
        ]
        synthax_type = BuiltInType.new(
            :type => TypeInterface.new,
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.core",
            :comment => "Component that hold the identifier",
            :inheritFrom => [
               ServerDrivenComponent.new
            ]
        )

        super(synthax_type)

    end

end