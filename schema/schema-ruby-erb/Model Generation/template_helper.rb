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

require_relative './Synthax/Types/basic_type'

class SupportedLanguages

    attr_accessor :swift, :kotlin, :kotlinBackend, :typeScript

    def initialize
        @swift = "swift"
        @kotlin = "kt"
        @kotlinBackend = "ktBackend"
        @typeScript = "ts"
        
        @dart = "dart" #todo
    end

end

class BasicType < SupportedLanguages

    attr_accessor :grammar
 
    def initialize
        super
        keys = BasicTypeKeys.new
        @grammar = {
            keys.string => {@swift => "String", @kotlin => "String"},
            keys.bool => {@swift => "Bool", @kotlin => "Bool"},
            keys.interface => {@swift => "protocol", @kotlin => "interface"},
            keys.enum => {@swift => "enum", @kotlin => "enum"}
        }
        
        #puts @grammar["String"]["swift"]

    end
    
end

class TemplateHelper

    attr_accessor :languageIdentifier

    def initialize
        @languageIdentifier = ''
        @types = BasicType.new
    end

    def fetchType(key)
        if @types.grammar.key?(key) && @types.grammar[key].key?(@languageIdentifier)
            @types.grammar[key][@languageIdentifier]
        else
            key
        end
    end

    def addPadding(padding, multiplier, text)
        output = ""
        text.each_line do |line|
            output += "#{padding * multiplier}#{line}"
        end

        output
    end

    def isEnum(objectType)
        objectType.synthaxType.class == EnumType
    end

end