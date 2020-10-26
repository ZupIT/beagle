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

@textInput @regression
Feature: TextInput Validation

    As a Beagle developer/user
    I'd like to make sure my textInput work as expected

    Background:
        Given the Beagle application did launch with the textInput on screen

    Scenario Outline: TextInput 01 - validate value field if filled
        Then I must check if the textInput value "<textInputValue>" appears on the screen

        Examples:
            | textInputValue            |
            | TextInput test            |
            | TextInput with expression |

    Scenario Outline: TextInput 02 - validate placeholder field if filled
        Then I must check if the textInput placeholder "<textInputPlaceholder>" appears on the screen

        Examples:
            | textInputPlaceholder                  |
            | TextInput placeholder                 |
            | TextInput placeholder with expression |
    
    Scenario Outline: TextInput 03 - validate disabled field
        Then verify if the field with the placeholder "<textInputDisabled>" is disabled

        Examples:
            | textInputDisabled                                 |
            | Standard text with disabled field                 |
            | Standard text with disabled field with expression |
