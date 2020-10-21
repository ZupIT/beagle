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
    
@button @regression
Feature: Button Component Validation

    As a Beagle developer/user
    I'd like to make sure my button component works as expected
    In order to guarantee that my application never fails

    Scenario: Button 01 - Button component renders text attribute correctly
    Given the app did load buttons screen
    Then all my button components should render their respective text attributes correctly
    
    Scenario Outline: Button 02 - Button component renders action attribute correctly
    Given the app did load buttons screen
    When I click on button "<buttonText>"
    Then component should render the action attribute correctly
    
    Examples:
    |buttonText                       |
    |Button                           |
    |Button with style                |
    |Button with Appearance           |
    |Button with Appearance and style |


# Snapshot Test
#    Scenario: Button 00 - Button component renders style attribute correctly
#    Given that I'm on the button screen
#    When I click on a component with a valid style attribute configured
#    Then component should render the style attribute correctly
