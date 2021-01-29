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

require_relative '../base_component.rb'
require_relative '../../Synthax/Attributes/field.rb'
require_relative '../../Synthax/Types/built_in_type.rb'
require_relative '../../Models/Types/double.rb'
require_relative '../../Synthax/Attributes/package.rb'

class CornerRadius < BaseComponent

    def initialize
        variables = [
            Field.new(:name => "radius", :type => TypeDouble.new, :defaultValue => "0.0", :comment => "define size of radius")
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :comment => "The corner radius change the appearance of view",
            :package => Package.new(:backend => "br.com.zup.beagle.core", :android => "br.com.zup.beagle.core")
        )

        super(synthax_type)

    end

end