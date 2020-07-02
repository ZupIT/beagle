source "https://rubygems.org"

gem "fastlane"
gem "xcode-install"
gem "danger", "6.3.2"
gem 'danger-checkstyle_format'
gem "danger-android_lint"
gem 'danger-junit'
gem "danger-swiftlint"
gem 'cocoapods'

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)
