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

require_relative 'base_attributes.rb'

# Use this class when you attempt to generate a list
class Field < BaseAttributes

	# The acessor of the attribute. Default is public
    # @return [String] 
    attr_accessor :accessor

    # Boolean indicating if the variable is mutable. Default is false
    # @return [Bool] 
    attr_accessor :isMutable

    # Boolean indicating if the variable is bindable. Default is false
    # @return [Bool] 
    attr_accessor :isBindable

     # Boolean indicating if the variable is optional. Default is false
    # @return [Bool] 
    attr_accessor :isOptional

    # The default value of an attribute. It can be left in blank.
    # @return [String]
    attr_accessor :defaultValue

    #TODO refator to type

      # The type to be displayed on the generated code. Default is empty string
    # @return [String]
    attr_accessor :type

    def initialize(params = {})
        super
        @accessor = params.fetch(:accessor, 'public')
        @isBindable = params.fetch(:isBindable, false)
        @isOptional = params.fetch(:isOptional, false)
        @isMutable = params.fetch(:isMutable, false)
        @type = params.fetch(:type, nil)
        @defaultValue = params.fetch(:defaultValue, '')
    end

end