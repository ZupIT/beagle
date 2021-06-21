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

@alert @android @ios
Feature: Alert Action Validation

  As a Beagle developer/user
  I'd like to make sure my alert actions work as expected

  Background:
    Given the Beagle application did launch with the alert screen url

  Scenario: Alert 01 - Validate alert properties
    Then validate the invoked alerts and its properties:
      | BUTTON-TITLE                 | ALERT-TITLE               | ALERT-MESSAGE             | ALERT-BUTTON-TITTLE |
      | JustAMessage                 | AlertMessage              |                           | OK                  |
      | JustAMessageViaExpression    | AlertMessageViaExpression |                           | OK                  |
      | TitleAndMessage              | AlertTitle                | AlertMessage              | OK                  |
      | TitleAndMessageViaExpression | AlertTitleViaExpression   | AlertMessageViaExpression | OK                  |
      | CustomAlertButton            | AlertMessage              |                           | CUSTOMLABEL         |

  Scenario: Alert 02 - The alert should display an action on a confirmation
    When I click on text AlertTriggersAnAction
    And I click on button OK
    Then a dialog should appear on the screen with text SecondAlert
  
