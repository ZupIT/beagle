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

# This module containts all the attributes of a type.
module BaseType
    # Name of the type
    # @return [String]
    attr_accessor :name

    # Variables that a type contains. Default is an empty array.
    # @see Variable
    # @return [Array<Variable>]
    attr_accessor :variables

    # Accessor for a type. Default is public.
    # @return [String]
    attr_accessor :accessor

    # Array containing the classes that a type inherits or implements. Default is an empty array.
    # @return [Array<String>]
    attr_accessor :inheritFrom

    # Package in wich a type belongs. Default is an empty string
    # @return [String]
    attr_accessor :package

    # Array of components that will be generated in the same file.
    #   E.g.: swift uses this property to generate some enums inside structs.
    # @see BaseComponent
    # @return [Array<BaseComponent>]
    attr_accessor :sameFileTypes

    # Type
    # setting this to something else will overwrite {TemplateHelper#defaultDeclarationType defaultDeclarationType} in the templates
    # @return [String]
    attr_accessor :type

    # Comment for built-in types. Default is nil
    # the intention of this comment is to be generated before the object declaration
    # @return [String]
    attr_accessor :comment

    def initialize(params = {})
        @type = nil 
        @name = params.fetch(:name, '')
        @variables = params.fetch(:variables, [])
        @accessor = params.fetch(:accessor, "public")
        @inheritFrom = params.fetch(:inheritFrom, [])
        @package = params.fetch(:package, "")
        @sameFileTypes = params.fetch(:sameFileTypes, [])
        @comment = params.fetch(:comment, nil)
    end
    
end