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

require_relative '../../Synthax/Attributes/variable.rb'
require_relative '../base_component.rb'
require_relative '../../Synthax/Types/built_in_type.rb'

class AddChildren < BaseComponent

    def initialize
        action = Action.new
        variables = [
            Variable.new(:name => "componentId", :typeName => TypesToString.string),
            List.new(:name => "value", :typeName => ServerDrivenComponent.new.name),
            Variable.new(:name => "mode", :typeName => Mode.new.name),
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :inheritFrom => [action.name],
            :comment => "Action that insert children components in a node hierarchy",
            :sameFileTypes => [Mode.new]
        )

        super(synthax_type)

    end

end

class Mode < BaseComponent

    def initialize
        enum_cases = [
            EnumCase.new(:name => "append"),
            EnumCase.new(:name => "prepend"),
            EnumCase.new(:name => "replace")
        ]
        synthax_type = EnumType.new(
            :name => self.name,
            :variables => enum_cases,
            :inheritFrom => [TypesToString.string],
            :package => "br.com.zup.beagle.widget.core"
        )

        super(synthax_type)

    end

end