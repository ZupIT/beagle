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

@sendrequest @android @ios
Feature: Send Request Action Validation

    As a Beagle developer/user
    I'd like to make sure my send request action work as expected
    In order to guarantee that my application never fails

    Background:
        Given the Beagle application did launch with the send request screen url

    Scenario Outline: Send Request 01 - The send request action get some result and show alert
        When I press the <buttonTitle> button
        Then the screen should show some alert with <alertTitle> title

        Examples:
            | buttonTitle          | alertTitle |
            | request with success | Success    |
            | request with error   | Error      |


    Scenario Outline: Send Request 02 - The send request onFinish action get some result and modify the pressed button
        When I press the <title> button
        Then the pressed button changes it's <title> title to didFinish

        Examples:
            | title                 |
            | onFinish with success |
            | onFinish with error   |

    Scenario Outline: Send Request 03 - The send request action get some result and show alert when setting the
    URL as an expression
        When I press the <buttonTitle> button
        Then the screen should show some alert with <alertTitle> title

        Examples:
            | buttonTitle                               | alertTitle |
            | request with success using expression URL | Success    |

    Scenario Outline: Send Request 04 - The send request action get some result and show alert when setting the
    METHOD as an expression
        When I press the <buttonTitle> button
        Then the screen should show some alert with <alertTitle> title

        Examples:
            | buttonTitle                    | alertTitle |
            | request with expression method | Success    |