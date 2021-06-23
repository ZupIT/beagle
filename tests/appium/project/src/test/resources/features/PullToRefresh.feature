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

@pullToRefresh @android
Feature: PullToRefresh component validation

  As a Beagle developer/user
  I'd like to make sure my PullToRefresh component works as expected


  Background:
    Given that I'm on the pull to refresh screen

  Scenario: Pull to refresh 01 - test onPull event and property isRefreshing of element PullToRefresh
    When I swipe down from the center of the screen
    And I click on button Alert OK button
    Then the loading element should not be showing

