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

# This is one of the core classes of beagle schema. This is the object that will be processed in the ERB templates.
# You should NOT use this class directly. Instead, create a class that inherits from this one.
class BaseComponent

    # An attribute that represents the type of the component that will be generated
    # @see BaseType
    # @return [BaseType]
    attr_accessor :synthax_type

    # Initializer for Base Component
    #
    # @param type [BaseType] {synthax_type Same as synthax_type}
    def initialize(type)
        @synthax_type = type
    end

    # Converted string name of a BaseComponent. If used by a child class, the output is
    # the name of the child class
    def name
        self.class.to_s
    end

end