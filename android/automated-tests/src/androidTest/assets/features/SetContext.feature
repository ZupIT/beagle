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

@setcontext @regression
Feature: SetContext Action Validation

    As a Beagle developer/user
    I'd like to make sure my SetContext actions work as expected
    In order to guarantee that my application never fails

    Background:
        Given the Beagle application did launch with the SetContext screen url

    Scenario Outline: Scenario Outline: SetContext 01 - A context value should change when a SetContext action
    is triggered setting a hardcoded value on that context
        When I press a SetContext button with the <buttonTitle> title
        Then a text with the <message> message should appear on the screen

        Examples:
            | buttonTitle        | message            |
            | HardcodedValue     | ValueHardcoded     |
            | HardcodedPathValue | ValueHardcodedPath |


    Scenario Outline: Scenario Outline: SetContext 02 - A context value should change when a SetContext action
    is triggered setting value via expression on that context
        When I press a SetContext button with the <buttonTitle> title
        Then a text with the <message> message should appear on the screen

        Examples:
            | buttonTitle         | message             |
            | ExpressionValue     | ValueExpression     |
            | ExpressionPathValue | ValueExpressionPath |