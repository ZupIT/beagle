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

# Use this class when you attempt to generate a list
class Package

	# Package defined for Backend
    # @return [String] 
    attr_accessor :backend

    # Package defined for Android
    # @return [Bool] 
    attr_accessor :android

    
    def initialize(params = {})
        @backend = params.fetch(:backend, 'br.com.zup.beagle.generated')
        @android = params.fetch(:android, 'br.com.zup.beagle.generated')
    end

end