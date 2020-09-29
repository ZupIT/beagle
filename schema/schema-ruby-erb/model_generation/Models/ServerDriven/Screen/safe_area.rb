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

class SafeArea < BaseComponent

    def initialize
        variables = [
            Variable.new(
                :name => "top",
                :typeName => TypesToString.bool,
                :isOptional => true,
                :comment => "enable the safeArea constraint only on the TOP of the screen view."
            ),
            Variable.new(
                :name => "leading",
                :typeName => TypesToString.bool,
                :isOptional => true,
                :comment => "enable the safeArea constraint only on the LEFT side of the screen view."
            ),
            Variable.new(
                :name => "bottom",
                :typeName => TypesToString.bool,
                :isOptional => true,
                :comment => "enable the safeArea constraint only on the BOTTOM of the screen view."
            ),
            Variable.new(
                :name => "trailing",
                :typeName => TypesToString.bool,
                :isOptional => true,
                :comment => "enable the safeArea constraint only on the RIGHT of the screen view."
            ),
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :comment => "The SafeArea will enable Safe areas to help you place your views within the visible portion of the overall interface. Note: This class is only used to iOS SafeArea"
        )

        super(synthax_type)

    end

end