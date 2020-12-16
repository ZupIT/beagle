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

require_relative '../../../Synthax/Attributes/field.rb'
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
        size = Size.new
        edgeValue = EdgeValue.new
        
        variables = [
            Field.new(
                :name => "flexDirection",
                :type => flex_direction,
                :isOptional => true,
                :comment => "controls the direction in which the children of a node are laid out.\nThis is also referred to as the main axis",
            ),
            Field.new(
                :name => "flexWrap",
                :type => flex_wrap,
                :isOptional => true,
                :comment => "set on containers and controls what happens when children\noverflow the size of the container along the main axis.",
            ),
            Field.new(
                :name => "justifyContent",
                :type => justify_content,
                :isOptional => true,
                :comment => "align children within the main axis of their container.",
            ),
            Field.new(
                :name => "alignItems",
                :type => align_items,
                :isOptional => true,
                :comment => "Align items describes how to align children along the cross axis of their container.",
            ),
            Field.new(
                :name => "alignSelf",
                :type => align_self,
                :isOptional => true,
                :comment => "Align self has the same options and effect as align items\nbut instead of affecting the children within a container.",
            ),
            Field.new(
                :name => "alignContent",
                :type => align_content,
                :isOptional => true,
                :comment => "Align content defines the distribution of lines along the cross-axis..",
            ),
            Field.new(
                :name => "basis",
                :type => UnitValue.new,
                :isOptional => true,
                :comment => "is an axis-independent way of providing the default size of an item along the main axis.",
            ),
            Field.new(
                :name => "flex",
                :type => TypeDouble.new,
                :isOptional => true,
                :comment => "TODO.",
            ),
            Field.new(
                :name => "grow",
                :type => TypeDouble.new,
                :isOptional => true,
                :comment => "describes how any space within a container should be distributed among its children along the main axis.",
            ),
            Field.new(
                :name => "shrink",
                :type => TypeDouble.new,
                :isOptional => true,
                :comment => "describes how to shrink children along the main axis in the case that\nthe total size of the children overflow the size of the container on the main axis.",
            ),
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => Package.new(:backend => "br.com.zup.beagle.widget.core", :android => "br.com.zup.beagle.widget.core"),
            :sameFileTypes => [size, edgeValue, flex_direction, flex_wrap, justify_content, align_content, align_self, align_items],
            :comment => "The flex is a Layout component that will handle your visual component positioning at the screen.\nInternally Beagle uses a Layout engine called Yoga Layout to position elements on screen.\nIn fact it will use the HTML Flexbox properties applied on the visual components and its children."
        )

        super(synthax_type)

    end

end