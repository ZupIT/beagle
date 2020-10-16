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
    I'd like to make sure my navigation actions work as expected
    In order to guarantee that my application never fails
    
    Background:
        Given the Beagle application did launch with the navigation screen url
    
    Scenario Outline: Navigation 01 - The push actions navigate to a valid route
        When I press a navigation button "<title>"
        Then the screen should navigate to another screen with the text label "<text>"

        Examples:
        |            title             |                  text                |
        |   PushStackRemote            |    PushStackRemoteScreen             |
        |   PushViewRemote             |    PushViewRemoteScreen              |
        |   PushStackRemoteExpression  |    PushStackRemoteExpressionScreen   |
        |   PushViewRemoteExpression   |    PushViewRemoteExpressionScreen    |
    
    Scenario Outline: Navigation 02 - The push actions navigate to an invalid route
        When I press a navigation button "<title>"
        Then the screen should not navigate to another screen with the text label "<text>"

        Examples:
        |                title                |                     text                    |
        |   PushStackRemoteFailure            |    PushStackRemoteFailureScreen             |
        |   PushViewRemoteFailure             |    PushViewRemoteFailureScreen              |
        |   PushViewRemoteExpressionFailure   |    PushViewRemoteExpressionFailureScreen    |
        |   PushStackRemoteExpressionFailure  |    PushStackRemoteExpressionFailureScreen   |
        
    Scenario: Navigation 03 - 'popView' action dismisses the current screen
        When I navigate to another screen using the "PushViewRemote" action and I press a button with the "PopView" action
        Then the app should dismiss the current view
        
    Scenario: Navigation 04 - 'popToView' action with an invalid route not dismiss the current screen
        When I navigate to another screen using the "PushViewRemote" action and I press a button with the "PopToViewInvalidRoute" action
        Then the app should not dismiss the current view
        
    Scenario: Navigation 05 - 'popToView' action navigates to a specified route of a screen on the stack and cleans up the navigation that was generated from this screen
        When I navigate to another screen using the "PushViewRemote" action and I press a button with the "PopToView" action
        Then the application should navigate back to a specific screen and remove from the stack the other screens loaded from the current screen

    Scenario Outline: Navigation 06 - navigates to a specified screen and cleans up the stack of the previously loaded screens
        When I navigate to another screen using the "<action1>" action and I press a button with the "<action2>" action
        Then the app should navigate to a specified screen and cleans up the entire stack of the previously loaded views
        
        Examples:
        |       action1     |       action2                             |
        |   PushViewRemote  |   ResetStack                              |
        |   PushStackRemote |   ResetApplication                        |
        |   PushViewRemote  |   ResetStackExpression                    |
        |   PushStackRemote |   ResetApplicationExpression              |
        |   PushViewRemote  |   ResetStackExpressionFallback            |
        |   PushStackRemote |   ResetApplicationExpressionFallback      |
        
    Scenario Outline: Navigation 07 - removes the single existing stack of views
        When I navigate to another screen using the "PushViewRemote" action and I press a button with the "<action>" action
        Then the app should clean up the entire stack and the application should enter in the foreground state

        Examples:
        |       action          |
        |   PopStack            |
        |   ResetApplication    |
