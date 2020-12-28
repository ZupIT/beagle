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

class Constants

    def initialize
        generated_path = "Generated/"
        @swift_path = generated_path + "Swift/"
        @kotlin_android_path = "../../android/beagle/src/main/java/"
        @kotlin_core_backend_path = "../../backend/kotlin-core/src/main/kotlin/"
        @kotlin_widgets_backend_path = "../../backend/widgets/src/main/kotlin/"
        @ts_path = generated_path + "Ts/"
        @templates = "Templates/"
    end

    attr_accessor :swift_path, :kotlin_android_path, :kotlin_core_backend_path, :kotlin_widgets_backend_path, :ts_path, :templates
end


