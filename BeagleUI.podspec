#
# Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

tag = `git describe --tags --match "[0-9]*.[0-9]*.[0-9]*"` # e.g: "0.2.7-5-gc9c4730d"
(last_release, _, new_commit) = tag.split("-")

# dev version will only be used when user's Podfile references our git repository directly
#  without using tags for releases
dev_version = "#{last_release}-DEV-#{new_commit}"

Pod::Spec.new do |spec|

# ―――  Spec Metadata  ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.name = "BeagleUI"

  spec.version = new_commit ? dev_version : last_release

  spec.summary = "This is the Beagle framework for iOS"
  spec.description = <<-DESC
    We encourage you to use Beagle from Carthage, since it is the preferred
    way of using it. But if you are willing to just test Beagle, you can use this 
    pod instead.
  DESC
  spec.homepage = "https://docs.usebeagle.io"

# ―――  Spec License  ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.license = "Apache License 2.0"

# ――― Author Metadata  ――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.author = "Zup IT"

# ――― Platform Specifics ――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.platform = :ios, "10.0"
  spec.swift_version = "5.0"

# ――― Source Location ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  source = { :git => "git@github.com:ZupIT/beagle.git" }
  if new_commit
    source[:commit] = new_commit
  else
    source[:tag] = last_release
  end

  spec.source =  source

# ――― Source Code ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.default_subspec = "BeagleUI"

  # ――― Beagle UI ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #
  
  spec.subspec 'BeagleUI' do |beagleUI|
    path_source = 'iOS/Sources/BeagleUI/Sources'
    path_generated = 'iOS/Sources/BeagleUI/CodeGeneration/'

    beagleUI.source_files = [
      path_source + '/**/*.swift',
      path_generated + "Generated/*.generated.swift",
      path_generated + "*.swift"
    ]

    beagleUI.resources = [
      "iOS/**/*.xcdatamodeld",
      path_generated + "Templates/*"
    ]

    beagleUI.exclude_files = [ 
      path_source + "/**/Test/**/*.swift",
      path_source + "/**/Tests/**/*.swift",
      path_source + "/**/*Test*.swift"
    ]

    # make sure to declare YogaKit on your Podfile as:
    # pod 'YogaKit', :git => 'git@github.com:lucasaraujo/yoga.git'
    # We need this because we fixed an issue in the original repository and our PR was not merged yet.
    beagleUI.frameworks = 'Foundation', 'CoreData'
    beagleUI.dependency 'YogaKit'
    beagleUI.dependency 'BeagleSchema', "~> #{spec.version}"
  end

  # ――― Beagle Preview ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.subspec 'Preview' do |preview|
    source = 'iOS/Sources/Preview/Sources'
    preview.source_files = source + '/**/*.swift'
    preview.dependency 'Starscream'
  end

end
