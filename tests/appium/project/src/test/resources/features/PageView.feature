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

@pageView @android @ios
Feature: PageView Component Validation

    As a Beagle developer/user
    I'd like to make sure my pageView component works as expected

    Background:
        Given that I'm on the PageView screen

    Scenario: PageView 01 - PageView component renders text attribute correctly by screen swiping
        Then my PageView components should render their respective pages attributes correctly when swiping left

    Scenario: PageView 02 - PageView component renders text attribute correctly by context
        When I click on button Click to go to page three
        Then the screen should show an element with the title Page 3
        And the screen should show an element with the title Context2
