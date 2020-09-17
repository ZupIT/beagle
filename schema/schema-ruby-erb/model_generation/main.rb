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

require_relative 'Synthax/Attributes/variable.rb'
require_relative 'Synthax/Types/type.rb'

require_relative 'Models/Layout/corner_radius.rb'
require_relative 'Models/Layout/display.rb'
require_relative 'Models/Layout/edge_value.rb'
require_relative 'Models/Layout/Flex/flex.rb'
require_relative 'Models/Layout/position_type.rb'
require_relative 'Models/Layout/size.rb'
require_relative 'Models/Layout/style.rb'
require_relative 'Models/Layout/unit_value.rb'
require_relative 'Models/Layout/unit_type.rb'

require_relative 'Models/Accessibility/accessibility.rb'

require_relative 'Models/Analytics/analytics_events.rb'
require_relative 'Models/Analytics/analytics_models.rb'

require_relative 'Models/Widgets/button.rb'
require_relative 'Models/Widgets/text.rb'
require_relative 'Models/Widgets/text_input.rb'
require_relative 'Models/Widgets/text_input_type.rb'
require_relative 'Models/Widgets/container.rb'
require_relative 'Models/Widgets/image.rb'
require_relative 'Models/Widgets/image_content.rb'

require_relative 'FileHandler/file_handler.rb'
require_relative 'Common/constants.rb'

class ModelGenerator
  
  def initialize(components)
    @objectType = nil
    @erb = nil
    @writer = FileHandler.new
    @components = components
    @importManager = Hash.new("")
    @c = Constants.new

    components.each do |component|
      type = component.new.synthax_type
      @importManager[type.name] = "#{type.package}.#{type.name}"
    end
  end
  
  attr_accessor :objectType, :importManager

  def to_s
    @erb.result(binding)
  end

  def generate
    generate_swift
    generate_kotlin
    generate_kotlin_backend
    generate_ts
  end

  def generate_kotlin
    @erb = ERB.new(File.read("#{@c.templates}kotlin.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      @writer.write(@c.kotlin_path, @objectType.name + "Schema.kt", to_s)
    end
  end

  def generate_kotlin_backend
    @erb = ERB.new(File.read("#{@c.templates}kotlin_backend.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      if @objectType.synthax_type.class == BaseType
        @writer.write(@c.kotlin_backend_path, @objectType.name + ".kt", to_s)
      end
    end
  end
  
  def generate_swift
    @erb = ERB.new(File.read("#{@c.templates}swift.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      @writer.write(@c.swift_path, @objectType.name + ".swift", to_s)
    end
  end

  def generate_ts
    @erb = ERB.new(File.read("#{@c.templates}ts.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      @writer.write(@c.ts_path, @objectType.name + ".ts", to_s)
    end
  end

end

if __FILE__ == $0

  components = [
    # Components
    Button,
    Text,
    TextInputType,
    ImageContentMode,
    # Layout
    CornerRadius,
    EdgeValue,
    Flex,
    Size,
    Style,
    UnitValue,
    UnitType,
    # Accessibility
    Accessibility,
    # Far from being usable
    Container,
    Image,
    TextInput,
    AnalyticsEvent,
    AnalyticsClick,
    AnalyticsScreen
  ]
  
  generator = ModelGenerator.new(components)
  message = "models generated!"
  
  Dir.chdir(ARGV[0])
  
  case ARGV[1]
  when "swift"
    generator.generate_swift
    puts "Swift #{message}"
  when "ts"
    generator.generate_ts
    puts "Type Script #{message}"
  when "kotlin"
    generator.generate_kotlin
    puts "Kotlin #{message}"
    generator.generate_kotlin_backend
    puts "Kotlin Backend #{message}"
  when "all"
    generator.generate
    puts "All language #{message}"
  else
    "You gave me #{ARGV[0]} -- I have no idea what to do with that."
  end

end