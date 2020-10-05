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
    
@navigation @regression
Feature: Navigation Action Validation

    As a Beagle developer/user
    I'd like to make sure my navigation action works as expected
    In order to guarantee that my application never fails
    
    Scenario: Navigation 01 - Navigation action navigate from one screen to another
    Given the app did load a screen with a navigation action
    When I click on button "<title>"
    Then the screen should navigate to another screen with text label "<text>"

    Example:

    |title| text|
    |PushStackRemote|PushStackRemoteScreen|
    |PushViewRemote|PushViewRemoteScreen|
    
    Scenario: Navigation 02 - navigation action navigate to some invalid route
    Given the app did load a screen with a navigation action
    When I click on button "<title>"
    Then the screen should show a error message

    Example:

    |title| text|
    |PushStackRemoteFailure|PushStackRemoteFailureScreen|
    |PushViewRemoteFailure|PushViewRemoteFailureScreen|
