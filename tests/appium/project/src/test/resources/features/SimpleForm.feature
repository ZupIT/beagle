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

  # TODO: convert many of these steps to generic ones

@simpleForm @android @ios
Feature: SimpleForm Component Validation

  As a Beagle developer/user
  I'd like to make sure my SimpleForm component works as expected

  Background:
    Given that I'm on the simple form screen

  Scenario: SimpleForm 01 - Validate the operation of the onSubmit attribute
    When I click on the input with place holder "Type in your email" and insert "teste@simpleform.com"
    And I click on the input with place holder "Type in your name" and insert "joao"
    And I click on button Click to Submit
    Then the screen should show an element with the title the email: teste@simpleform.com and the name: joao

