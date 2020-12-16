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

require_relative '../../../Synthax/Attributes/enum_case.rb'
require_relative '../../base_component.rb'
require_relative '../../../Synthax/Types/enum_type.rb'

class FlexWrap < BaseComponent

    def initialize
        enum_cases = [
            EnumCase.new(:name => "NO_WRAP", :comment => "The flex items are laid out in a single line which may cause the flex container to overflow.\nThe cross-start is either equivalent to start or before depending flex-direction value.\nThis is the default value."),
            EnumCase.new(:name => "WRAP", :comment => "The flex items break into multiple lines.\nThe cross-start is either equivalent to start or\nbefore depending flex-direction value and the cross-end is the opposite of the specified cross-start."),
            EnumCase.new(:name => "WRAP_REVERSE", :comment => "Behaves the same as wrap but cross-start and cross-end are permuted.")
        ]
        synthax_type = EnumType.new(
            :name => self.name,
            :variables => enum_cases,
            :package => Package.new(:backend => "br.com.zup.beagle.widget.core", :android => "br.com.zup.beagle.widget.core"),
            :comment => "FlexWrap is set on containers and it controls what happens when children overflow\nthe size of the container along the main axis.\nBy default, children are forced into a single line (which can shrink elements).\nIf wrapping is allowed, items are wrapped into multiple lines along the main axis if needed."
        )

        super(synthax_type)

    end

end