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
class KotlinTemplateHelper < TemplateHelper

  TAB = "    "
  
  def initialize
    super
  end

  # Given object_type, this functions returns if such an object is interface or not
    #
    # @param object_type [BaseComponent]
    # @return [Bool] indicating wether the object is interface or not
  def is_interface(object_type)
    super(object_type) or is_widget_android(object_type)
  end

  # Given object_type, this functions returns if it is android widget
  #
  # @param object_type [BaseComponent]
  # @return [Bool] it is android widget or not
  def is_widget_android(object_type)
      inheritFrom_widget(object_type) and !is_backend()
  end

  # Given object_type, this functions returns the imports
  #
  # @param object_type [BaseComponent]
  # @return [String] imports for kotlin template
  def resolve_imports(object_type)
    output = ""

    output += resolve_variables_imports(object_type)
    output += resolve_bind_import(object_type)

    if output.include? "import"
      "\n" + output + "\n"
    else
      output + "\n"
    end
  end

  # Handle import in kotlin
  #
  # @param object_type [BaseComponent]
  # @return [String] import for kotlin
  def handle_import(object_type)
    "import #{handle_package(object_type)}.#{object_type.synthax_type.name}\n"
  end

  # Given object_type, this functions returns imports of variables
  #
  # @param object_type [BaseComponent]
  # @return [String] import of variables
  def resolve_variables_imports(object_type)
    output = ""

    #Local fields
    for variable in object_type.synthax_type.variables
      if !variable_is_primitive(variable)
        qualified_name = handle_import(variable.type)
        if !output.include? "#{qualified_name}" and !already_contains_import(qualified_name, object_type)
          output += qualified_name
        end
      end
    end
    
     #Inherited fields
    for inherited in object_type.synthax_type.inheritFrom
      if !handle_package(inherited).include? "#{handle_package(object_type)}" and !(is_widget_android(object_type) and is_widget(inherited))
        output += handle_import(inherited)
      end

      for variable in inherited.synthax_type.variables
        if !variable_is_primitive(variable) and !is_widget_android(object_type)
            qualified_name = handle_import(variable.type)
          if !output.include? "#{qualified_name}" and !already_contains_import(qualified_name, object_type)
            output += qualified_name
          end
        end
      end 
    end
    
    output
  end

  # Given object_type, this functions returns imports of Bind
  #
  # @param object_type [BaseComponent]
  # @return [String] bind import for kotlin Backend or Android template
  def resolve_bind_import(object_type)
    output = ""

    bind_package = (is_backend()) ? "br.com.zup.beagle.widget.context" : "br.com.zup.beagle.android.context"

    if object_type.synthax_type.variables.any? { |variable| variable.isBindable }
      if !bind_package.include? "#{handle_package(object_type)}"
        output += "import " + bind_package + ".Bind\n"
      end
    end

    # valueOf and valueOfNullable only in backend
    if is_backend()
      if object_type.synthax_type.variables.any? { |variable| variable.isBindable && !variable.isOptional } and object_type.synthax_type.variables.any? { |v| v.isOptional == false}
        if !already_contains_import(bind_package, object_type)
          output += "import br.com.zup.beagle.widget.context.valueOf\n"
        end
      end

      if object_type.synthax_type.variables.any? { |variable| variable.isBindable && variable.isOptional } and object_type.synthax_type.variables.any? { |v| v.isOptional == false}
        if !already_contains_import(bind_package, object_type)
          output += "import br.com.zup.beagle.widget.context.valueOfNullable\n"
        end
      end
    end

    output
  end

  # Given object_type, this functions returns documentation of variables
  #
  # @param object_type [BaseComponent]
  # @return [String] documentation of variables for template
  def resolve_variables_documentation(object_type)
    output = ""

    for variable in object_type.synthax_type.variables
      if variable.comment != nil
        output += " * #{handle_field_type_documentation(object_type)} #{variable.name}"

        if !is_enum(object_type)
          output += " #{replace_breakLine_documentation(variable.comment, "\n * #{generate_scape_documentation(variable, is_interface_or_abstract(object_type))}")}\n"
        else
          output += "\n"
        end
      end
    end

    output
  end

  # Given object_type, this functions returns documentation in kotlin
  #
  # @param object_type [BaseComponent]
  # @return [String] documentation for template
  def resolve_documentation(object_type)
    output = ""

    if has_any_documentation(object_type)
      output += "/**\n"
    end

    if object_has_documentation(object_type)
      output += " * #{replace_breakLine_documentation(object_type.synthax_type.comment, "\n * ")}\n"
    end

    if variables_has_documentation(object_type) or inheritFrom_has_documentation(object_type)
      output += " *\n"
    end

    output += resolve_variables_documentation(object_type)

    for inherited in object_type.synthax_type.inheritFrom
      output += resolve_variables_documentation(inherited)
    end

    if has_any_documentation(object_type)
      output += " *\n */"
    end

    output
  end

  # Handle field type for documentation
  #
  # @param object_type [BaseComponent]
  # @return [String] field type for documentation
  def handle_field_type_documentation(object_type)
    output = ""

    if is_interface_or_enum(object_type)
      output += "@property"
    else
      output += "@param" 
    end

    output
  end

  # Handle name and type of class
  #
  # @param object_type [BaseComponent]
  # @return [String] name and type of class
  def handle_name_and_type(object_type)
    "\n#{fetch_built_in_type_declaration(object_type.synthax_type.type)} #{object_type.synthax_type.name}"
  end

  # Given object_type, this functions returns a generated kotlin object
  #
  # @param object_type [BaseComponent]
  # @return [String] generated kotlin object
  def resolve_kotlin_object(object_type) 
    output = ""

    if is_enum(object_type)
      output += init_enum(object_type)
    else 
      if is_interface(object_type)
        output += init_interface(object_type)
      else
        if is_abstract(object_type)
          output += init_abstract(object_type)
        else
          output += init_data_class(object_type, is_backend())
        end
      end
    end

    output
  end

  # Given object_type, this functions returns a generated interface
  #
  # @param object_type [BaseComponent]
  # @return [String] generated interface
  def init_interface(object_type)
    output = handle_name_and_type(object_type)
    output += resolve_super_classes(object_type)

    counter = 0 
    if object_type.synthax_type.variables.any?
      output += " {"
      for variable in object_type.synthax_type.variables
        output += "\n#{TAB}#{handle_field_accessor(variable)}#{handle_field_mutator(variable)}#{variable.name}: #{handle_type_name(object_type, variable, is_abstract(object_type), object_type.synthax_type.variables.size - 1 != counter)}"
        counter += 1
      end
    end

    if contains_declared_fields(output)
      output += "\n}"
    end
    
    output
  end

  # Given output, this functions returns if such an output has declared fields
  #
  # @param output [String]
  # @return [Bool] has declared fields
  def contains_declared_fields(output)
    output.include? "val" or output.include? "var"
  end

  # Given object_type, this functions returns its super classes
  #
  # @param object_type [BaseComponent]
  # @return [String] its super classes
  def resolve_super_classes(object_type)
    output = ""
    
    if object_type.synthax_type.inheritFrom.size > 0
      output += " : "
      
      counter = 0 
      for inherited in object_type.synthax_type.inheritFrom
        super_class = ""

        if is_interface(object_type)
          super_class += is_abstract(inherited) ? "" : inherited.name
        else
          super_class += is_abstract(inherited) ? "#{inherited.name}()" : inherited.name
        end
        
        if super_class != "" and object_type.synthax_type.inheritFrom.size - 1 != counter 
          super_class += ", "
        end

        output += super_class
        counter += 1
      end

      output += " "
    end

    output
  end

  # Given object_type, this functions returns a generated enum
  #
  # @param object_type [BaseComponent]
  # @return [String] generated enum
  def init_enum(object_type)
    output = "#{handle_name_and_type(object_type)} {\n"

    counter = 0
    for field in object_type.synthax_type.variables
      if field.comment != nil
        output += "#{TAB}/**\n#{TAB} * #{replace_breakLine_documentation(field.comment, "\n#{TAB} * ")}\n#{TAB} */\n"
      end

      output += "#{TAB}#{field.name}"

      if counter != object_type.synthax_type.variables.size - 1
        output += ",\n\n"
      end

      counter += 1
    end

    output += "\n}"
    output
  end

  # Given object_type, this functions returns a generated abstract
  #
  # @param object_type [BaseComponent]
  # @return [String] generated abstract
  def init_abstract(object_type)
    output = handle_name_and_type(object_type)
    output += resolve_super_classes(object_type)

    counter = 0 
    if object_type.synthax_type.variables.any?
      output += " {"
      
      for variable in object_type.synthax_type.variables
        output += "\n#{TAB}#{handle_field_accessor(variable)}#{handle_field_mutator(variable)}#{variable.name}: #{handle_type_name(object_type, variable, is_abstract(object_type), object_type.synthax_type.variables.size - 1 != counter)}"
        counter += 1
      end
    end

    counter = 0
    for inherited in object_type.synthax_type.inheritFrom
      if is_interface(inherited)
        for variable in inherited.synthax_type.variables
          if !output.include? "{"
            output += "{"
          end

          output += "\n#{TAB}#{handle_field_accessor(variable)} override #{handle_field_mutator(variable)}#{variable.name}: #{handle_type_name(object_type, variable, is_abstract(object_type), inherited.synthax_type.variables.size - 1 != counter && !is_abstract(object_type))}"
          counter += 1
        end
      end
    end

    if contains_declared_fields(output)
      output += "\n}"
    end
    
    output
  end

  # Given object_type, this functions returns a generated data class
  #
  # @param object_type [BaseComponent]
  # @return [String]  generated data class
  def init_data_class(object_type, withContructor = true)
    object_type.synthax_type.type = TypeDataClass.new
    output = handle_name_and_type(object_type)

    counter = 0 
    if object_type.synthax_type.variables.any?
      output += " ("
      for variable in object_type.synthax_type.variables
        output += "\n#{TAB}#{handle_field_accessor(variable)}#{handle_field_mutator(variable)}#{variable.name}: #{handle_type_name(object_type, variable, true, object_type.synthax_type.variables.size - 1 != counter)}"
        counter += 1
      end
    end

    for inherited in object_type.synthax_type.inheritFrom
      if is_interface(inherited) && has_variables(inherited)
        if object_type.synthax_type.variables.size == 0
            output += " ("
        else
          output += ","
        end

        counter = 0 
        for variable in inherited.synthax_type.variables
          output += "\n#{TAB}#{handle_field_accessor(variable)}override #{handle_field_mutator(variable)}#{variable.name}: #{handle_type_name(object_type, variable, true, inherited.synthax_type.variables.size - 1 != counter)}"
          counter += 1
        end
      end
    end
    
    output += "\n)"
    output += resolve_super_classes(object_type)

    if withContructor
      output += init_contructor(object_type)
    end

    output
  end

  # Given object_type, this functions returns a generated contructor for data class
  #
  # @param object_type [BaseComponent]
  # @return [String]  generated contructor for data class
  def init_contructor(object_type)
    output  = ""

    if object_type.synthax_type.variables.any?(&:isBindable) and object_type.synthax_type.variables.any? { |v| v.isOptional == false}
      output += "{\n#{TAB}constructor ("

      counter = 0 
      for variable in object_type.synthax_type.variables
        output += "\n#{TAB}#{TAB}#{variable.name}: #{handle_type_name_init_method(variable, object_type.synthax_type.variables.size - 1 != counter)}"
        counter += 1
      end

      for inherited in object_type.synthax_type.inheritFrom
        if is_interface(inherited) && has_variables(inherited)
          if has_variables(object_type)
            output += ","
          else
            output += " ("
          end

          counter = 0 
          for variable in inherited.synthax_type.variables
          output += "\n#{TAB}#{TAB}#{variable.name}: #{handle_type_name_init_method(variable, inherited.synthax_type.variables.size - 1 != counter)}"
          counter += 1
          end
        end
      end

      output += "\n#{TAB}) : this ("

      counter = 0
      for variable in object_type.synthax_type.variables
        output += "\n#{TAB}#{TAB}#{handle_variable_with_bind(variable, object_type.synthax_type.variables.size - 1 != counter)}"
        counter += 1
      end

      for inherited in object_type.synthax_type.inheritFrom
        if is_interface(inherited) && has_variables(inherited)
          if has_variables(object_type)
              output += ","
          else
            output += " ("
          end

          counter = 0
          for variable in inherited.synthax_type.variables
          output += "\n#{TAB}#{TAB}#{handle_variable_with_bind(variable, inherited.synthax_type.variables.size - 1 != counter)}"
          counter += 1
          end
        end
      end

      output += "\n#{TAB})\n}"
    end

    output
  end

  # Given object_type, this functions returns if it is generating for the backend
  #
  # @return [Bool] generating for the backend or not
  def is_backend()
      languageIdentifier == @kotlinBackend
  end

  # Given object_type, this functions returns the package
  #
  # @param object_type [BaseComponent]
  # @return [String] package for kotlin template
  def resolve_package(object_type)
    "package #{handle_package(object_type)}\n"
  end

  # Handle package in kotlin
  #
  # @param object_type [BaseComponent]
  # @return [String] package for kotlin Backend or Android template
  def handle_package(object_type)
    (is_backend()) ? object_type.synthax_type.package.backend : object_type.synthax_type.package.android
  end

  # Replace breakLine for kotlin documentation
  #
  # @param comment [String]
  # @param replace [String]
  # @return [String] new comment for kotlin documentation
  def replace_breakLine_documentation(comment, replace)
      comment.gsub("\n", replace)
  end

  # Given output and object_type, this functions returns if output already contains package of the objectType
  #
  # @param output [String]
  # @param object_type [BaseComponent]
  # @return [Bool] output already contains package
  def already_contains_import(output, object_type)
    output.include? "#{handle_package(object_type)}"
  end

  # Handle field accessor
  #
  # @param field [Field]
  # @return [String] field accessor
  def handle_field_accessor(field)
    output = ""

    if field.accessor != "public"
      output += "#{field.accessor} "
    end

    output
  end

  # Handle field mutator
  #
  # @param field [Field]
  # @return [String] field mutator
  def handle_field_mutator(field)
    output = ""

    if field.isMutable
        output += "var "
    else
        output += "val "
    end

    output
  end

  # Given variable, this functions returns a scape in kotlin documentation
  #
  # @param variable [Field]
  # @param is_property [Bool]
  # @return [Bool] a scape in kotlin documentation
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

   def dictionary_variable_declaration(variable) 
    type_of_key = handle_type(variable.type_of_key)
    type_of_value = handle_type(variable.type_of_value)
    type_name = "Map<#{type_of_key}, #{type_of_value}>"
    type_name
  end

  # Handle type to kotlin
  #
  # @param type [String]
  # @return [String] type to kotlin
  def handle_type(type)
    if type.eql?("ContextData")
      "Any"
    else
      type
    end
  end

  # Handle single field
  #
  # @param field [String]
  # @return [String] single field
  def single_field_declaration(field)
    type_name = fetch_type(field.type.name)
    type_name = handle_type(type_name)
    type_name
  end

  # Handle type and name
  #
  # @param object_type [String]
  # @param variable [Field]
  # @param default_value [String]
  # @param should_add_comma [Bool]
  # @return [String] type and name
  def handle_type_name(object_type, variable, default_value, should_add_comma = false)
    if variable.instance_of? Dictionary
      output = dictionary_variable_declaration(variable)
    else
      output = single_field_declaration(variable)
    end

    output = variable.isBindable ? "Bind<#{output}>" : output
    output = variable.class == List ? "List<#{output}>" : output
    output = variable.isOptional ? output + "?" : output

    if default_value
      default = handle_default_variable(variable)
      output = !default.empty? && !is_interface(object_type) ? "#{output} = #{default}" : output
      output = should_add_comma ? output + "," : output
    end
    
    output
  end

  # Handle default value
  #
  # @param field [Field]
  # @return [String] default value
  def handle_default_variable(field)
    field.defaultValue.empty? && field.isOptional ? "null" : field.defaultValue
  end

  # Handle type and name for init method
  #
  # @param variable [Field]
  # @param should_add_comma [Bool]
  # @return [String] type and name for init method
  def handle_type_name_init_method(variable, should_add_comma = false)
    default = handle_default_variable(variable)
    if variable.instance_of? Dictionary
      output = dictionary_variable_declaration(variable)
    else
      output = single_field_declaration(variable)
    end
    output = variable.class == List ? "List<#{output}>" : output
    output = variable.isOptional ? output + "?" : output
    output = !default.empty? ? "#{output} = #{default}" : output
    output = should_add_comma ?  output + "," : output
    output
  end

  # Handle variable with Bind
  #
  # @param variable [Field]
  # @param should_add_comma [Bool]
  # @return [String] variable with Bind
  def handle_variable_with_bind(variable, should_add_comma = false)
    bind_helper = variable.isOptional ? "valueOfNullable" : "valueOf"
    output = variable.isBindable ? "#{bind_helper}(#{variable.name})" : variable.name
    output = should_add_comma ? output + "," : output
    output
  end

end