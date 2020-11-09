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

# This module exists to create ids for types.
# This is important because some reserved words are written in a different way among languages
# despite meaning the same thing. If you add a type here, you will also have to add in the grammar located in TemplateHelper.
module TypesToString
    
    # String id for String
    # @return [String]
    @@string = "String"
    def self.string
        @@string
    end

    # Integer id for Int
    # @return [String]
    @@integer = "Int"
    def self.integer
        @@integer
    end

    # Boolean id for Bool
    # @return [String]
    @@bool = "Bool"
    def self.bool
        @@bool
    end

    # Double id for Double
    # @return [String]
    @@double = "Double"
    def self.double
        @@double
    end

    # Interface id for Interface
    # @return [String]
    @@interface = "interface"
    def self.interface
        @@interface
    end

    # Enum id for Enum
    # @return [String]
    @@enum = "Enum"
    def self.enum
        @@enum
    end

    # Abstract id for abstract
    # @return [String]
    @@abstract = "abstract"
    def self.abstract
        @@abstract
    end

end