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

class JustifyContent < BaseComponent

    def initialize
        enum_cases = [
            EnumCase.new(:name => "FLEX_START", :comment => "Align children of a container to the start of the container's main axis."),
            EnumCase.new(:name => "CENTER", :comment => "Align children of a container in the center of the container's main axis."),
            EnumCase.new(:name => "FLEX_END", :comment => "Align children of a container to the end of the container's main axis."),
            EnumCase.new(:name => "SPACE_BETWEEN", :comment => "Evenly space off children across the container's main axis,\ndistributing the remaining space between the children."),
            EnumCase.new(:name => "SPACE_AROUND", :comment => "Evenly space off children across the container's main axis,\ndistributing the remaining space around the children.\nCompared to space-between, using space-around will result in space\nbeing distributed to the beginning of the first child and end of the last child."),
            EnumCase.new(:name => "SPACE_EVENLY", :comment => "evenly distribute children within the alignment container along the main axis.\nThe spacing between each pair of adjacent items,\nthe main-start edge and the first item, and the main-end edge and the last item, are all exactly the same.")
        ]
        synthax_type = EnumType.new(
            :name => self.name,
            :variables => enum_cases,
            :package => "br.com.zup.beagle.widget.core",
            :comment => "Describes how to align children within the main axis of their container.\nFor example, you can use this property to center a child horizontally within a container with flexDirection\nset to row or vertically within a container with flexDirection set to column."
        )
    
        super(synthax_type)

    end

end