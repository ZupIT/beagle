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

    def initialize(components, helper)
        @helper = helper
        @tab = "    "
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

    def handle_field_accessor(variable)
        output = ""
        if variable.accessor != "public"
          output += "#{variable.accessor} "
        end
        output
    end

    def handle_field_mutator(variable)
        output = ""
        if variable.isMutable
            output += "var "
        else
            output += "val "
        end
        output
    end

    #Documentation

    def resolve_documentation(objectType)
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
        if @helper.is_interface_or_enum(objectType) or is_widget_android(objectType)
          output += "@property "
        else
          output += "@param " 
        end

        output += "#{variable.name}"

        if !@helper.is_enum(objectType)
          output += " #{replace_breakLine_documentation(variable.comment, "\n * #{generate_scape_documentation(variable, (@helper.is_interface(objectType) or @helper.is_abstract(objectType)))}")}\n"
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
            output += " #{replace_breakLine_documentation(variable.comment, "\n * #{generate_scape_documentation(variable, (@helper.is_interface(objectType) or @helper.is_abstract(objectType)))}")}\n"
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

    def is_backend()
      @helper.languageIdentifier == "ktBackend"
    end

    def handle_package(object)
      (is_backend()) ? object.synthax_type.package.backend : object.synthax_type.package.android
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

    def is_widget_android(objectType)
      @helper.inheritFrom_widget(objectType) and !is_backend()
    end

    def resolve_variables_imports(objectType)
    output = ""

    for variable in objectType.synthax_type.variables
      if !@helper.variable_is_primitive(variable)
        qualifiedName = "#{handle_package(variable.type)}.#{variable.type.synthax_type.name}"
        if !output.include? "#{qualifiedName}\n" and !qualifiedName.include? "#{handle_package(objectType)}"
          output += "import #{qualifiedName}\n"
        end
      end
    end
    
    for inherited in objectType.synthax_type.inheritFrom
      if !handle_package(inherited).include? "#{handle_package(objectType)}" and !(is_widget_android(objectType) and @helper.is_widget(inherited))
        output += "import #{handle_package(inherited)}.#{inherited.synthax_type.name}\n"
      end

      for variable in inherited.synthax_type.variables
        if !@helper.variable_is_primitive(variable) and !(is_widget_android(objectType) and @helper.is_interface(inherited))
            qualifiedName = "#{handle_package(variable.type)}.#{variable.type.synthax_type.name}"
          if !output.include? "#{qualifiedName}\n" and !qualifiedName.include? "#{handle_package(objectType)}"
            output += "import #{qualifiedName}"
            output += "\n"
          end
        end
      end 
    end

    output
    end

  def resolve_imports(objectType)
    output = resolve_variables_imports(objectType)
    output += resolve_bind_import(objectType)

    if output.include? "import"
      "\n" + output + "\n"
    else
      output + "\n"
    end
  end

  #TODO remove bind import rules
  def resolve_bind_import(objectType)
    output = ""

    bindPackage = (is_backend()) ? "br.com.zup.beagle.widget.context" : "br.com.zup.beagle.android.context"

    if objectType.synthax_type.variables.any? { |variable| variable.isBindable }
      if !bindPackage.include? "#{handle_package(objectType)}"
        output += "import " + bindPackage + ".Bind\n"
      end
    end

    # valueOf and valueOfNullable only in backend
    if is_backend()
      if objectType.synthax_type.variables.any? { |variable| variable.isBindable && !variable.isOptional } and objectType.synthax_type.variables.any? { |v| v.isOptional == false}
        if !bindPackage.include? "#{objectType.synthax_type.package.backend}"
          output += "import br.com.zup.beagle.widget.context.valueOf\n"
        end
      end

      if objectType.synthax_type.variables.any? { |variable| variable.isBindable && variable.isOptional } and objectType.synthax_type.variables.any? { |v| v.isOptional == false}
        if !bindPackage.include? "#{objectType.synthax_type.packag.backend}"
          output += "import br.com.zup.beagle.widget.context.valueOfNullable\n"
        end
      end
    end

    if output.include? "import"
      "\n" + output
    else
      output
    end
  end

  def init_enum(objectType)
    typeKind = @helper.fetch_built_in_type_declaration(objectType.synthax_type.type)

    output = "\n#{typeKind} #{objectType.synthax_type.name} {\n"

    counter = 0
    for field in objectType.synthax_type.variables
      if field.comment != nil
        output += "#{@tab}/**\n#{@tab} * #{replace_breakLine_documentation(field.comment, "\n#{@tab} * ")}\n#{@tab} */\n"
      end
      output += "#{@tab}#{field.name}"
      if counter != objectType.synthax_type.variables.size - 1
        output += ",\n\n"
      end
      counter += 1
    end

    output += "\n}"
    output
  end

  def single_variable_declaration(variable)
    type_name = @helper.fetch_type(variable.type.name)
    type_name = adapt_type_name_to_kotlin_specific(type_name)
    type_name
  end

  def init_interface(objectType)
    typeKind = @helper.fetch_built_in_type_declaration(objectType.synthax_type.type)
    
    output = "\n#{typeKind} #{objectType.synthax_type.name}"

    output += resolve_super_classes(objectType)

    counter = 0 
    if objectType.synthax_type.variables.any?
      output += " {"
      for variable in objectType.synthax_type.variables
        output += "\n#{@tab}#{handle_field_accessor(variable)}#{handle_field_mutator(variable)}#{variable.name}: #{handle_type_name(objectType, variable, @helper.is_abstract(objectType), objectType.synthax_type.variables.size - 1 != counter)}"
        counter += 1
      end
    end

    if output.include? "val" or output.include? "var"
      output += "\n}"
    end
    
    output
  end

  def init_abstract(objectType)
    typeKind = @helper.fetch_built_in_type_declaration(objectType.synthax_type.type)
    
    output = "\n#{typeKind} #{objectType.synthax_type.name}"
    output += resolve_super_classes(objectType)

    counter = 0 
    if objectType.synthax_type.variables.any?
      output += " {"
      for variable in objectType.synthax_type.variables
        output += "\n#{@tab}#{handle_field_accessor(variable)}#{handle_field_mutator(variable)}#{variable.name}: #{handle_type_name(objectType, variable, @helper.is_abstract(objectType), objectType.synthax_type.variables.size - 1 != counter)}"
        counter += 1
      end
    end

    for inherited in objectType.synthax_type.inheritFrom
      if @helper.is_interface(inherited)
        for variable in inherited.synthax_type.variables
          if !output.include? "{"
            output += "{"
          end
          output += "\n#{@tab}#{handle_field_accessor(variable)} override #{handle_field_mutator(variable)}#{variable.name}: #{handle_type_name(objectType, variable, @helper.is_abstract(objectType), inherited.synthax_type.variables.size - 1 != counter && !@helper.is_abstract(objectType))}"
          counter += 1
        end
      end
    end

    if output.include? "val" or output.include? "var"
      output += "\n}"
    end
    
    output
  end

    def resolve_super_classes(objectType)
    output = ""
    if objectType.synthax_type.inheritFrom.size > 0
      output += " : "
      counter = 0 
      for inherited in objectType.synthax_type.inheritFrom
        superClass = ""
        if @helper.is_interface(objectType) or is_widget_android(objectType)
          superClass += @helper.is_abstract(inherited) ? "" : inherited.name
        else
          superClass += @helper.is_abstract(inherited) ? "#{inherited.name}()" : inherited.name
        end
        
        if superClass != "" and objectType.synthax_type.inheritFrom.size - 1 != counter 
          superClass += ", "
        end
        output += superClass
        counter += 1
      end
      output += " "
    end

    output
  end

  def handle_type_name(objectType, variable, defaultValue, shouldAddComma = false)
    if variable.instance_of? Dictionary
      output = dictionary_variable_declaration(variable)
    else
      output = single_variable_declaration(variable)
    end

    output = variable.isBindable ? "Bind<#{output}>" : output
    output = variable.class == List ? "List<#{output}>" : output
    output = variable.isOptional ? output + "?" : output

    if defaultValue
      default = handle_default_variable(variable)
      output = !default.empty? && !@helper.is_interface(objectType) ? "#{output} = #{default}" : output
      output = shouldAddComma ? output + "," : output
    end
    
    output
  end

   def handle_default_variable(variable)
    variable.defaultValue.empty? && variable.isOptional ? "null" : variable.defaultValue
  end

  def handle_type_name_init_method(variable, shouldAddComma = false)
    default = handle_default_variable(variable)
    if variable.instance_of? Dictionary
      output = dictionary_variable_declaration(variable)
    else
      output = single_variable_declaration(variable)
    end
    output = variable.class == List ? "List<#{output}>" : output
    output = variable.isOptional ? output + "?" : output
    output = !default.empty? ? "#{output} = #{default}" : output
    output = shouldAddComma ?  output + "," : output
    output
  end

  def init_data_class(objectType, withContructor = true)
    typeKind = "data class"
    
    output = "\n#{typeKind} #{objectType.synthax_type.name}"

    counter = 0 
    if objectType.synthax_type.variables.any?
      output += " ("
      for variable in objectType.synthax_type.variables
        output += "\n#{@tab}#{handle_field_accessor(variable)}#{handle_field_mutator(variable)}#{variable.name}: #{handle_type_name(objectType, variable, true, objectType.synthax_type.variables.size - 1 != counter)}"
        counter += 1
      end
    end

    for inherited in objectType.synthax_type.inheritFrom
      if @helper.is_interface(inherited) && @helper.has_variables(inherited)
        if objectType.synthax_type.variables.size == 0
            output += " ("
        else
          output += ","
        end
        counter = 0 
        for variable in inherited.synthax_type.variables
          output += "\n#{@tab}#{handle_field_accessor(variable)}override #{handle_field_mutator(variable)}#{variable.name}: #{handle_type_name(objectType, variable, true, inherited.synthax_type.variables.size - 1 != counter)}"
          counter += 1
        end
      end
    end
    
    output += "\n)"
    output += resolve_super_classes(objectType)

    if withContructor
      if objectType.synthax_type.variables.any?(&:isBindable) and objectType.synthax_type.variables.any? { |v| v.isOptional == false}
        output += "{\n#{@tab}constructor ("
        counter = 0 
        for variable in objectType.synthax_type.variables
          output += "\n#{@tab}#{@tab}#{variable.name}: #{handle_type_name_init_method(variable, objectType.synthax_type.variables.size - 1 != counter)}"
          counter += 1
        end
        for inherited in objectType.synthax_type.inheritFrom
          if @helper.is_interface(inherited) && @helper.has_variables(inherited)
            if @helper.has_variables(objectType)
              output += ","
            else
              output += " ("
            end
            counter = 0 
            for variable in inherited.synthax_type.variables
            output += "\n#{@tab}#{@tab}#{variable.name}: #{handle_type_name_init_method(variable, inherited.synthax_type.variables.size - 1 != counter)}"
            counter += 1
            end
          end
        end
        output += "\n#{@tab}) : this ("

        counter = 0
        for variable in objectType.synthax_type.variables
          output += "\n#{@tab}#{@tab}#{handle_variable_with_bind(variable, objectType.synthax_type.variables.size - 1 != counter)}"
          counter += 1
        end

        for inherited in objectType.synthax_type.inheritFrom
          if @helper.is_interface(inherited) && @helper.has_variables(inherited)
            if @helper.has_variables(objectType)
                output += ","
            else
              output += " ("
            end
            counter = 0
            for variable in inherited.synthax_type.variables
            output += "\n#{@tab}#{@tab}#{handle_variable_with_bind(variable, inherited.synthax_type.variables.size - 1 != counter)}"
            counter += 1
            end
          end
        end
        output += "\n#{@tab})\n}"
      end
    end

    output
  end

  def handle_variable_with_bind(variable, shouldAddComma = false)
    bind_helper = variable.isOptional ? "valueOfNullable" : "valueOf"
    output = variable.isBindable ? "#{bind_helper}(#{variable.name})" : variable.name
    output = shouldAddComma ? output + "," : output
    output
  end

  def resolve_kotlin_object(objectType) 
    output = ""
    if @helper.is_enum(objectType)
      output += init_enum(objectType)
    else 
      if @helper.is_interface(objectType)
        output += init_interface(objectType)
      else
        if @helper.is_abstract(objectType)
          output += init_abstract(objectType)
        else
          output += init_data_class(objectType, is_backend())
        end
      end
    end
    output
  end

end