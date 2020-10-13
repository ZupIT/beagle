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

@sendrequest @regression
Feature: SendRequest Component Validation

    As a Beagle developer/user
    I'd like to make sure my sendRequest component works as expected

    Scenario Outline:
        Given that I'm on the screen with a button for call a sendRequest and to realize the http requests <text1>
        When I click on sendRequestSuccess button <text2>
        Then the component should return an alert with a message onSuccess <text3> and onFinished <text4>

        Examples:
            | text1               | text2                | text3   | text4      |
            | Send request Screen | Send request success | Success | onFinished |

    Scenario Outline:
        When I click on sendRequestError button <text2>
        Then the component should return an alert with a message onError <text3> and SendRequestError <text4>

        Examples:
            | text2              | text3   | text4              |
            | Send request error | Error   | Send request error |





