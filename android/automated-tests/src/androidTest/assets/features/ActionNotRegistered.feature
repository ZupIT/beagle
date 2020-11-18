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

@unregisteredaction @regression
Feature: Unregistered action Validation

    As a Beagle developer/user
    I'd like to make sure my application does not crash when an unregistered action is triggered
    In order to guarantee that my application never fails

    Background:
        Given the Beagle application did launch with the ActionNotRegistered Screen url

    Scenario: Unregistered action 01 - The application mustn't crash when an unregistered action is triggered
        When I click on button ClickToCallActionNotRegistered
        Then nothing happens and the ActionNotRegistered Screen should still be visible