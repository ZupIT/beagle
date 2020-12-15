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
    
@alert @regression
Feature: Alert Component Validation

    As a Beagle developer/user
    I'd like to make sure my alert component work as expected
    In order to guarantee that my application never fails
    
    Background:
        Given the Beagle application did launch with the alert screen url
    
    Scenario Outline: Alert 01 - The alert should display a message
        When I press an alert button with the "<buttonTitle>" title
        Then an alert with the "<message>" message should appear on the screen

        Examples:
        |         buttonTitle             |               message                |
        |   JustAMessage                  |    AlertMessage                      |
        |   JustAMessageViaExpression     |    AlertMessageViaExpression         |
        
    Scenario Outline: Alert 02 - The alert should display a title and a message
        When I press an alert button with the "<buttonTitle>" title
        Then an alert with the "<title>" title and the "<message>" message should appear on the screen

        Examples:
        |         buttonTitle             |            title            |           message            |
        |   TitleAndMessage               |    AlertTitle               |   AlertMessage               |
        |   TitleAndMessageViaExpression  |    AlertTitleViaExpression  |   AlertMessageViaExpression  |


    Scenario: Alert 03 - The alert should trigger an action when pressing its confirmation button
        When I press an alert button with the "AlertTriggersAnAction" title
        When I press an alert button with the "Ok" title
        Then a second alert with the "SecondAlert" title should appear on the screen
        
    Scenario: Alert 04 - The alert should display its confirmation button with a custom title
        When I press an alert button with the "CustomAlertButton" title
        Then an alert should appear on the screen and its confirmation button should display the "CustomLabel" custom title
