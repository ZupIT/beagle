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

require './Synthax/variable.rb'
require './Synthax/type.rb'
require './Models/button.rb'
require './FileHandler/file_handler.rb'
require './Common/constants.rb'

class ModelGenerator
  
  @writer = FileHandler.new

  def initialize(objectType = nil, fileName = "")
    @objectType = objectType
    @erb = ERB.new(File.read(fileName), nil, '-')
  end
  
  attr_accessor :objectType

  def to_s
    @erb.result(binding)
  end

  def generateKotlin
    # TODO
  end
  
  def generateSwift
    # TODO
  end


end

if __FILE__ == $0
  writer = FileHandler.new
  
  swiftGenerator = ModelGenerator.new(Button.new, 'model_template_swift.erb')
  writer.write(Constants.new.swift_path + "Button.swift", swiftGenerator.to_s)

  kotlinGenerator = ModelGenerator.new(Button.new, 'model_template_kotlin.erb')
  writer.write(Constants.new.kotlin_path + "Button.kt", kotlinGenerator.to_s)
  
end