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

@scrollview @regression
Feature: ScrollView Component Validation

    As a Beagle developer/user
    I'd like to make sure my scrollView component works as expected
    In order to guarantee that my application never fails

    Background:
        Given the Beagle application did launch with the scrollview screen url

    Scenario Outline: ScrollView 01 - scrollView component should perform the orientation scroll correctly
        When I press on text scroll "<text>"
        Then the current text "<text>" should be replaced for a large text and the scrollview should perform in the specified orientation "<buttonTitle>"

        Examples:
        |                 text                    |         buttonTitle         |
        | Click to see the new text in vertical   | verticalScrollTest          |
        | Click to see the new text in horizontal | horizontalScrollTest        |



