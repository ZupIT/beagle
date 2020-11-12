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
        @import_manager = Hash.new("")

        initImports(components)
    end

    def resolveImports(variables)
        variables
            .select {|variable| !variable.instance_of? Dictionary }
            .map { |variable| @import_manager[variable.typeName] }
            .uniq.filter { |import| !import.empty? }
    end
    
    private
    def initImports(components) 
        components.each do |clazz|
          component = clazz.new
          handleInnerImports(component)
        end
    end

    private
    def handleInnerImports(component)
        defineImport(component)
        
        for components in component.synthax_type.sameFileTypes
          handleInnerImports(components)
        end
    end

    private
    def defineImport(component) 
        type = component.synthax_type
        @import_manager[type.name] = "#{type.package}.#{type.name}"

        for inherit in type.inheritFrom
            @import_manager[inherit.synthax_type.name] = "#{inherit.synthax_type.package}.#{inherit.synthax_type.name}"

            for variable in type.variables
              if !@helper.variable_is_primitive(variable)
                @import_manager[variable.typeName] = "#{inherit.synthax_type.package}.#{variable.typeName}"
              end
            end
        end
    end
end