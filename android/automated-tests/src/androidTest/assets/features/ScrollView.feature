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

    Scenario: ScrollView 01 - scrollView component performs horizontal scroll correctly
        When I have a horizontal scroll configured
        Then scrollview screen should perform the scroll action horizontally

    Scenario Outline: ScrollView 02 - scrollView component should be render the correctly texts and perform the scroll horizontal
        When I press on text scroll horizontal <textScrollHorizontal>
        Then the text should change for the next and the scrollview should perform horizontally <textScrollHorizontal>

        Examples:
            | textScrollHorizontal                    |
            | Click to see the new text in horizontal |

    Scenario Outline: ScrollView 03 - scrollView component should be render the correctly texts in horizontal even if the screen is rotated
        When I press on text to be scrolled and rotated <textScrollViewRotate>
        Then the text horizontal of scrollview rotate should change
        And the scrollview rotate should perform horizontally <textScrollViewRotate>
        And even if the screen is rotated the scrollview must be perform horizontally <textScrollViewRotate>

        Examples:
            | textScrollViewRotate                                         |
            | Click to see the text change, rotate and scroll horizontally |

    Scenario: ScrollView 04 - scrollView component performs vertical scroll correctly
        When I have a vertical scroll configured
        Then scrollview screen should perform the scroll action vertically

    Scenario Outline: ScrollView 05 - scrollView component should be render the correctly texts and perform the scroll vertically
        When I press on text scrollview vertical <textScrollVertical>
        Then the text should change
        And the scrollview should perform vertically <textScrollVertical>

        Examples:
            | textScrollVertical                    |
            | Click to see the new text in vertical |

    Scenario Outline: ScrollView 06 - scrollView component should be render the correctly texts in vertical even if the screen is rotated
        When I press on text scrollview to be rotate <textScrollRotate>
        Then the text vertical of scrollview rotate should change
        And the scrollview rotate should perform vertically <textScrollRotate>
        And even if the screen is rotated the scrollview must be perform vertically <textScrollRotate>

        Examples:
            | textScrollRotate                                           |
            | Click to see the text change, rotate and scroll vertically |


