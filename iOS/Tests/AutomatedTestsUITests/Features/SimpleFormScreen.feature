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
    
@simpleform @regression
Feature: SimpleForm Component Validation

    As a Beagle developer/user
    I'd like to make sure my SimpleForm component works as expected
    In order to guarantee that my application never fails


    Scenario: SimpleForm 01 - SimpleForm component renders text attribute correctly
    Given the app did load simpleform screen
    Then all my simple form components should render their respective text attributes correctly
    
    Scenario Outline: SimpleForm 02 - SimpleForm component renders action attribute correctly
    Given the app did load simpleform screen
    When I click on text field "<textFieldText>"
    And insert text "<textValue>"
    And I click on text field "Complement"
    And I click on button "Enviar"
    Then confirm popup should appear correctly
    
    Examples:
    |textFieldText  | textValue              |
    |ZIP            | 38405142               |
 #   |Street         | Avenida Rondon Pacheco |
 #   |Number         | 4600                   |
 #   |Neighborhood   | Tibery                 |
 #   |City           | Uberlandia             |
 #   |State          | Minas Gerais           |
 #   |Complement     |                        |



