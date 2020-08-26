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
require_relative '../../Synthax/Attributes/variable.rb'
require_relative '../../Synthax/Attributes/list.rb'
require_relative '../base_component.rb'
require_relative 'widget.rb'
require_relative 'text_input_type.rb'

class TextInput < BaseComponent

    def initialize
        variables = [
            Variable.new(:name => "value", :typeName => BasicTypeKeys.string, :isBindable => true, :isOptional => true),
            Variable.new(:name => "placeholder", :typeName => BasicTypeKeys.string, :isBindable => true, :isOptional => true),
            Variable.new(:name => "disabled", :typeName => BasicTypeKeys.bool, :isBindable => true, :isOptional => true),
            Variable.new(:name => "readOnly", :typeName => BasicTypeKeys.bool, :isBindable => true, :isOptional => true),
            Variable.new(:name => "type", :typeName => TextInputType.new.name, :isBindable => true, :isOptional => true),
            Variable.new(:name => "hidden", :typeName => BasicTypeKeys.bool, :isBindable => true, :isOptional => true),
            Variable.new(:name => "styleId", :typeName => BasicTypeKeys.string, :isOptional => true),
            List.new(:name => "onChange", :typeName => "Action", :isOptional => true),
            List.new(:name => "onBlur", :typeName => "Action", :isOptional => true),
            List.new(:name => "onFocus", :typeName => "Action", :isOptional => true)
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.ui",
            :inheritFrom => [
               Widget.new.name
            ]
        )

        super(synthax_type)

    end

end