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

@setContext @android @ios
Feature: SetContext Action Validation

  As a Beagle developer/user
  I'd like to make sure my SetContext actions work as expected

  Background:
    Given the Beagle application did launch with the SetContext screen url

  Scenario: Conditional 01 - Validate alert conditions
    Then validate the invoked context buttons and its actions:
      | BUTTON-TITLE        | ACTION-TEXT         |
      | HardcodedValue      | ValueHardcoded      |
      | HardcodedPathValue  | ValueHardcodedPath  |
      | ExpressionValue     | ValueExpression     |
      | ExpressionPathValue | ValueExpressionPath |


