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

# This class is a core class of beagle schema. It should NOT be use directly. If you need to use
# these attributes, use one of its subclasses.
class BaseAttributes

    # The name of the attribute.
    # @return [String]
    attr_accessor :name

    # The type to be displayed on the generated code.
    # @return [String]
    attr_accessor :typeName
    
    # The acessor of the attribute. Default is public
    # @return [String] 
    attr_accessor :accessor

    # The default value of an attribute. It can be left in blank.
    # @return [String]
    attr_accessor :defaultValue
    
    # Comment to be generated to this attributed. It can be left in blank.
    # @return [String]
    attr_accessor :comment
    
    # Initialize for BaseAttributes
    def initialize(params = {})
        @name = params.fetch(:name, '')
        @typeName = params.fetch(:typeName, '')
        @accessor = params.fetch(:accessor, 'public')
        @defaultValue = params.fetch(:defaultValue, '')
        @comment = params.fetch(:comment, nil)
    end

end