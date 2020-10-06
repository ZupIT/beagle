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
    
    Scenario Outline: Navigation 01 - navigation action navigates to a valid route
        Given the Beagle application did launch with the navigation screen url
        When I click on a navigate button "<title>"
        Then the screen should navigate to another screen with text label "<text>"

        Examples:
        |       title        |           text             |
        |   PushStackRemote  |    PushStackRemoteScreen   |
        |   PushViewRemote   |    PushViewRemoteScreen    |
    
    Scenario Outline: Navigation 02 - navigation action navigates to an invalid route
        Given the Beagle application did launch with the navigation screen url
        When I click on a navigate button "<title>"
        Then the screen should not navigate to another screen with text label "<text>"

        Examples:
        |           title           |               text                |
        |   PushStackRemoteFailure  |    PushStackRemoteFailureScreen   |
        |   PushViewRemoteFailure   |    PushViewRemoteFailureScreen    |
