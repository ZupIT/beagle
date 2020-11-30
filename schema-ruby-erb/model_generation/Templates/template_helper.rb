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

# This class lists the available supported languages of beagle schema.
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

# This class is needed because of the way we treat declarations in diferent languages. 
# If you want to handle diferent basic types in your language, this is the place to add them
#   E.g.: we use interface for kotlin but that word does not exist for swift so we are using protocol instead  
class BasicType < SupportedLanguages
    attr_accessor :grammar

    def initialize
        super
        @grammar = {
            TypeString.new.name => {@swift => "String", @kotlin => "String"},
            TypeBoolean.new.name => {@swift => "Bool", @kotlin => "Boolean"},
            TypeInterface.new.name => {@swift => "protocol", @kotlin => "interface"},
            TypeDouble.new.name => {@swift => "Double", @kotlin => "Double"},
            TypeEnum.new.name => {@swift => "enum", @kotlin => "enum class"},
            TypeInteger.new.name => {@swift => "Int", @kotlin => "Int"},
            TypeAbstract.new.name => {@swift => "protocol", @kotlin => "abstract class"}
        }

    end
    
end

# This class is a helper with common methods that might be useful on the creation of different 
# language templates. You don't necessarily have to use this, but it will be of a great help if you do.
class TemplateHelper
    
    # The identifier of of the language you want to generate. They are define in SupportedLanguages
    # @return [String]
    attr_accessor :languageIdentifier

    # The default declaration type for your models.
    #   E.g.: swift uses struct
    # @return [String]
    attr_accessor :defaultDeclarationType

    # Initialize method for TemplateHelper. You should definitely assign new values to defaultDeclarativeType and
    # languageIdentifier
    # @return [Bool] indicating wether the object is widget or not
    def initialize
        @defaultDeclarationType = ''
        @languageIdentifier = ''
        @types = BasicType.new
    end

    # Fetches a custom basic type if specified inside grammar in BasicType and if the languageIdentifer exists
    #
    # @param key [String] type identifier
    # @return [String] converted string to specified language or the key itself
    def fetch_type(key)
        if @types.grammar.key?(key) && @types.grammar[key].key?(@languageIdentifier)
            @types.grammar[key][@languageIdentifier]
        else
            key
        end
    end

    # Fetches a built in type if specified inside grammar in BasicType and if the languageIdentifer exists
    #
    # @param key [String] type identifier
    # @return [String] converted string to specified language or the key itself
    def fetch_built_in_type_declaration(key)
        (key == nil or key.name == nil) ? @defaultDeclarationType : fetch_type(key.name)
    end

    # Adds padding in each line of a given multiline string
    #
    # @param padding [String] string with empty spaces. Preferably a padding that is considered the default padding in your language
    # @param multiplier [Integer] multiplier to be applied to the padding parameter. Use 1 if you don't want a multiplier
    # @param text [String] text in which the padding will be applied
    # @return [String] the input text with padding
    def add_padding(padding, multiplier, text)
        output = ""
        text.each_line do |line|
            output += "#{padding * multiplier}#{line}"
        end

        output
    end

    # Given object_type, this functions returns if such an object is enum or not
    #
    # @param object_type [BaseComponent]
    # @return [Bool] indicating wether the object is enum or not
    def is_enum(object_type)
        object_type.synthax_type.class == EnumType
    end

    def variable_is_enum(variable)
        variable.class == EnumCase
    end

    def variable_is_primitive(variable)
        variable.type.is_a?(TypeString) or variable.type.is_a?(TypeInteger) or variable.type.is_a?(TypeBoolean) or variable.type.is_a?(TypeDouble)
    end

    def is_interface(object_type)
        object_type.synthax_type.type.is_a?(TypeInterface)
    end

    def is_abstract(object_type)
        object_type.synthax_type.type.is_a?(TypeAbstract)
    end

    def is_interface_or_enum(object_type)
         is_interface(object_type) or is_enum(object_type)
    end

    # Given object_type, this functions returns if such an object is widget or not
    #
    # @param object_type [BaseComponent]
    # @return [Bool] indicating wether the object is widget or not
    def is_widget(object_type)
        object_type.synthax_type.inheritFrom.any? { |component| component.synthax_type.name == "Widget" }
    end

    # Given object_type, this functions returns if such an object is a server driven component or not
    #
    # @param object_type [BaseComponent]
    # @return [Bool] indicating wether the object is a server driven component or not
    def is_server_driven_component(object_type)
        if object_type.synthax_type.name == "ServerDrivenComponent"
            return true
        else
            for inherit in object_type.synthax_type.inheritFrom
                return is_server_driven_component(inherit)
            end
        end

        return false
    end

    def has_variables(object_type)
        object_type.synthax_type.variables != nil and object_type.synthax_type.variables.size > 0
    end

    # Documentation

    def objectType_has_documentation(object_type)
        return object_type.synthax_type.comment != nil
    end 

    def variables_has_documentation(object_type) 
        if has_variables(object_type)
          return object_type.synthax_type.variables.any? { |variable| variable.comment != nil }
        end
        
        return false
    end

    def inheritFrom_has_documentation(object_type)
    result = false

    for inherit in object_type.synthax_type.inheritFrom
      if variables_has_documentation(inherit)
        return true
      end
    end

    result
    end

    def has_any_documentation(object_type)
        objectType_has_documentation(object_type) or variables_has_documentation(object_type) or has_inheritFrom_documentation(object_type)
    end 

end
