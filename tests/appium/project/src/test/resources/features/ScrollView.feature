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

@scrollview @android @ios
Feature: ScrollView Component Validation

  As a Beagle developer/user
  I'd like to make sure my scrollView component works as expected
  In order to guarantee that my application never fails

  Background:
    Given that I'm on the scrollview screen

  Scenario: ScrollView 01  - interact with elements of the ScrollView 1
    When I access ScrollView 1
    And I confirm that the ScrollView 1 is not showing a button with text "horizontal scroll" by default
    And I expand all the items of ScrollView 1, checking their new values
    Then I should view a button with text "horizontal scroll" by scrolling ScrollView 1 to the end

  Scenario: ScrollView 02  - interact with elements of the ScrollView 2
    When I access ScrollView 2
    And I confirm that the ScrollView 2 is showing a button with text "vertical scroll" by default
    And I expand all the items of ScrollView 2, checking their new values
    Then I should view a button with text "vertical scroll" by scrolling ScrollView 2 to the end

  Scenario: ScrollView 03  - interact with elements of the ScrollView 3
    When I access ScrollView 3
    And I confirm that the ScrollView 3 is showing a button with text "vertical direction" by default
    And I confirm that the ScrollView 3 is not showing a button with text "horizontal direction" by default
    And I expand all the items of ScrollView 3, checking their new values
    Then I should view a button with text "vertical direction" by scrolling ScrollView 3 to the end
    And I should view a button with text "horizontal direction" by scrolling ScrollView 3 to the end