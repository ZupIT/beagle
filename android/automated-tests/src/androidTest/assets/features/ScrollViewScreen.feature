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
        Given that I'm on the scrollview screen

    Scenario: ScrollView 01 - scrollView component renders text attribute correctly
        Then scrollview screen should render all text attributes correctly

    Scenario: ScrollView 02 - scrollView component performs vertical scroll correctly
        When I have a vertical scroll configured
        Then scrollview screen should perform the scroll action vertically

    Scenario: ScrollView 03 - scrollView component performs horizontal scroll correctly
        When I have a horizontal scroll configured
        Then scrollview screen should perform the scroll action horizontally