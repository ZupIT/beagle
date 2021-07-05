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

@confirm @android @ios
Feature: Confirm Action Validation

  As a Beagle developer/user
  I'd like to make sure my alert actions work as expected

  Background:
    Given the Beagle application did launch with the confirm screen url

  Scenario: Confirm01 - Validate confirm and alert properties
    Then validate the invoked alerts and its confirm properties:
      | BUTTON-TITLE                  | ALERT-CONTENT                                         | BUTTON-ACTION          |
      | JustAMessage                  | ConfirmMessage                                        |                        |
      | JustAMessageViaExpression     | ConfirmMessageViaExpression                           |                        |
      | TitleAndMessage               | ConfirmTitle;ConfirmMessage                           |                        |
      | TitleAndMessageViaExpression  | ConfirmTitleViaExpression;ConfirmMessageViaExpression |                        |
      | TriggersAnActionWhenConfirmed | ConfirmMessage                                        | Confirm ok clicked     |
      | TriggersAnActionWhenCanceled  | CancelMessage                                         | Confirm cancel clicked |
      | CustomConfirmLabel            | ConfirmMessage;CustomLabelOk;CustomLabelCancel        |                        |
