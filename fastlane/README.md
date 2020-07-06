fastlane documentation
================
# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```
xcode-select --install
```

Install _fastlane_ using
```
[sudo] gem install fastlane -NV
```
or alternatively using `brew cask install fastlane`

# Available Actions
### deploy
```
fastlane deploy
```
Publish new release based on last two tags
### run_danger
```
fastlane run_danger
```


----

## Android
### android pull_request_verification
```
fastlane android pull_request_verification
```
Pull Request verification

----

## common
### common remote_publish
```
fastlane common remote_publish
```
Publish remote new version
### common local_publish
```
fastlane common local_publish
```
Publish local new version

----

## iOS
### ios pull_request_verification
```
fastlane ios pull_request_verification
```
Pull Request verification
### ios release
```
fastlane ios release
```
Updates versions on Podspec

----

## backend
### backend pull_request_verification
```
fastlane backend pull_request_verification
```
Pull Request verification
### backend sync_to_micronaut
```
fastlane backend sync_to_micronaut
```
Sync Spring BFF sample to Micronaut BFF sample
### backend sync_to_spring
```
fastlane backend sync_to_spring
```
Sync Micronaut BFF sample to Spring BFF sample

----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
