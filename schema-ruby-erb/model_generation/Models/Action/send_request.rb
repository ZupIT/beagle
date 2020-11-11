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

require_relative '../../Synthax/Attributes/field.rb'
require_relative '../../Synthax/Attributes/dictionary.rb'
require_relative '../base_component.rb'
require_relative '../../Synthax/Types/built_in_type.rb'

class SendRequest < BaseComponent

    def initialize
        action = Action.new
        method = HTTPMethod.new
        variables = [
            Field.new(
                :name => "url",
                :typeName => TypesToString.string,
                :isBindable => true
            ),
            Field.new(
                :name => "method",
                :typeName => method.name,
                :isOptional => true,
                :isBindable => true
            ),
            Field.new(
                :name => "data",
                :typeName => "ContextData",
                :isOptional => true
            ),
            Dictionary.new(
                :name => "headers",
                :type_of_key => TypesToString.string,
                :type_of_value => TypesToString.string,
                :isOptional => true,
                :isBindable => true
            ),
            List.new(
                :name => "onSuccess",
                :typeName => action.name,
                :isOptional => true
            ),
            List.new(
                :name => "onError",
                :typeName => action.name,
                :isOptional => true,
            ),
            List.new(
                :name => "onFinish",
                :typeName => action.name,
                :isOptional => true
            ),


        ]
        synthax_type = BuiltInType.new(
            :name => self.name,
            :variables => variables,
            :package => "br.com.zup.beagle.widget.core",
            :inheritFrom => [action.name],
            :sameFileTypes => [method]
        )

        super(synthax_type)

    end

end

class HTTPMethod < BaseComponent

    def initialize
        enum_cases = [
            EnumCase.new(:name => "get", :defaultValue => "GET"),
            EnumCase.new(:name => "post", :defaultValue => "POST"),
            EnumCase.new(:name => "put", :defaultValue => "PUT"),
            EnumCase.new(:name => "patch", :defaultValue => "PATCH"),
            EnumCase.new(:name => "delete", :defaultValue => "DELETE")
        ]
        synthax_type = EnumType.new(
            :name => self.name,
            :variables => enum_cases,
            :package => "br.com.zup.beagle.widget.core"
        )

        super(synthax_type)

    end

end