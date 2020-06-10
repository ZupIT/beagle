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

  spec.name = "BeagleSchema"

  spec.version = new_commit ? dev_version : last_release

  spec.summary = "This pod is a part of Beagle framework, it will probably not work alone"
  spec.description = <<-DESC
    This pod is a part of Beagle framework, it will probably not work alone
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

  spec.source = source

# ――― Source Code ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――― #
  
  path = 'iOS/Schema'
  path_source = path + '/Sources'

  spec.source_files = [
    path_source + '/**/*.swift'
  ]

  spec.exclude_files = [
    path + "/**/Test/**/*.swift",
    path + "/**/Tests/**/*.swift",
    path + "/**/*Test*.swift"
  ]

end
