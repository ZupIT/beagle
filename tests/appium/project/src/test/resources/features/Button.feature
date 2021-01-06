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
    
@button @android @ios
Feature: Button Component Validation

    As a Beagle developer/user
    I'd like to make sure my button component works as expected
    In order to guarantee that my application never fails

    Background:
        Given that I'm on the button screen

    Scenario: Button 01 - Button component renders text attribute correctly
        Then all my button components should render their respective text attributes correctly

    Scenario Outline: Button 02 - Button component renders action attribute correctly
        When I click on button <buttonText>
        Then component should render the action attribute correctly

        Examples:
            |buttonText                       |
            |Button                           |
            |Button with style                |
            |Button with Appearance           |
