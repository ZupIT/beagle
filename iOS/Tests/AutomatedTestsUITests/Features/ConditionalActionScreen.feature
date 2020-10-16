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

@conditionalaction @regression
Feature: ConditionalAction Component Validation

    As a Beagle developer/user
    I'd like to make sure my condition component works as expected
    In order to guarantee that my application never fails

    Background:
        Given the Beagle application did launch with the conditional action screen url

    Scenario Outline: Conditional Action 01 - The conditional action get some result and show alert
        When I press the "<buttonTitle>" button
        Then the screen should show some alert with "<alertTitle>" title


    Examples:
        |          buttonTitle                |          alertTitle        |
        |   Action on True                    |    TRUE                    |
        |   Action on False                   |    FALSE                   |
        |   Action on expression true         |    TRUE                    |
        |   Action on invalid expression      |    FALSE                   |
