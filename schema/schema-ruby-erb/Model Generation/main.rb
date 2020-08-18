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

require 'erb'
require 'date'

require './Synthax/Attributes/variable.rb'
require './Synthax/Types/type.rb'

require './Models/Layout/corner_radius.rb'
require './Models/Layout/display.rb'
require './Models/Layout/edge_value.rb'
require './Models/Layout/Flex/flex.rb'
require './Models/Layout/position_type.rb'
require './Models/Layout/size.rb'
require './Models/Layout/style.rb'
require './Models/Layout/unit_value.rb'

require './Models/Widgets/button.rb'
require './Models/Widgets/text.rb'

require './FileHandler/file_handler.rb'
require './Common/constants.rb'

class ModelGenerator
  
  def initialize(components)
    @objectType = nil
    @erb = nil
    @writer = FileHandler.new
    @components = components
    @importManager = Hash.new("")
    
    components.each do |component|
      type = component.new.synthaxType
      @importManager[type.name] = "#{type.package}.#{type.name}"
    end
  end
  
  attr_accessor :objectType, :importManager

  def to_s
    @erb.result(binding)
  end

  def generate
    generateSwift
    generateKotlin
    generateKotlinBackend
    generateTs
  end

  private

  def generateKotlin
    @erb = ERB.new(File.read("model_template_kotlin.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      @writer.write(Constants.new.kotlin_path, @objectType.name + "Schema.kt", to_s)
    end
  end

  def generateKotlinBackend
    @erb = ERB.new(File.read("model_template_kotlin_backend.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      @writer.write(Constants.new.kotlin_backend_path, @objectType.name + ".kt", to_s)
    end
  end
  
  def generateSwift
    @erb = ERB.new(File.read("model_template_swift.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      @writer.write(Constants.new.swift_path, @objectType.name + ".swift", to_s)
    end
  end

  def generateTs
    @erb = ERB.new(File.read("model_template_ts.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      @writer.write(Constants.new.ts_path, @objectType.name + ".ts", to_s)
    end
  end

end

if __FILE__ == $0
  components = [
    # Components
    Button,
    Text,
    # Layout
    CornerRadius,
    EdgeValue,
    Flex,
    Size,
    Style,
    UnitValue
  ]
  
  ModelGenerator.new(components).generate

end