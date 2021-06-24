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

@container @android @ios
Feature: Container component validation

  As a Beagle developer/user
  I'd like to make sure my container component works as expected

  Background:
    Given the Beagle application did launch with the container screen url

  Scenario: Container 01: validate container context validation
    Then the screen should show an element with the title item1
    And the screen should show an element with the title item2
    And the screen should show an element with the title item3
    And the screen should show an element with the title containerContextValue1
    And the screen should show an element with the title containerContextValue2
    And the screen should show an element with the title FirstActionExecuted
    And the screen should show an element with the title SecondActionExecuted
