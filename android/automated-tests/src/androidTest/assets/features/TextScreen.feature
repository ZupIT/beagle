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
    
@text @regression
Feature: Text Component Validation

    As a Beagle developer/user
    I'd like to make sure my Text component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the text screen

    Scenario: Text 01 - Text component renders text attribute correctly
        Then my text component must render its respective text attribute correctly

    Scenario: Text 02 - Text component renders text via expression correctly
        Then my text component should render their respective text via expression correctly

    Scenario: Text 03 - Text component renders text with textColor correctly
        Then my text component should render their respective text with textColor correctly

    Scenario: Text 04 - Text component renders text with textColor via expression correctly
        Then my text component should render their respective text with textColor via expression correctly

    Scenario: Text 05 - Text component renders text with textAlignment LEFT correctly
        Then my text component should render their respective text with textAlignment LEFT correctly

    Scenario: Text 06 - Text component renders text with textAlignment CENTER correctly
        Then my text component should render their respective text with textAlignment CENTER correctly

    Scenario: Text 07 - Text component renders text with textAlignment RIGHT correctly
        Then my text component should render their respective text with textAlignment RIGHT correctly

    Scenario: Text 08 - Text component renders text with textAlignment LEFT via expression correctly
        Then my text component should render their respective text with textAlignment LEFT via expression correctly

    Scenario: Text 09 - Text component renders text with textAlignment CENTER via expression correctly
        Then my text component should render their respective text with textAlignment CENTER via expression correctly

    Scenario: Text 10 - Text component renders text with textAlignment RIGHT via expression correctly
        Then my text component should render their respective text with textAlignment RIGHT via expression correctly