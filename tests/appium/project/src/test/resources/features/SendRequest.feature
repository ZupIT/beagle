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

@sendRequest @android @ios
Feature: Send Request Action Validation

  As a Beagle developer/user
  I'd like to make sure my send request action work as expected

  Background:
    Given the Beagle application did launch with the send request screen url

  Scenario: SendRequest 01 - validate sendRequest scenarios
    Then validate the invoked buttons and its sendRequest actions:
      | BUTTON-TITLE                              | ALERT-TITLE |
      | request with success                      | Success     |
      | request with error                        | Error       |
      # onFinish buttons change their tittle to didFinish when clicked
      | onFinish with success                     |             |
      | onFinish with error                       |             |
      | request with success using expression URL | Success     |
      | request with expression method            | Success     |