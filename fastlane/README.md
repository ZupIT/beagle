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
### android remote_publish
```
fastlane android remote_publish
```
Publish remote new version
### android local_publish
```
fastlane android local_publish
```
Publish local new version

----

## iOS
### ios pull_request_verification
```
fastlane ios pull_request_verification
```
Pull Request verification

----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
