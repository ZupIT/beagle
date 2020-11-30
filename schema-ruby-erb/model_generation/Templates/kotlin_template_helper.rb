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

# This class lists the available supported kotlin template of beagle schema.
class KotlinTemplateHelper

    # @return [Hash]
    attr_accessor :import_manager

    def initialize(components)
        @helper = TemplateHelper.new
    end

    def dictionary_variable_declaration(variable) 
        type_of_key = adapt_type_name_to_kotlin_specific(variable.type_of_key)
        type_of_value = adapt_type_name_to_kotlin_specific(variable.type_of_value)
        type_name = "Map<#{type_of_key}, #{type_of_value}>"
        type_name
    end

    def adapt_type_name_to_kotlin_specific(typeName)
        if typeName.eql?("ContextData")
          "Any"
        else
          typeName
        end
    end

    def handleFieldAccessor(variable)
        output = ""
        if variable.accessor != "public"
          output += "#{variable.accessor} "
        end
        output
    end

    def handleFieldMutable(variable)
        output = ""
        if variable.isMutable
            output += "var "
        else
            output += "val "
        end
        output
    end

    #Documentation

    def resolveDocumentation(objectType)
    output = ""

    if @helper.has_any_documentation(objectType)
      output += "/**\n"
    end

    if @helper.objectType_has_documentation(objectType)
      output += " * #{replace_breakLine_documentation(objectType.synthax_type.comment, "\n * ")}\n"
    end

    if @helper.variables_has_documentation(objectType) or @helper.inheritFrom_has_documentation(objectType)
      output += " *\n"
    end

    for variable in objectType.synthax_type.variables
      if variable.comment != nil
        output += " * "
        if @helper.is_interface_or_enum(objectType)
          output += "@property "
        else
          output += "@param " 
        end

        output += "#{variable.name}"

        if !@helper.is_enum(objectType)
          output += " #{replace_breakLine_documentation(variable.comment, "\n * #{generate_scape_documentation(variable, @helper.is_interface(objectType))}")}\n"
        else
          output += "\n"
        end
      end
    end

    for inherited in objectType.synthax_type.inheritFrom
        for variable in inherited.synthax_type.variables
          if variable.comment != nil
            output += " * "
            if @helper.is_interface_or_enum(inherited)
              output += "@property "
            else
              output += "@param " 
            end

            output += "#{variable.name}"
            if !@helper.is_enum(inherited)
              output += " #{replace_breakLine_documentation(variable.comment, "\n * ")}\n"
            else
              output += "\n"
            end
          end
        end
    end

    if @helper.has_any_documentation(objectType)
      output += " *\n */"
    end

    output
  end

    def generate_scape_documentation(variable, is_property)
      output = ""
      scape = ""
      if is_property
        scape = "@property "
      else
        scape = "@param "
      end

      for i in 0.."#{scape}#{variable.name}".length
        output += " "
      end
      output
    end

    def replace_breakLine_documentation(comment, replace)
        comment.gsub("\n", replace)
    end
end