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
        Given the Beagle application did launch with the texts on screen

    Scenario Outline: Text 01 - Text component renders text attribute correctly
        Then my text component <textAttribute> must be renderded on screen

        Examples:
            | textAttribute          |
            | TextValue              |
            | TextValueViaExpression |

    Scenario Outline: Text 02 - Text component renders text with textColor correctly
        Then my text component <textWithColor> should render their respective color <color> correctly

        Examples:
            | textWithColor              | color   |
            | TextWithColor              | #008000 |
            | TextWithColorViaExpression | #008000 |

    Scenario Outline: Text 03 - Text component renders text with textAlignment correctly
        Then my text component <textAttribute> should render itself with a textAlignment <Position>

        Examples:
            | textAttribute                  | Position |
            | TextAlignedLeft                | LEFT     |
            | TextAlignedCenter              | CENTER   |
            | TextAlignedRight               | RIGHT    |
            | TextAlignedLeftViaExpression   | LEFT     |
            | TextAlignedCenterViaExpression | CENTER   |
            | TextAlignedRightViaExpression  | RIGHT    |
