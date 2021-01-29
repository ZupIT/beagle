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

require_relative '../../base_component.rb'
require_relative '../../../Synthax/Attributes/enum_case.rb'
require_relative '../../../Synthax/Types/enum_type.rb'
require_relative '../../../Synthax/Attributes/package.rb'

class AlignSelf < BaseComponent

    def initialize
        enum_cases = [
            EnumCase.new(:name => "FLEX_START", :comment => "Align wrapped lines to the start of the container's cross axis."),
            EnumCase.new(:name => "CENTER", :comment => "Align wrapped lines in the center of the container's cross axis."),
            EnumCase.new(:name => "FLEX_END", :comment => "Align wrapped lines to the end of the container's cross axis."),
            EnumCase.new(:name => "BASELINE", :comment => "Align children of a container along a common baseline.\nIndividual children can be set to be the reference baseline for their parents."),
            EnumCase.new(:name => "AUTO", :comment => "Computes to the parent's"),
            EnumCase.new(:name => "STRETCH", :comment => "Stretch wrapped lines to match the height of the container's cross axis.")
        ]
        synthax_type = EnumType.new(
            :name => self.name,
            :variables => enum_cases,
            :package => Package.new(:backend => "br.com.zup.beagle.widget.core", :android => "br.com.zup.beagle.widget.core"),
            :comment => "Describes how to align the children on the container's cross axis.\nAlign self replaces any parent-defined options with align items.\nFor example, you can use this property to center a child horizontally\ninside a container with flexDirection set to column or vertically inside a container with flexDirection set to row."
        )

        super(synthax_type)

    end

end