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

class Screen < BaseComponent

    def initialize
        variables = [
            Variable.new(
                :name => "id",
                :typeName => TypesToString.string,
                :isOptional => true,
                :comment => "identifies your screen globally inside your application so that it could have actions set on itself."
            ),
            Variable.new(
                :name => "style",
                :typeName => Style.new.name,
                :isOptional => true,
                :comment => "enable a few visual options to be changed."
            ),
            Variable.new(
                :name => "safeArea",
                :typeName => SafeArea.new.name,
                :isOptional => true,
                :comment => "enable Safe areas to help you place your views within the visible portion of the overall interface. By default it is not enabled and it wont constrain considering any safe area."
            ),
            Variable.new(
                :name => "navigationBar",
                :typeName => NavigationBar.new.name,
                :isOptional => true,
                :comment => "enable a action bar/navigation bar into your view. By default it is set as null."
            ),
            Variable.new(
                :name => "screenAnalyticsEvent",
                :typeName => "AnalyticsScreen", #todo unify analytics names between platforms
                :isOptional => true,
                :comment => "send event when screen appear/disappear"
            ),
            Variable.new(
                :name => "child",
                :typeName => ServerDrivenComponent.new.name,
                :comment => "define the child elements on this screen. It could be any visual component that extends ServerDrivenComponent"
            ),
            Variable.new(
                :name => "context",
                :typeName => "Context",
                :isOptional => true
            )
        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :inheritFrom => [
                ContextComponent.new.name
            ],
            :comment => "The screen element will help you define the screen view structure. By using this component you can define configurations like whether or not you want to use safe areas or display a tool bar/navigation bar."
        )

        super(synthax_type)

    end

end