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

require_relative 'Models/Layout/Flex/flex.rb'
require_relative 'Models/Layout/style.rb'
require_relative 'Models/Layout/unit_value.rb'
require_relative 'Models/Accessibility/accessibility.rb'
require_relative 'Models/Analytics/touchable_analytics.rb'
require_relative 'Models/Analytics/click_event.rb'
require_relative 'Models/Widgets/button.rb'
require_relative 'Models/Widgets/widget.rb'
require_relative 'Models/Widgets/style_component.rb'
require_relative 'Models/Widgets/accessibility_component.rb'
require_relative 'Models/Widgets/identifier_component.rb'
require_relative 'Models/ServerDriven/server_driven_component.rb'

require_relative 'FileHandler/file_handler.rb'
require_relative 'Common/constants.rb'

require_relative 'Templates/template_helper.rb'
require_relative 'Templates/kotlin_template_helper.rb'

# This is the main class of Beagle Schema
class ModelGenerator
  
  # Initializer for ModelGenerator
  #
  # @param components [BaseComponent] array of base components that will be translated to other languages
  def initialize(components)
    @objectType = nil
    @erb = nil
    @writer = FileHandler.new
    @components = components
    @c = Constants.new

    @helper = TemplateHelper.new
    @kotlinHelper = KotlinTemplateHelper.new
    @tab = "    "
  end

  # Array of BaseComponents
  # @return [Array<BaseComponent>]
  attr_accessor :objectType

  # This method is used to trigger the logic for code generation inside the templates
  # @return [String] the result of this method return a string that will be saved in a file
  def to_s
    @erb.result(binding)
  end

  # Generates models for all the supported languages
  def generate
    generate_swift
    generate_kotlin_android
    generate_kotlin_backend
    generate_ts
  end

  # Generates models for kotlin Android
  def generate_kotlin_android
    @erb = ERB.new(File.read("#{@c.templates}kotlin_android.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      @writer.write(@c.kotlin_android_path + @objectType.synthax_type.package.android.gsub(".", "/") + "/", @objectType.name + ".kt", to_s)
    end

    puts "Kotlin Android models generated!"

  end

  # Generates models for kotlin backend
  def generate_kotlin_backend
    to_widgets = [
      Button.new.name,
      Action.new.name
    ]
    
    @erb = ERB.new(File.read("#{@c.templates}kotlin_backend.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      path = @c.kotlin_core_backend_path

      if to_widgets.include? @objectType.name
        path = @c.kotlin_widgets_backend_path
      end

      path += @objectType.synthax_type.package.backend.gsub(".", "/") + "/"

      @writer.write(path, @objectType.name + ".kt", to_s)
    end

    puts "Kotlin Backend models generated!"
  end
  
  # Generates models for swift
  def generate_swift
    ready_to_prod = [
      Button.new.name,
      # Widget.new.name,
      IdentifierComponent.new.name,
      # ServerDrivenComponent.new.name,
      TouchableAnalytics.new.name,
      StyleComponent.new.name,
      AccessibilityComponent.new.name,
      ClickEvent.new.name,
      Flex.new.name,
      Style.new.name,
      UnitValue.new.name,
      Accessibility.new.name
    ]
    @erb = ERB.new(File.read("#{@c.templates}swift.erb"), nil, '-')
    for component in @components
      @objectType = component.new
      path = @c.swift_path

      if ready_to_prod.include? @objectType.name
        path += "../../../../iOS/Sources/Beagle/Sources/SchemaGenerated/"
      end

      @writer.write(path, @objectType.name + ".swift", to_s)
    end
  end

  # Generates models for type script
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
    Widget,
    IdentifierComponent,
    ServerDrivenComponent,
    TouchableAnalytics,
    StyleComponent,
    AccessibilityComponent,
    ClickEvent,
    Flex,
    Style,
    UnitValue,
    Accessibility
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
    generator.generate_kotlin_android
    generator.generate_kotlin_backend
  when "kotlinAndroid"
    generator.generate_kotlin_android
  when "kotlinBackend"
    generator.generate_kotlin_backend
  when "all"
    generator.generate
    puts "All language #{message}"
  else
    "You gave me #{ARGV[0]} -- I have no idea what to do with that."
  end

end