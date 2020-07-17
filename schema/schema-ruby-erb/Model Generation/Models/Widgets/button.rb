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

require_relative '../../Synthax/Variable'
require_relative '../base_component.rb'

class Button < BaseComponent

    def initialize
        buttonVariables = [
            Variable.new(:name => "text", :typeName => "String", :isBindable => true),
            Variable.new(:name => "styleId", :typeName => "String", :isOptional => true)
        ]
        synthaxType = SynthaxType.new(:kind => 'struct', :name => 'Button', :variables => buttonVariables)

        super(synthaxType)

    end
    
end