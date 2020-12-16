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

class AlignContent < BaseComponent

    def initialize
        enum_cases = [
            EnumCase.new(:name => "FLEX_START", :comment => "Align wrapped lines to the start of the container's cross axis."),
            EnumCase.new(:name => "CENTER", :comment => "Align wrapped lines in the center of the container's cross axis."),
            EnumCase.new(:name => "FLEX_END", :comment => "Align wrapped lines to the end of the container's cross axis."),
            EnumCase.new(:name => "SPACE_BETWEEN", :comment => "Evenly space wrapped lines across the container's main axis,\ndistributing the remaining space between the lines."),
            EnumCase.new(:name => "SPACE_AROUND", :comment => "Evenly space wrapped lines across the container's main axis, distributing the remaining space around the lines.\nCompared to space-between, using space-around will result in space being\ndistributed to the beginning of the first line and the end of the last line."),
            EnumCase.new(:name => "STRETCH", :comment => "Stretch wrapped lines to match the height of the container's cross axis.")
        ]
        synthax_type = EnumType.new(
            :name => self.name,
            :variables => enum_cases,
            :package => Package.new(:backend => "br.com.zup.beagle.widget.core", :android => "br.com.zup.beagle.widget.core"),
            :comment => "Describes how to align distribution of lines along the transverse axis of the container.\nFor example, you can use this property to center child lines horizontally\ninside a container with flexDirection defined as a column or vertically inside a container\nwith flexDirection defined as a row."
        )

        super(synthax_type)

    end

end