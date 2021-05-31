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

@simpleForm @android @ios
Feature: SimpleForm Component Validation

  As a Beagle developer/user
  I'd like to make sure my SimpleForm component works as expected
  In order to guarantee that my application never fails

  Background:
    Given that I'm on the simple form screen

  Scenario: SimpleForm 01 - Validate if SimpleForm children components appear on the screen
    Then the screen should show an element with the place holder Type in your email
    Then the screen should show an element with the place holder Type in your name
    Then the screen should show an element with the title Click to Submit

  Scenario Outline: SimpleForm 02 - Validate the operation of the onSubmit attribute
    When I click on textInput with place holder Type in your email and insert the value <email>
    And I click on textInput with place holder Type in your name and insert the value <name>
    And I click on button Click to Submit
    Then a dialog should appear on the screen with text the email: <email> and the name: <name>

    Examples:
      | email                | name |
      | teste@simpleform.com | joao |
