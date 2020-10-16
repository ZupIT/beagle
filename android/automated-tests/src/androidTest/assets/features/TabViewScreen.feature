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

@tabview @regression
Feature: TabView Component Validation

    As a Beagle developer/user
    I'd like to make sure my tabView component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the tabview screen

    Scenario: TabView 01 - TabView component renders text attribute correctly
        Then my tabview components should render their respective tabs attributes correctly

    Scenario Outline: TabView 02 - Tabs render all text attribute correctly
        When I click on text <text1>
        Then my tab should render the text <text2> and <text3> correctly

        Examples:
        | text1 | text2            | text3                      |
        |Tab 1  | Welcome to Tab 1 | This is Tab1's second text |
        |Tab 2  | Welcome to Tab 2 | This is Tab2's second text |
        |Tab 3  | Welcome to Tab 3 | This is Tab3's second text |
        |Tab 4  | Welcome to Tab 4 | This is Tab4's second text |