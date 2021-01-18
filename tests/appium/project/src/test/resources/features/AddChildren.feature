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

@addChildren @android @ios
Feature: AddChildren validation
    As a Beagle developer/user
    I'd like to make sure my addChildren action works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the addChildren Screen

    Scenario: Default - AddChildren append a Text in the container children
        When I click on button DEFAULT
        Then A Text needs to be added after the already existing one

    Scenario: Prepend - AddChildren prepend a Text in the container children
        When I click on button PREPEND
        Then A Text needs to be added before the already existing one

    Scenario: Append - AddChildren append a Text in the container children
        When I click on button APPEND
        Then A Text needs to be added after the already existing one

    Scenario: Replace - AddChildren replace the children container to a Text
        When I click on button REPLACE
        Then A Text needs to replace the already existing one

    Scenario: Prepend component doesn't exist - AddChildren replace the children container to a Text
        When I click on button PREPEND COMPONENT NULL
        Then Nothing should happen

    Scenario: Append component doesn't exist - AddChildren replace the children container to a Text
        When I click on button APPEND COMPONENT NULL
        Then Nothing should happen

    Scenario: Replace component doesn't exist - AddChildren replace the children container to a Text
        When I click on button REPLACE COMPONENT NULL
        Then Nothing should happen