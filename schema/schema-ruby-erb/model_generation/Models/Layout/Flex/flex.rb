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
require_relative 'flex_direction.rb'
require_relative 'flex_wrap.rb'
require_relative 'justify_content.rb'
require_relative 'align_items.rb'
require_relative 'align_self.rb'
require_relative 'align_content.rb'

class Flex < BaseComponent

    def initialize
        flex_direction = FlexDirection.new
        flex_wrap = FlexWrap.new
        justify_content = JustifyContent.new
        align_items = AlignItems.new
        align_self = AlignSelf.new
        align_content = AlignContent.new
        
        variables = [
            Variable.new(
                :name => "flexDirection",
                :typeName => flex_direction.name,
                :isOptional => true,
                :comment => "Controls the direction in which the children of a node are laid out.This is also referred to as the main axis.",
            ),
            Variable.new(
                :name => "flexWrap",
                :typeName => flex_wrap.name,
                :isOptional => true,
                :comment => "Set on containers and controls what happens when children overflow the size of the container along the main axis.",
            ),
            Variable.new(
                :name => "justifyContent",
                :typeName => justify_content.name,
                :isOptional => true,
                :comment => "Align children within the main axis of their container.",
            ),
            Variable.new(
                :name => "alignItems",
                :typeName => align_items.name,
                :isOptional => true,
                :comment => "Align items describes how to align children along the cross axis of their container.",
            ),
            Variable.new(
                :name => "alignSelf",
                :typeName => align_self.name,
                :isOptional => true,
                :comment => "This property allows to override the behavior of an item defined by the align-items property.",
            ),
            Variable.new(
                :name => "alignContent",
                :typeName => align_content.name,
                :isOptional => true,
                :comment => "Align content defines the distribution of lines along the cross-axis.",
            ),
            Variable.new(
                :name => "basis",
                :typeName => UnitValue.new.name,
                :isOptional => true,
                :comment => "Is an axis-independent way of providing the default size of an item along the main axis.",
            ),
            Variable.new(
                :name => "flex",
                :typeName => TypesToString.double,
                :isOptional => true,
                :comment => "Describes how any space within a container should be distributed among its children along the main axis.",
            ),
            Variable.new(
                :name => "grow",
                :typeName => TypesToString.double,
                :isOptional => true,
                :comment => "Define the proportion with which an item should grow if necessary.",
            ),
            Variable.new(
                :name => "shrink",
                :typeName => TypesToString.double,
                :isOptional => true,
                :comment => "Describes how to shrink children along the main axis in the case that the total size of the children overflow the size of the container on the main axis.",
            ),
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :sameFileTypes => [flex_direction, flex_wrap, justify_content, align_items, align_self, align_content],
            :comment => "Apply positioning using the flex box concept, with a yoga layout structure."
        )

        super(synthax_type)

    end

end