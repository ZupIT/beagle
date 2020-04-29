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

Pod::Spec.new do |spec|

  # ―――  Spec Metadata  ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.name = "BeagleUI"
  spec.version = "0.1.0"
  spec.summary = "This is the Beagle framework for iOS"
  spec.description = <<-DESC
    We encourage you to use Beagle from Carthage, since it is the preferred
    way of using it. But if you are willing to just test Beagle, you can use this 
    pod instead.
  DESC
  spec.homepage = "https://zup-products.gitbook.io/beagle/"

  # ―――  Spec License  ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.license = "Apache License 2.0"

  # ――― Author Metadata  ――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.author = "Zup IT"

  # ――― Platform Specifics ――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.platform = :ios, "10.0"
  spec.swift_version = "4.1"

  # ――― Source Location ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.source = { :git => "git@github.com:ZupIT/beagle.git", :branch => "master" }

  # ――― Source Code ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #
  # ――― Beagle UI ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.default_subspec = "BeagleUI"
  
  spec.subspec 'BeagleUI' do |beagleUI|
    source = 'iOS/Sources/BeagleUI/Sources'
    beagleUI.source_files = source + '/**/*.swift'
    beagleUI.resources = "iOS/**/*.xcdatamodeld"
    beagleUI.exclude_files = 
      source + "/**/Test/**/*.swift",
      source + "/**/Tests/**/*.swift",
      source + "/**/*Tests*.swift",
      source + "/**/*Test*.swift",
      source + "/**/*Tests.swift",
      source + "/**/*Test.swift"
    # make sure to declare YogaKit on your Podfile as:
    # pod 'YogaKit', :git => 'git@github.com:lucasaraujo/yoga.git'
    # We need this because we fixed an issue in the original repository and our PR was not merged yet.
    beagleUI.frameworks = 'Foundation', 'CoreData'
    beagleUI.dependency 'YogaKit'
  end

  # ――― Beagle Preview ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #

  spec.subspec 'Preview' do |preview|
    source = 'iOS/Sources/Preview/Sources'
    preview.source_files = source + '/**/*.swift'
    preview.dependency 'Starscream'
  end

end
