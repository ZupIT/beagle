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

require_relative 'base_attributes.rb'
require_relative 'Mixins/bindable.rb'
require_relative 'Mixins/optional.rb'
require_relative 'Mixins/single_type_name.rb'

# Use this class when you attempt to generate a list
class Constant < BaseAttributes
    include Bindable, Optional, SingleTypeName

    def initialize(params = {})
        super
        @typeName = params.fetch(:typeName, '')
        @isBindable = params.fetch(:isBindable, false)
        @isOptional = params.fetch(:isOptional, false)
    end

end