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

@confirm @regression
Feature: Confirm Action Validation

    As a Beagle developer/user
    I'd like to make sure my confirm actions work as expected
    In order to guarantee that my application never fails

    Background:
        Given the Beagle application did launch with the confirm screen url

    Scenario Outline: Confirm 01: Checks that a confirmation with just a message shows on the screen when set HARDCODED and via EXPRESSION
        When I press a confirm button with the "<buttonTitle>" title
        Then a confirm with the "<message>" message should appear on the screen

        Examples:
            | buttonTitle               | message                     |
            | JustAMessage              | ConfirmMessage              |
            | JustAMessageViaExpression | ConfirmMessageViaExpression |


    Scenario Outline: Confirm 02 - Checks that a confirmation with a TITLE and a MESSAGE shows on the screen when set HARDCODED and via EXPRESSION
        When I press a confirm button with the "<buttonTitle>" title
        Then a confirm with the "<title>" and "<message>" should appear on the screen

        Examples:
            | buttonTitle                  | title                     | message                     |
            | TitleAndMessage              | ConfirmTitle              | ConfirmMessage              |
            | TitleAndMessageViaExpression | ConfirmTitleViaExpression | ConfirmMessageViaExpression |

    Scenario: Confirm 03 - Checks that a the onPressOk method is triggered when the OK button is pressed to confirm component.
        When I press an confirm button with the "TriggersAnActionWhenConfirmed" title
        Then I press the confirmation "Ok" button on the confirm component
        Then a confirm with the "Confirm ok clicked" message should appear on the screen

    Scenario: Confirm 04 - Checks that a the onPressCancel method is triggered when the Cancel button is pressed to confirm component.
        When I press an confirm button with the "TriggersAnActionWhenCanceled" title
        Then I press the confirmation "Cancel" button on the confirm component
        Then a confirm with the "Confirm cancel clicked" message should appear on the screen

    Scenario: Confirm 05 - Shows a Confirm message with customized text on the LabelOk and LabelCancel button.
        When I press an confirm button with the "CustomConfirmLabel" title
        Then a confirm with the "CustomLabelOk" and "CustomLabelCancel" buttons should appear on the screen
