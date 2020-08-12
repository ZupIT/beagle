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

require 'fileutils'

class FileHandler
    
    def write(path, fileName, dataToBeWritten) 
        handleDirectoryCreationIfNeeded(path)
        File.open(path + fileName, "w+") do |f|     
            f.write(dataToBeWritten)
            f.close
        end
    end

    def handleDirectoryCreationIfNeeded(path)
        FileUtils.mkdir_p path unless File.exists?(path)
    end

end