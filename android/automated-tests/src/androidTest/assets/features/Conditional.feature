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

@conditional @regression
Feature: Conditional Action Validation

    As a Beagle developer/user
    I'd like to make sure my conditional actions work as expected
    In order to guarantee that my application never fails

    Background:
        Given the Beagle application did launch with the conditional screen url

    Scenario Outline: Conditional 01 - Checks that the onTrue method is triggered when the condition
    is set TRUE HARDCODED and via EXPRESSION
        When I click in a conditional button with <buttonTitle> title
        Then an Alert action should pop up with a TrueCondition message

        Examples:
            | buttonTitle                   |
            | Condition true                |
            | Condition via expression true |

    Scenario Outline: Conditional 02 - Checks that the onFalse method is triggered when the condition
    is set FALSE HARDCODED and via EXPRESSION
        When I click in a conditional button with <buttonTitle> title
        Then an Alert action should pop up with a FalseCondition message

        Examples:
            | buttonTitle                    |
            | Condition false                |
            | Condition via expression false |

    Scenario: Conditional 03 - Checks that the onTrue method is triggered when an operation returns TRUE
    on the condition attribute
        When I click in a conditional button with Condition via valid expression operation title
        Then an Alert action should pop up with a TrueCondition message

    Scenario: Conditional 04 -  Checks that the onFalse method is triggered when a invalid expression is set
    on the condition attribute
        When I click in a conditional button with Condition via invalid expression title
        Then an Alert action should pop up with a FalseCondition message
