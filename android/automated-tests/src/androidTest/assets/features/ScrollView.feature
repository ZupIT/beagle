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
        Then the text <textScrollHorizontal> should change for the next text
        And the scrollview should perform horizontally going up <buttonScrollVertical>

        Examples:
            | textScrollHorizontal                    | buttonScrollVertical |
            | Click to see the new text in horizontal | horizontal scroll    |

    Scenario Outline: ScrollView 03 - scrollView component should be render the correctly texts in horizontal even if the screen is rotated
        When I press on text to be scrolled and rotated <textScrollViewRotate>
        Then the <textScrollViewRotate> text of the horizontal scrollview with rotation should change
        And the scrollview rotate should perform horizontally <buttonScrollVertical>
        And even if the screen is rotated the scrollview must be perform horizontally <buttonScrollHorizontal>

        Examples:
            | textScrollViewRotate                                         | buttonScrollHorizontal |
            | Click to see the text change, rotate and scroll horizontally | horizontal scroll      |

    Scenario Outline: ScrollView 04 - scrollView component should be render the correctly texts and perform the scroll vertically
        When I press on text scrollview vertical <textScrollVertical>
        Then the text should change
        And the scrollview should perform vertically <textScrollVertical>

        Examples:
            | textScrollVertical                    |
            | Click to see the new text in vertical |

    Scenario Outline: ScrollView 05 - scrollView component should be render the correctly texts in vertical even if the screen is rotated
        When I press on text scrollview to be rotate <textScrollRotate>
        Then the text vertical of scrollview rotate should change
        And the scrollview rotate should perform vertically <textScrollRotate>
        And even if the screen is rotated the scrollview must be perform vertically <buttonScrollVertical>

        Examples:
            | textScrollRotate                                           | buttonScrollVertical |
            | Click to see the text change, rotate and scroll vertically | horizontal scroll    |

    Scenario: ScrollView 06 - the scrollview component should work correctly with another scrollview within
        When I press on text Click to see the new text of scrollview
        Then the scrollview should be render vertically
        And the other scrollview should be render horizontally



